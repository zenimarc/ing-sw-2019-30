package controller;
import attack.Attack;
import attack.DistanceAttack;
import board.*;
import board.Cell;
import board.billboard.Billboard;
import constants.Color;
import constants.Constants;
import constants.EnumActionParam;
import deck.AmmoCard;
import deck.Bullet;
import deck.Card;
import org.jetbrains.annotations.NotNull;
import player.Pawn;
import powerup.PowerCard;
import player.Player;
import powerup.PowerUp;
import weapon.AreaWeapon;
import weapon.EnumWeapon;
import weapon.PriorityWeapon;
import weapon.WeaponCard;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

import static constants.Constants.ACTION_PER_TURN_NORMAL_MODE;
import static constants.EnumActionParam.*;
import static controller.EnumCommand.*;
import static deck.Bullet.toIntArray;
import static powerup.PowerUp.TARGETING_SCOPE;
import static powerup.PowerUp.TAGBACK_GRENADE;


/**
 * PlayerController is used to control if a player can do certain actions
 */
public class PlayerController extends Observable{
    private BoardController boardController;
    private Billboard billboard;
    private Player player;
    private int numAction = 0;
    private ArrayList<Cell> modifyCell;
    private boolean askForPowerUp = true;
    private List<Player> enemies;
    private Cell supportCell4ComboAction;
    private Color supportColor;

    /**
     * Constructors
     */
    public PlayerController(Player player) {
        this.player = player;
    }

    public PlayerController(Player player, @NotNull BoardController boardController) {
        this(player);
        this.boardController = boardController;
        this.billboard = boardController.getBoard().getBillboard();
    }

    public void setBillboard(Billboard board){this.billboard = board;}

    public void setBoardController(BoardController board){this.boardController = board;}

    public Player getPlayer() {
        return player;
    }

