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
import deck.Card;
import org.jetbrains.annotations.NotNull;
import player.Pawn;
import powerup.PowerCard;
import player.Player;
import powerup.PowerUp;
import view.PlayerBoardView;
import view.PlayerView;
import weapon.AreaWeapon;
import weapon.EnumWeapon;
import weapon.WeaponCard;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

import static constants.Constants.ACTION_PER_TURN_NORMAL_MODE;
import static constants.EnumActionParam.*;
import static controller.EnumCommand.*;
import static deck.Bullet.toIntArray;
import static powerup.PowerUp.*;
import static powerup.PowerUp.VENOMGRENADE;

//TODO gestire meglio i power up nel caso si spari e si voglia usare un power up
/**
 * PlayerController is used to control if a player can do certain actions
 */
public class PlayerController extends Observable implements Observer{
    private BoardController boardController;
    private Billboard billboard;
    private PlayerView playerView;
    private Player player;
    private int numAction = 0;
    private ArrayList<Cell> modifyCell;
    private PlayerBoardView playerBoardView;

    /**
     * Default constructor
     */
    public PlayerController(Player player) {
        this.player = player;
    }

    public PlayerController(Player player, @NotNull BoardController boardController) {
        this(player);
        this.boardController = boardController;
        this.billboard = boardController.getBoard().getBillboard();
    }

    public PlayerBoardView getPlayerBoardView() {
        return playerBoardView;
    }

    public void setBillboard(Billboard board){this.billboard = board;}