    public void receiveCmd(CommandObj cmdObj){
        PowerCard powerCard;

        switch (cmdObj.getCmd()) {
            case GRAB_MOVE:
            case GRAB_MOVE_FRENZY_BEFORE_FIRST:
            case GRAB_MOVE_FRENZY_AFTER_FIRST:
            case SHOOT_MOVE_FRENZY_BEFORE_FIRST:
            case SHOOT_MOVE_FRENZY_AFTER_FIRST:
            case SHOOT_MOVE:
                moveAndDoSomethingElse(cmdObj);
                break;
            case MOVE_FRENZY:
            case MOVE:
                Cell c = billboard.getCellFromPosition((Position) (cmdObj.getObject()));
                if (move(c, cmdObj.getCmd())) {
                    numAction++;
                } else {
                    viewPrintError();
                }
                break;
            case GRAB_AMMO:
                if (grabAmmo()) {
                    numAction++;
                } else {
                    deleteMovement();
                    viewPrintError();
                }
                player.notifyEndAction();
                break;
            case GRAB_WEAPON:
                int grabWeaponIndex = (int) cmdObj.getObject();
                grabWeaponManager(grabWeaponIndex);
                break;
            case USE_POWER_UP:
            case CHECK_EVERY_TIME_POWER_UP:
                powerUpManager(cmdObj);
                break;
            case PAYPOWERUP:
                if ((int) cmdObj.getObject() != -1) {
                    if (player.canPayPower(player.getPowerups().get((int) cmdObj.getObject())))
                        cmdForView(new CommandObj(PAYPOWERUP, player.getPowerups().get((int) cmdObj.getObject())));
                    else {
                        PowerCard power = player.getPowerups().get((int) cmdObj.getObject());
                        player.usePowerUp(power, true);
                        boardController.getBoard().getDiscardAmmoCardDeck().addCard(power);
                        receiveCmd(verifyPowerUp(power));
                    }
                } else player.notifyEndAction();
                break;
            case PAIDPOWERUP:
                player.usePowerUp((PowerCard) cmdObj.getObject(), (Boolean) cmdObj.getObject2());
                receiveCmd(verifyPowerUp((PowerCard) cmdObj.getObject()));
                break;
            case NEWTON_TARGET:
                Player p = boardController.getListOfPlayers().stream().filter(x -> x.getName().equals((String) cmdObj.getObject())).findFirst().orElse(null);
                if (p != null) {
                    List<Position> potentialPosition = boardController.getCellsKineticRay(p.getCell());
                    if (!potentialPosition.isEmpty()) {
                        cmdForView(new CommandObj(NEWTON_TARGET, potentialPosition, p.getName()));
                    }
                }
                break;
            case TELEPORTER:
                setCell(billboard.getCellFromPosition((Position) (cmdObj.getObject())));
                payPowerUp();
                player.notifyEndAction();
                break;
            case USE_NEWTON:
                Cell kntCell = billboard.getCellFromPosition((Position) cmdObj.getObject());
                Player kntPlayer = boardController.getPlayer((String) cmdObj.getObject2());
                setCell(kntPlayer, kntCell);
                payPowerUp();
                break;
            case USE_TAGBACK_GRENADE:
                powerCard = player.getPowerups().stream().filter(x->x.getPowerUp().equals(TAGBACK_GRENADE)).findFirst().orElse(null);
                if(powerCard!=null)
                    if(player.canPay(Bullet.colorToArray(powerCard.getColor()))) {
                        player.useTagbackGrenade();
                        player.useAmmo(Bullet.colorToArray(powerCard.getColor()));
                        player.notifyEndAction();
                    }
                break;
            case TARGETING_SCOPE:
                useTargetingScope(cmdObj);
                break;
            case DISCARD_POWER:
                PowerCard power = (PowerCard) cmdObj.getObject();
                if (player.getPowerups().contains(power))
                    player.getPowerups().remove(power);
                askForPowerUp = false;
                break;
            case END_TURN:
                numAction += ACTION_PER_TURN_NORMAL_MODE.getValue();
                break;
            case REG_CELL:
                powerCard = (PowerCard) cmdObj.getObject();
                if (boardController.setRegenerationCell(player, powerCard.getColor())) {
                    player.usePowerUp(powerCard, true);
                    boardController.getBoard().addPowerUpDiscardDeck(powerCard);
                }
                break;
            case DISCARD_WEAPON:
                int discardIndex = cmdObj.getObject().getClass().equals(Integer.class) ? (int) cmdObj.getObject() : -1;
                if (discardIndex != -1 && cmdObj.getObject2().getClass().equals(Integer.class)) {
                    discardWeapon(discardIndex, (int) cmdObj.getObject2());
                }
                break;
            case SHOOT_CHECK:
                if (player.getNumDamages() >= ADRENALINIC_SECOND_STEP.getNum()) {
                    cmdForView(new CommandObj(SHOOT_MOVE));
                } else {
                    cmdForView(new CommandObj(SHOOT));
                }
                break;
            case SHOOT:
                shootManager(cmdObj);
                break;
            case LOAD_WEAPONCARD:
                try {
                    int index = (Integer) cmdObj.getObject();
                    WeaponCard wpToLoad = player.getNotLoaded().get(index);
                    if (index >= player.getNotLoaded().size()) viewPrintError("Bad index");
                    if (player.canPay(toIntArray(wpToLoad.getCost()))) {
                        wpToLoad.setLoaded();
                        player.useAmmo(toIntArray(wpToLoad.getCost()));
                    } else {
                        viewPrintError("You have not enough ammo to load this weapon");
                    }
                } catch (ClassCastException cce) {
                    viewPrintError("Bad object arrived in PlayerController." + cce.getMessage());
                    break;
                }
                player.notifyEndAction();
                break;
            default:
                break;
        }
    }

    /**
     * TO USE POWER UP: CommandObj format: USE_POWER_UP, PowerCard to use
     * @param cmdObj operation using powerup
     */
    private void powerUpManager(CommandObj cmdObj){
        List<PowerCard> powerUps;

        switch (cmdObj.getCmd()) {
            case CHECK_EVERY_TIME_POWER_UP:
                powerUps = player.getPowerups().stream()
                        .filter((x -> (x.getPowerUp() == PowerUp.TELEPORTER || x.getPowerUp() == PowerUp.NEWTON) &&
                                player.canPay(Bullet.colorToArray(x.getColor()))))
                        .collect(Collectors.toList());
                if (!powerUps.isEmpty()) {
                    cmdForView(new CommandObj(ASK_FOR_POWER_UP, powerUps));
                } else {
                    viewPrintError("No PowerUp usable");
                }
                break;
            case USE_POWER_UP:
                PowerCard powerUp =  cmdObj.getObject().getClass().equals(PowerCard.class) ? (PowerCard) cmdObj.getObject() : null;
                if(powerUp!=null && player.getPowerups().stream().anyMatch(x-> x.equals(powerUp))) {
                    supportColor = powerUp.getColor();
                    player.getPowerups().stream().filter(x -> x.equals(powerUp)).findFirst().ifPresent(x -> {
                                if (x.getPowerUp().equals(PowerUp.TELEPORTER)) {
                                    cmdForView(new CommandObj(USE_TELEPORTER));
                                } else if (x.getPowerUp().equals(PowerUp.NEWTON)) {
                                    cmdForView(new CommandObj(USE_NEWTON, boardController.getListOfPlayers()
                                            .stream().filter(y -> !y.getName().equals(this.player.getName())).collect(Collectors.toList())));
                                }
                            }
                    );
                }
                break;
            default:
                break;
        }
    }

    /**
     * This function controls the actions with movement and verifies if a player can move to a cell
     * @param cell of destination
     * @return if the player can move, else false
     */
    public boolean move(Cell cell, EnumCommand enumCommand){
        if(player.getCell() == cell) return true;

        EnumActionParam actionParam;
            //NOT FINAL FRENZY
            switch (enumCommand) {
                case MOVE: //normal mode
                    actionParam = NORMAL_MOVE;
                    break;
                case GRAB_MOVE:// move for grab
                    actionParam = player.getNumDamages()<ADRENALINIC_FIRST_STEP.getNum() ? NORMAL_GRAB_MOVE : ADRENALINIC_GRAB_MOVE;
                    break;
                case SHOOT_MOVE: //move from shoot
                    actionParam = player.getNumDamages()<ADRENALINIC_SECOND_STEP.getNum() ? NORMAL_SHOOT_MOVE : ADRENALINIC_SHOOT_MOVE;
                    break;
                case MOVE_FRENZY: //normal mode
                    actionParam = FRENZY_MOVE;
                    break;
                case GRAB_MOVE_FRENZY_BEFORE_FIRST:// move for grab
                    actionParam = FRENZY_GRAB_MOVE_BEFORE_FIRST;
                break;
                case GRAB_MOVE_FRENZY_AFTER_FIRST:// move for grab
                    actionParam = FRENZY_GRAB_MOVE_AFTER_FIRST;
                break;
                case SHOOT_MOVE_FRENZY_AFTER_FIRST: //move from shoot
                    actionParam = FRENZY_SHOOT_MOVE_AFTER_FIRST;
                    break;
                case SHOOT_MOVE_FRENZY_BEFORE_FIRST: //move from shoot
                    actionParam = FRENZY_SHOOT_MOVE_BEFORE_FIRST;
                    break;
                default:
                    return false;
            }
            if(billboard.canMove(player.getCell(), cell, actionParam.getNum())){
                setCell(cell);
                return true;
            }else {
                viewPrintError("Illegal movement");
            }

        return false;
    }

    /**
     * Player pay to use powerUp
     */
    private void payPowerUp(){
        if(supportColor!=null) {
            player.useAmmo(Bullet.colorToArray(supportColor));
            supportColor = null;
        }
    }

    /**
     * This function controls GRAB action in a NormalCells (Grab Ammocard)
     * @return success of operation
     */
    private boolean grabAmmo(){
        AmmoCard ammoCard = (AmmoCard) boardController.getBoard().giveCardFromCell(player.getCell(), player, 0);
        if(ammoCard!=null) {
            Card power = boardController.getBoard().getPowerUpDeck().draw();
            if (ammoCard.verifyPowerUp()){
                if(player.getPowerups().size() == 3) {
                    cmdForView(new CommandObj(DISCARD_POWER, power));
                    while(true){
                        if(!askForPowerUp)
                            break;
                    }
                        askForPowerUp = true;
                    if(player.getPowerups().size() < 3)
                        player.getPowerups().add((PowerCard) power);
                }
            else player.addPowerCard((PowerCard)power);

            }
            modifyCell.add(player.getCell());
            return true;
        }
        return false;
    }