    public void setBoardController(BoardController board){this.boardController = board;}

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    public void receiveCmd(CommandObj cmdObj){
        switch (cmdObj.getCmd()) {
            case MOVE_FRENZY:
            case GRAB_MOVE_FRENZYX1:
            case GRAB_MOVE_FRENZYX2:
            case MOVE:
            case GRAB_MOVE:
                if(move(billboard.getCellFromPosition((Position) (cmdObj.getObject())), cmdObj.getCmd())){
                    if(cmdObj.getCmd()==MOVE || cmdObj.getCmd()==MOVE_FRENZY) numAction++;
                    else cmdForView(new CommandObj(GRAB));
                }else {
                    viewPrintError();
                }
                break;
            case GRAB_AMMO:
                 if(grabAmmo()){
                    numAction++;
                }else {
                    viewPrintError();
                }
                 player.notifyEndAction();
                break;
            case GRAB_WEAPON:
                checkCanGrabWeapon((int) cmdObj.getObject());
                player.notifyEndAction();
                break;
            case ASKFORPOWERUP:
                ArrayList<PowerCard> cards = (getPotentialPowerUps(cmdObj));
                if(cards.isEmpty())
                    notifyObservers();
                else if(!playerView.askForPowerUp(cards))
                    notifyObservers();
                else playerView.usePowerUp(cards);
                break;
            case CHECKPOWERUP:
                cmdForView(new CommandObj(CHECKPOWERUP, getPotentialPowerUps(cmdObj)));
                break;
            case PAYGUNSIGHT:
                playerView.askPayGunsight(player.payCubeGunsight(), (PowerCard)cmdObj.getObject());
                break;
            case GUNSIGHTPAID:
                player.canPayGunsight((Color) cmdObj.getObject2());
                receiveCmd(new CommandObj(PAYPOWERUP, cmdObj.getObject()));
                break;
            case PAYPOWERUP:
                if(player.canPayPower((PowerCard)cmdObj.getObject()))
                    playerView.askToPay((PowerCard)cmdObj.getObject());
                else {
                    player.usePowerUp((PowerCard) cmdObj.getObject(), true);
                    receiveCmd(new CommandObj(POWERUP, cmdObj.getObject()));
                }
                break;
            case PAIDPOWERUP:
                player.usePowerUp((PowerCard) cmdObj.getObject(), (Boolean) cmdObj.getObject2());
                receiveCmd(new CommandObj(POWERUP, cmdObj.getObject()));
                break;
            case POWERUP:
                receiveCmd(verifyPowerUp((PowerCard) cmdObj.getObject()));
                break;
            case USE_TELEPORTER:
                playerView.moveTeleporter();
                break;
            case TELEPORTER:
                player.setPawnCell((Cell)cmdObj.getObject());
                notifyObservers();
                break;
            case USE_KINETICRAY:
                playerView.moveKineticray(playerView.chooseTargets(1, boardController.getListOfPlayers()).get(0), boardController.getCellsKineticRay(player.getCell()));
                break;
            case KINETICRAY:
                setOtherCell((Player)cmdObj.getObject(), (Cell) cmdObj.getObject2());
                notifyObservers();
                break;
            case USE_VENOMGRENADE:
                if(boardController.getBoard().getBillboard().isVisible(((Player)cmdObj.getObject()).getCell(), player.getCell()))
                    ((Player)cmdObj.getObject()).addMark(player);
                break;
            case VENOMGRENADE:
                ((Player)cmdObj.getObject()).addMark(player);
                notifyObservers();
                receiveCmd(new CommandObj(ASKFORPOWERUP, VENOMGRENADE));
                break;
            case USE_GUNSIGHT:
                ((Player)cmdObj.getObject()).addDamage(player);
                notifyObservers();
                receiveCmd(new CommandObj(ASKFORPOWERUP, GUNSIGHT));
                break;
            case END_TURN:
                numAction+= ACTION_PER_TURN_NORMAL_MODE.getValue();
                break;
            case REG_CELL:
                PowerCard powerCard = (PowerCard) cmdObj.getObject();
                if(boardController.setRegenerationCell(player, powerCard.getColor())){
                    player.usePowerUp(powerCard, true);
                    boardController.getBoard().addPowerUpDiscardDeck(powerCard);
                }
                break;
            case DISCARD_WEAPON:
                player.rmWeapon((int) cmdObj.getObject());
                player.notifyEndAction();
                break;
            case SHOOT:
                if(player.getNumDamages() >= ADRENALINIC_SECOND_STEP.getNum())
                    if(!move(billboard.getCellFromPosition((Position) (cmdObj.getObject())), cmdObj.getCmd()))
                        viewPrintError();
                    else checkedShoot(cmdObj);
                else checkedShoot(cmdObj);
                break;
            case LOAD_WEAPONCARD:
                WeaponCard wc = (WeaponCard) cmdObj.getObject();

                if(!player.getNotLoaded().contains(wc)) viewPrintError("This weapon is already loaded");
                if(player.canPay(toIntArray(wc.getCost()))){
                    wc.setLoaded();
                    player.useAmmo(toIntArray(wc.getCost()));
                }else{
                    viewPrintError("You have not enough ammo to load this weapon");
                }
                player.notifyEndAction();
                break;
            default: 
                break;
        }
    }