    /**
     * This function controls GRAB action in a RegenerationCells (Grab Weapon)
     * @param grabWeaponIndex index of weapon to grab
     * @return -1 can't grab, 0 can grab but must discard, 1 can grab
     */
    private int checkCanGrabWeapon(int grabWeaponIndex){
        WeaponCard grabWeapon = (WeaponCard) (player.getCell().getCard(grabWeaponIndex));

        if(grabWeapon == null){
            viewPrintError("No weapon selected");
            return -1;
        }
        int[] grabCost = toIntArray(grabWeapon.getGrabCost());
        //Can player pay this weaponCard?
        if(player.canPay(grabCost)) {
            //Can player draw another WeaponCard?
            if (player.getWeapons().size() == Constants.MAX_WEAPON_HAND_SIZE.getValue()) {
                return 0; //Must discard
            }else {
                return 1; //Can grab
            }
        }else {
            //Can't pay
            viewPrintError("You have no bullets to grab this weapon!");
        }
        return -1;
    }

    /**
     * This verify player can grab selected weapon than invoke method to grab weapon or discard and grab weapon
     * @param grabWeaponIndex weapon index to grab
     */
    private void grabWeaponManager(int grabWeaponIndex){
        switch (checkCanGrabWeapon(grabWeaponIndex)) {
            case 0:
                cmdForView(new CommandObj(DISCARD_WEAPON, player.getWeapons().stream()
                        .map(WeaponCard::getName).collect(Collectors.toList()), grabWeaponIndex));
                break;
            case 1:
                if (grabWeapon(grabWeaponIndex)){
                    numAction++;
                } else deleteMovement();
                break;
            default:
                break;
        }
        player.notifyEndAction();
    }

    /**
     * This function restores player's cell to original if action is not valid or player did a wrong action
     */
    private void deleteMovement() {
        if (supportCell4ComboAction != null) {
            setCell(supportCell4ComboAction);
            supportCell4ComboAction = null;
        }
    }