    /**
     * This function controls the MOVE action and verifies if a player can move to a cell
     * @param cell of destination
     * @return if the player can move, else false
     */
    public boolean move(Cell cell, EnumCommand enumCommand){
        if(player.getCell() == cell) return true;

        EnumActionParam actionParam;
        if(!boardController.isFinalFrenzy()) {
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
                default:
                    return false;
            }
            if(billboard.canMove(player.getCell(), cell, actionParam.getNum())){
                setCell(cell);
                return true;
            }
        }
        else {
            //TODO i controlli per le due grab e le due shoot sono fatti in anticipo?
            // Serve davvero distinguere se è Final frenzy o meno qui?
            switch (enumCommand) {
                case MOVE_FRENZY: //normal mode
                    actionParam = FRENZY_MOVE;
                    break;
                case GRAB_MOVE_FRENZYX1:// move for grab
                    actionParam = FRENZY_GRAB_MOVEX1;
                break;
                case GRAB_MOVE_FRENZYX2:// move for grab
                    actionParam = FRENZY_GRAB_MOVEX2;
                break;
                case SHOOT_MOVE_FRENZYX1: //move from shoot
                    actionParam = FRENZY_SHOOT_MOVEX1;
                    break;
                case SHOOT_MOVE_FRENZYX2: //move from shoot
                    actionParam = FRENZY_SHOOT_MOVEX2;
                    break;
                default:
                    return false;
            }
            if(billboard.canMove(player.getCell(), cell, actionParam.getNum())){
                setCell(cell);
                return true;
            }

        }
        return false;
    }

    private boolean movePlayer(@NotNull Player player, Cell cell, int i){
        return (!billboard.canMove(player.getPawn().getCell(), cell, i));
     }

    /**
     * This function controls GRAB action in a NormalCells (Grab Ammocard)
     * @return success of operation
     */
    private boolean grabAmmo(){
        AmmoCard ammoCard = (AmmoCard) boardController.getBoard().giveCardFromCell(player.getCell(), player, 0);
        if(ammoCard!=null) {
            if (ammoCard.verifyPowerUp())
                boardController.getBoard().giveCardFromPowerUpDeck(player);
            boardController.getBoard().addAmmoDiscardDeck(ammoCard);
            modifyCell.add(player.getCell());
            return true;
        }
        return false;
    }

    /**
     * This function controls GRAB action in a RegenerationCells (Grab Weapon)
     * @param grabWeaponIndex index of weapon to grab
     */
    private void checkCanGrabWeapon(int grabWeaponIndex){
        WeaponCard grabWeapon = (WeaponCard) (player.getCell().getCard(grabWeaponIndex));

        int[] grabCost = toIntArray(grabWeapon.getGrabCost());
        //Can player pay this weaponCard?
        if(player.canPay(grabCost)) {
            //Can player draw another WeaponCard?
            if (player.getWeapons().size() == Constants.MAX_WEAPON_HAND_SIZE.getValue()) {
                //He can't
                cmdForView(new CommandObj(DISCARD_WEAPON, grabWeaponIndex));
            }else {
                grabWeapon(grabWeaponIndex, null);
            }
        }else {
            viewPrintError("You have no bullets to grab this weapon!");
        }

    }

    private void discardWeapon(int discardIndex, int grabIndex) {
        if (discardIndex == -1) return;
        Card discardWeapon = player.rmWeapon(discardIndex);
        grabWeapon(grabIndex, (WeaponCard) discardWeapon);
    }

    private void grabWeapon(int weaponIndex, WeaponCard discardWeapon) {
        //Draw weaponCard
        WeaponCard weaponCard = (WeaponCard) boardController.getBoard().giveCardFromCell(player.getCell(), player, weaponIndex);
        if (weaponCard != null) {
            //Set weapon load
            weaponCard.setLoaded();
            //Add cell to modify cell
            modifyCell.add(player.getCell());
            if (discardWeapon != null) {
                boardController.getBoard().addCardInCell(discardWeapon, player.getCell());
            }
            numAction++;
            player.useAmmo(toIntArray(weaponCard.getGrabCost()));
        } else {
            viewPrintError("You can't draw another WeaponCard");

        }
    }

    /**
     * This function checks if player can SHOOT
     * @param cmdObj data to use
     */
    private void checkedShoot(CommandObj cmdObj) {
        WeaponCard weaponCard = (WeaponCard) cmdObj.getObject();
        //Checked load weapon
        if (!weaponCard.isReady()) {
            viewPrintError("This weapon is not loaded");
            return;
        }
        //Checked good attack selector
        int selector = cmdObj.getWeaponSelector();
        if (!isGoodSelector(selector, weaponCard)) {
            viewPrintError("Selected attack is not usable");
            return;
        }

        if (selector == -1) return;
//aggiungere caso delle priority weapons per cui un optional va bene anche prima di un attacco base
        boolean isGoodAttack = false;
        if (selector==0){
            //BASE ATTACK
            if(shootBaseAttack(weaponCard)) isGoodAttack = true;
        }else if (!weaponCard.getAttacks().isEmpty()) {
            //BASE ATTACK + OPTIONAL ATTACK
            if(shootOptionalAttack(weaponCard)) isGoodAttack = true;
//            askWhichOptionalAttack(weaponCard);
        }else if(weaponCard.getAlternativeAttack()!=null){
            //ALTERNATIVE ATTACK
            if(shootAlternativeAttack(weaponCard)) isGoodAttack = true;
        }

        if(isGoodAttack) {
           // receiveCmd(new CommandObj(ASKFORPOWERUP, GUNSIGHT));
            /*chiederà ad altri giocatori che possono se usare la venomgranade
                for()
            boardController.getPlayerController().receiveCmd((new CommandObj(ASKFORPOWERUP, VENOMGRENADE)));
             */

            numAction++;
            player.notifyEndAction();
        }else {
            viewPrintError("Failed attack");
        }
    }

    /**
     * This ask player which optional attack want to use, if 0 -> use only BaseAttack.
     * Than verify if can pay optional attack, ask which opponents want shoot and shoot
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
        weaponCard.shoot(0,player, opponents, null);
        //Attack whit optional attack
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
     * This function asks a player which opponents he wants to hit, then verifies if they can be hit and finally shoots
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
        return weaponCard.shoot(attackSelector, player, opponents, null);
    }

    /**
     * Implements controller to use FURNACE attacks
     * @param baseAttack if use base attack
     * @return List of players to shoot
     */
    private List<Player> furnaceImplementation(boolean baseAttack){
        List<Player> opponents = new ArrayList<>();
        if(baseAttack){
            //TODO choose room
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
     * This verify that attack selector is good,
     * Only BaseAttack -> 0-1
     * BaseAttack + OptionalAttack -> 0-num_of_optional_attack+1
     * AlternativeAttack -> 0-2
     * @param selector
     * @param wc
     * @return
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
    private void setOtherCell(@NotNull Player player, Cell cell){
        player.getCell().removePawn(player.getPawn());
        player.getPawn().setCell(cell);
        cell.addPawn(player.getPawn());
    }

    /**
     * This function modifies the position of the pawn
     * @param cell of destination
     */
    public void setCell(Cell cell){
        this.boardController.getBoard().setPlayerCell(this.player,cell);
    }

    /**
     * This function verifies which power up the Power Card has, then creates a new Commandobj
     * with the instruction for next action
     * @param power to verify Power up
     * @return a CommandObj
     */
    private CommandObj verifyPowerUp(PowerCard power) {
        switch(power.getPowerUp()) {
            case KINETICRAY:
                return new CommandObj(USE_KINETICRAY);
            case GUNSIGHT:
                return new CommandObj(USE_GUNSIGHT);
            case VENOMGRENADE:
                return new CommandObj(USE_VENOMGRENADE);
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
    private ArrayList<PowerCard> getPotentialPowerUps(CommandObj obj){
        ArrayList<PowerCard> powers = new ArrayList<>();
        for(PowerCard power: player.getPowerups()){
            if(obj.getCmd() == USE_VENOMGRENADE && power.getPowerUp() == VENOMGRENADE)
                powers.add(power);
            else {
                if(obj.getCmd() == SHOOT && power.getPowerUp() == GUNSIGHT) {
                    if (power.getPowerUp() == GUNSIGHT && (player.canPay(new int[]{1,0,0}) || player.canPay(new int[]{0, 1, 0}) || player.canPay(new int[]{0, 1, 0})))
                        powers.add(power);
                }
                else if(power.getPowerUp() == PowerUp.TELEPORTER || power.getPowerUp() == PowerUp.KINETICRAY)
                    powers.add(power);

            }
        }
        return powers;
    }



    public List<Cell> getModifyCell() {
        return modifyCell;
    }

    protected void myTurn(){
        modifyCell = new ArrayList<>();

        cmdForView(new CommandObj(SHOW_BOARD));

        if(player.getCell()==null) cmdForView(new CommandObj(REG_CELL));

        while(numAction< ACTION_PER_TURN_NORMAL_MODE.getValue()) {
            cmdForView(new CommandObj(YOUR_TURN));
        }
        cmdForView(new CommandObj(LOAD_WEAPONCARD));
        numAction = 0;
    }

    private void viewPrintError(){
        viewPrintError("");
    }


    public void viewPrintError(String mex){
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
        cmdForView(new CommandObj(NOT_YOUR_TURN, name));
    }
}