    /**
     * This function is used to deal with grab and shoot actions
     * @param cmdObj object with info
     * @return true
     */
    private boolean moveAndDoSomethingElse(CommandObj cmdObj){
        supportCell4ComboAction = player.getPawn().getCell();

        if(!move(billboard.getCellFromPosition((Position) (cmdObj.getObject())), cmdObj.getCmd())){
            viewPrintError("You cannot do this movement");
            return false;
        }

        switch (cmdObj.getCmd()) {
            case GRAB_MOVE_FRENZY_AFTER_FIRST:
            case GRAB_MOVE_FRENZY_BEFORE_FIRST:
            case GRAB_MOVE:
                cmdForView(new CommandObj(GRAB, player.getCell()));
                break;
            case SHOOT_MOVE:
                cmdForView(new CommandObj(SHOOT));
                break;
            case SHOOT_MOVE_FRENZY_BEFORE_FIRST:
            case SHOOT_MOVE_FRENZY_AFTER_FIRST:
                askForLoad();
                cmdForView(new CommandObj(SHOOT));
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * This function is used when player needs to discard a weapon
     * @param discardIndex of weapon to discard
     * @param grabIndex of weapon to be picked
     */
    private void discardWeapon(int discardIndex, int grabIndex) {
        Card discardWeapon = player.rmWeapon(discardIndex);
        grabWeaponManager(grabIndex);
        boardController.getBoard().addCardInCell(discardWeapon, player.getCell());

    }

    private boolean grabWeapon(int weaponIndex) {
        //Draw weaponCard
        WeaponCard weaponCard = (WeaponCard) boardController.getBoard().giveCardFromCell(player.getCell(), player, weaponIndex);
        if (weaponCard != null) {
            //Set weapon load
            weaponCard.setLoaded();
            //Add cell to modify cell
            modifyCell.add(player.getCell());
            player.useAmmo(toIntArray(weaponCard.getGrabCost()));
            return true;
        } else {
            viewPrintError("You can't draw another WeaponCard");
            return false;
        }
    }

    /**
     * This function is used to deal with Shoot actions
     * @param cmdObj
     */
    private void shootManager(CommandObj cmdObj){
        if(cmdObj.getObject().getClass().equals(Integer.class)){
            deleteMovement();
            return;
        }

        if(checkedShoot(cmdObj)){
            numAction++;
            player.notifyEndAction();
        }else{
            deleteMovement();
        }
    }

    /**
     * This function checks if player can SHOOT
     * @param cmdObj data to use
     */
    private boolean checkedShoot(CommandObj cmdObj) {
        WeaponCard weaponCard = (WeaponCard) cmdObj.getObject();
        //Checked load weapon
        if (!weaponCard.isReady()) {
            viewPrintError("This weapon is not loaded");
            return false;
        }
        //Checked good attack selector
        int selector = cmdObj.getWeaponSelector();
        if (!isGoodSelector(selector, weaponCard)) {
            viewPrintError("Selected attack is not usable");
            return false;
        }

        if (selector == -1) return false;
        //aggiungere caso delle priority weapons per cui un optional va bene anche prima di un attacco base
        boolean isGoodAttack = false;
        if (selector==0){
            //BASE ATTACK
            if(shootBaseAttack(weaponCard)) isGoodAttack = true;
        }else if (!weaponCard.getAttacks().isEmpty()) {
            //BASE ATTACK + OPTIONAL ATTACK
            if(shootOptionalAttack(weaponCard)) isGoodAttack = true;
        }else if(weaponCard.getAlternativeAttack()!=null){
            //ALTERNATIVE ATTACK
            if(shootAlternativeAttack(weaponCard)) isGoodAttack = true;
        }

        if(isGoodAttack) {
            enableOpponentsForTagbackGrenade();
            verifyTargetingScope();
            enemies.clear();
            return true;
        }else {
            viewPrintError("Failed attack");
        }
        return false;
    }

    /**
     *Support for Tagback Grenade. Search if some player have Tagback Grenade and if can pay, if true enable tagback grenade
     */
    private void enableOpponentsForTagbackGrenade(){
        List<Player> players = enemies.stream().filter(x -> x!= null &&
                !(x.haveTagbackGranade().isEmpty()) &&
                boardController.getBoard().getBillboard().visibleCells(x.getCell()).contains(this.player.getCell())
                )
                .collect(Collectors.toList());
        if(!players.isEmpty()){
            for(Player opponent: players){
                if(opponent.haveTagbackGranade().stream().anyMatch(x-> opponent.canPay(Bullet.colorToArray(x.getColor())))){
                    opponent.enableTagbackGrenade(player);
                }
            }
        }
    }

    /**
     * Check if player can pay, than add damage to "Sfigghi" opponents
     * @param cmdObj cmdObj contains good news!
     */
    private void useTargetingScope(CommandObj cmdObj){
        Player p;
        int[] cubes;
        if(cmdObj.getObject().getClass().equals(Player.class) && cmdObj.getObject2().getClass().equals(Color.class)){
            p = boardController.getPlayer(((Player) cmdObj.getObject()).getName());
            cubes = Bullet.colorToArray((Color) cmdObj.getObject2());
            if(player.canPay(cubes)){
                player.addDamage(p);
                p.notifyEndAction();
                player.useAmmo(cubes);
                player.notifyEndAction();
            }else {
                viewPrintError("You can't pay this effect.");
            }
        }else viewPrintError("Format Error, sorry!");

    }

    /**
     * Check if player can use TARGETING_SCOPE to add a damage to 1 of opponents just hit
     */
    private void verifyTargetingScope(){
        List<PowerCard> powerCards = this.player.getPowerups().stream()
                .filter(x->x.getPowerUp().equals(TARGETING_SCOPE)).collect(Collectors.toList());
        if(powerCards.isEmpty()) return;
        if(player.canPay(new int[]{1,0,0})|| player.canPay(new int[]{0,1,0})||player.canPay(new int[]{0,0,1})){
            CommandObj commandObj = new CommandObj(EnumCommand.TARGETING_SCOPE,
                    enemies.stream().filter(x->x!=null).collect(Collectors.toList()));
            cmdForView(commandObj);
        }
    }

    /**
     * This asks player which optional attack wants to use, if 0 -> use only BaseAttack.
     * Than verifies if can pay optional attack, asks which opponents want to shoot and then shoots
     * @param weaponCard Weapon to shot opponents
     * @return Shoot someone?
     */
    private boolean shootOptionalAttack(WeaponCard weaponCard){
        List<Integer> indexes;

        try {
            indexes = boardController.getGameServer().chooseIndexes(weaponCard.getAttacks(), true);
        }catch (RemoteException re){
            return false;
        }
        if (indexes.isEmpty()) return shootBaseAttack(weaponCard);

        int maxTarget;
        List<Attack> attacks = new ArrayList<>();
        //Add base attack and selected attack in "attacks" (list to shoot)
        attacks.add(weaponCard.getBaseAttack());
        indexes.forEach(x -> attacks.add(weaponCard.getAttack(x)));
        //Calc max target
        maxTarget = attacks.stream().mapToInt(Attack::getTarget).max().orElse(0);

        if(weaponCard.getClass() == PriorityWeapon.class){
            if(weaponCard.getAttack(0).getTarget() == 0)

            cmdForView(new CommandObj(PRIORITY_OPTIONAL));

        }
        //Ask for opponents to shoot
        List<Player> opponents = getTargets(weaponCard.getBaseAttack().getTargetType(), maxTarget);
        //User can't attack or decided to not attack
        if (opponents.isEmpty()){
            viewPrintError("Failed attack");
            return false;
        }
        //Add null opponents to have opponents.size() == attack.target
        stdPlayerList(opponents, maxTarget);

        //Attack with base attack
        weaponCard.shoot(0,player, opponents, Optional.empty());
        //Attack with optional attack
        for(Integer index : indexes) {
            if (!player.canPay(weaponCard.getAttack(index).getCost())) {
                viewPrintError("You have not enough bullet to use this attack");
            }else{
                int i = index +1;
                weaponCard.shoot(i, player, opponents, null);
                player.useAmmo(weaponCard.getAttack(index).getCost());
            }
        }
        return true;
    }

    /**
     * This function invokes shootSingleAttack to use BaseAttack
     * @param weaponCard WeaponCard to use to attack
     * @return a base single attack
     */
    private boolean shootBaseAttack(WeaponCard weaponCard){
        return shootSingleAttack(weaponCard, true);
    }

    /**
     * This function invokes shootSingleAttack to use AlternativeAttack
     * @param weaponCard WeaponCard  to use to attack
     * @return an optional single attack
     */
    private boolean shootAlternativeAttack(WeaponCard weaponCard) {
        if (!player.canPay(weaponCard.getAlternativeAttack().getCost())) {
            viewPrintError("You have not enough bullet to use this attack");
            return false;
        }

        if (shootSingleAttack(weaponCard, false)) {
            player.useAmmo(weaponCard.getAlternativeAttack().getCost());
            return true;
        } else return false;
    }

    /**
     * This function asks a player which opponents wants to hit, then verifies if they can be hit and finally shoots
     * @param weaponCard WaeponCard with the attack
     * @param baseAttack if true uses baseAttack, else uses alternativeAttack
     * @return if shooter is shoot
     */
    private boolean shootSingleAttack(WeaponCard weaponCard, boolean baseAttack){
        int attackSelector;
        int maxTarget;
        Attack attack;

        if(baseAttack){
            attackSelector = 0;
            maxTarget = weaponCard.getBaseAttack().getTarget();
            attack = weaponCard.getBaseAttack();
        }else {
            attackSelector = 1;
            maxTarget = weaponCard.getAlternativeAttack().getTarget();
            attack = weaponCard.getAlternativeAttack();
        }
        List<Player> opponents = new ArrayList<>();

        if(attack.getClass() == DistanceAttack.class && weaponCard.getClass()!=AreaWeapon.class) {
            opponents.addAll(getDistanceAttackTargets(weaponCard, attack, maxTarget));
        }else if(weaponCard.getWeaponType()==EnumWeapon.FURNACE){
            opponents = furnaceImplementation(baseAttack);
        } else {
            opponents.addAll(getTargets(attack.getTargetType(), maxTarget));
        }

        if (opponents.isEmpty()) return false;
        //Add null opponents to have opponents.size() == attack.target
        stdPlayerList(opponents, maxTarget);
        //FINALLY SHOOT!!
        enemies = opponents;
        return weaponCard.shoot(attackSelector, player, opponents, Optional.empty());
    }

    /**
     * Implements controller to use FURNACE attacks
     * @param baseAttack if use base attack
     * @return List of players to shoot
     */
    private List<Player> furnaceImplementation(boolean baseAttack){
        List<Player> opponents = new ArrayList<>();
        if(baseAttack){
            //TODO choose room - Error
            opponents.addAll(boardController.getPotentialTargets(opponents.get(0).getCell(), EnumTargetSet.SAME_ROOM));
        }else {
                List<Cell> possibleCells = boardController.getPotentialDestinationCells(player.getCell(), 1);
                try {
                    Position position = boardController.getGameServer().choosePositionToAttack(possibleCells);
                    Cell c = boardController.getBoard().getBillboard().getCellFromPosition(position);
                    if(possibleCells.contains(c)){
                        opponents.addAll(c.getPawns().stream().map(Pawn::getPlayer).collect(Collectors.toList()));
                    }else viewPrintError("Selected cell cannot be selected");
                }catch (RemoteException re){
                    viewPrintError(re.getMessage());
                }
            }
        return opponents;
    }


    /**
     * Implements controller to use Distanceattacks
     * @param weaponCard to use
     * @param attack to use
     * @param maxTarget players
     * @return true
     */
    private List<Player> getDistanceAttackTargets(WeaponCard weaponCard, Attack attack, int maxTarget){
        if (weaponCard.getWeaponType() != EnumWeapon.HELLION) {
            return getTargets(attack.getTargetType(), maxTarget,
                    ((DistanceAttack) attack).getMinDistance(), ((DistanceAttack) attack).getMaxDistance(), false);
        } else {
            return  getTargets(attack.getTargetType(), maxTarget,
                    ((DistanceAttack) attack).getMinDistance(), ((DistanceAttack) attack).getMaxDistance(), true);
        }
    }

    private List<Player> getTargets(EnumTargetSet targetType, int maxTarget, int minDistance, int maxDistance, boolean hitOpponentSameCell) {
        List<Player> potentialTarget = boardController.getPotentialTargets(player.getCell(), targetType, minDistance, maxDistance);
        List<Player> chosenTargets = chooseTargets(potentialTarget, maxTarget);
        if (!hitOpponentSameCell) {
            return chosenTargets;
        }else {
            chosenTargets.addAll(boardController.getPotentialTargets(chosenTargets.get(0).getCell(), EnumTargetSet.SAME_CELL));
            return chosenTargets;
        }
    }

    /**
     * This function verifies if the target is hittable or not
     * @param maxTarget max opponents to shoot
     * @return true if the attack was successful, false otherwise
     */
    private List<Player> getTargets(@NotNull EnumTargetSet targetType, int maxTarget) {
        List<Player> potentialTargets = boardController.getPotentialTargets(player.getCell(), targetType);
        return chooseTargets(potentialTargets, maxTarget);
    }

    private List<Player> chooseTargets(List<Player> potentialTargets, int maxTarget){
        if(potentialTargets.isEmpty()) {
            viewPrintError("You have not any possible targets");
            return Collections.emptyList();
        }

        if(maxTarget==-1){
            return potentialTargets;
        }else {
            List<Player> opponents;
            try {
                opponents = boardController.getGameServer().getTargets(potentialTargets, maxTarget);
            }catch (RemoteException re){
                viewPrintError(re.getMessage());
                opponents = Collections.emptyList();
            }

            if(areGoodTarget(potentialTargets, opponents)){
                enemies = opponents;
                return opponents;
            }else{
                return Collections.emptyList();
            }
        }

    }

    /**
     * Checks if selected opponents are good targets
     * @param potentialTargets potential good targets
     * @param selectedTargets selected target by shooter
     * @return if selected target are good target
     */
    private boolean areGoodTarget(List<Player> potentialTargets, List<Player> selectedTargets){
        if (selectedTargets.isEmpty()) return false;
        if (!potentialTargets.containsAll(selectedTargets)) {
            viewPrintError("You can't attack one (or more) selected player(s)");
            return false;
        }
        return true;
    }

    /**
     * This function verifies if attack selector is good,
     * Only BaseAttack -> 0-1
     * BaseAttack + OptionalAttack -> 0-num_of_optional_attack+1
     * AlternativeAttack -> 0-2
     * @param selector of attack
     * @param wc WeaponCard
     * @return true
     */
    private boolean isGoodSelector(int selector, @NotNull WeaponCard wc){

        if(wc.getAttacks().isEmpty() && wc.getAlternativeAttack()==null){
            return (selector==0 || selector == -1);
        }else if(!wc.getAttacks().isEmpty() ^ wc.getAlternativeAttack()!=null){
            return (selector>=-1 && selector<=1);
        }
        return false;
    }

    /**
     * This increase players's size to num (add null Player)
     * @param players list of players
     * @param num final num of players in players list
     */
    private void stdPlayerList(@NotNull List<Player> players, int num){
        if (players.size() < num) {
            for (int diff = 0; diff < num; diff++) {
                players.add(null);
            }
        }
    }

    /**
     * This function modifies the position of the pawn of another player
     * @param player to be moved
     * @param cell of destination
     */
    private void setCell(@NotNull Player player, Cell cell){
        this.boardController.getBoard().setPlayerCell(player, cell);
    }

    /**
     * This function modifies the position of the pawn
     * @param cell of destination
     */
    public void setCell(Cell cell){
        setCell(this.player, cell);
    }

    /**
     * This function verifies which power up the Power Card has, then creates a new Commandobj
     * with the instruction for next action
     * @param power to verify Power up
     * @return a CommandObj
     */
    private CommandObj verifyPowerUp(PowerCard power) {
        switch(power.getPowerUp()) {
            case NEWTON:
                return new CommandObj(USE_NEWTON);
            case TARGETING_SCOPE:
                return new CommandObj(USE_GUNSIGHT);
            case TAGBACK_GRENADE:
                return new CommandObj(USE_TAGBACK_GRENADE);
            case TELEPORTER:
                return new CommandObj(USE_TELEPORTER);

        }
        return null;
    }

    /**
     * This function filters player's Power cards depending on the situation
     * @param obj command by which cards are filtered
     * @return usable Power Cards
     */
    @Deprecated
    private ArrayList<PowerCard> getPotentialPowerUps(CommandObj obj){
        ArrayList<PowerCard> powers = new ArrayList<>();
        for(PowerCard power: player.getPowerups()){
            if(obj.getObject() == TAGBACK_GRENADE && boardController.getPlayerWhoPlay() != player && boardController.getBoard().getBillboard().isVisible(player.getCell(), boardController.getPlayerWhoPlay().getCell()))
                powers.add(power);
            else {
                if(power.getPowerUp() == TARGETING_SCOPE && !enemies.isEmpty()) {
                    if (power.getPowerUp() == TARGETING_SCOPE && (player.canPay(new int[]{1,0,0}) || player.canPay(new int[]{0, 1, 0}) || player.canPay(new int[]{0, 0, 1})))
                        powers.add(power);
                }
                else if(power.getPowerUp() == PowerUp.TELEPORTER || (power.getPowerUp() == PowerUp.NEWTON && boardController.notNullCellPlayers().size() > 0))
                    powers.add(power);

            }
        }
        return powers;
    }

    public List<Cell> getModifyCell() {
        return modifyCell;
    }

    /**
     * This function manages turn of a player
     * @param maxAction number of actions which can be performed in a turn
     */
    protected void myTurn(Constants maxAction){
        modifyCell = new ArrayList<>();

        cmdForView(new CommandObj(SHOW_BOARD));

        if(player.getCell()==null) regCell();

        cmdForView(new CommandObj(YOUR_TURN));

        if(this.player.canUseTagbackGrenade()){
            cmdForView(new CommandObj(USE_TAGBACK_GRENADE, Arrays.asList(player.getPotentialTagbackGrenade())));
        }

        while(numAction < maxAction.getValue()) {
            cmdForView(new CommandObj(TIME_TO_PLAY, maxAction));
        }
        askForLoad();
        numAction = 0;
    }

    /**
     * If player has not loaded weapon ask him if want load it
     */
    private void askForLoad(){
        if(!player.getNotLoaded().isEmpty()) {
            cmdForView(new CommandObj(LOAD_WEAPONCARD, player.getNotLoadedName()));
        }
    }

    private void viewPrintError(){
        viewPrintError("");
    }


    private void viewPrintError(String mex){
        cmdForView(new CommandObj(PRINT_ERROR, mex));
    }
    
    protected void cmdForView(CommandObj cmd){
        setChanged();
        notifyObservers(cmd);
    }

    private void setPlayerboardFrenzy(){
        if(boardController.isFinalFrenzy())
            player.getPlayerBoard().setFrenzy(true);
    }

    public void notMyTurn(String name){
        askForPowerUp = true;
        cmdForView(new CommandObj(NOT_YOUR_TURN, name));
    }

    public void regCell(){
        cmdForView(new CommandObj(REG_CELL));
    }
}