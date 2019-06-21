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
import view.PlayerBoardView;
import view.PlayerView;
import weapon.AreaWeapon;
import weapon.EnumWeapon;
import weapon.WeaponCard;

import java.util.*;
import java.util.stream.Collectors;

import static constants.Constants.ACTION_PER_TURN_NORMAL_MODE;
import static constants.EnumActionParam.*;
import static controller.PlayerCommand.*;
import static deck.Bullet.toIntArray;
import static powerup.PowerUp.*;
import static powerup.PowerUp.VENOMGRENADE;

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
        this.playerView = new PlayerView(player, this);
        this.playerBoardView = new PlayerBoardView(player);
        this.player.addObserver(playerView);
        this.player.addObserver(playerBoardView);

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

    public void receiveCmd(Object obj){
        CommandObj cmdObj = (CommandObj) obj;
        switch (cmdObj.getCmd()) {
            case MOVE:
            case GRAB_MOVE:
                if(move(billboard.getCellFromPosition((Position) (cmdObj.getObject())), cmdObj.getCmd())){
                    if(cmdObj.getCmd()==MOVE) numAction++;
                    //else ((PlayerView) view).grab();
                    else cmdForView(new CommandObj(GRAB));
                }else {
                    viewPrintError();
                }
                break;
            case GRAB_AMMO:
                 if(grabAmmo((NormalCell) cmdObj.getCell())){
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
                //powerups
            case ASKFORPOWERUP:
                ArrayList<PowerCard> cards = (getPotentialPowerUps(cmdObj));
                if(cards.isEmpty())
                    notifyObservers();
                else if(!playerView.askForPowerUp(cards))
                    notifyObservers();
                else playerView.usePowerUp(cards);
                break;
            case CHECKPOWERUP:
                playerView.usePowerUp(getPotentialPowerUps(cmdObj));
                break;
            case PAYGUNSIGHT:
                playerView.askPayGunsight(payCubeGunsight(), (PowerCard)cmdObj.getObject());
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
                notifyObservers();
                break;
            case TELEPORTER:
                player.setPawnCell((Cell)cmdObj.getObject());
                break;
            case USE_KINETICRAY:
                playerView.moveKineticray(playerView.chooseTarget(boardController.getListOfPlayers()), boardController.getCellsKineticRay(player.getCell()));
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
                break;
            case USE_GUNSIGHT:
                ((Player)cmdObj.getObject()).addDamage(player);
                notifyObservers();
                break;
            case END_TURN:
                numAction+= ACTION_PER_TURN_NORMAL_MODE.getValue();
                break;
            case REG_CELL:
                PowerCard pc = (PowerCard) cmdObj.getObject();
                if(boardController.setRegenerationCell(player, pc.getColor())){
                    player.usePowerUp(pc, true);
                    boardController.getBoard().addPowerUpDiscardDeck(pc);
                }
                break;
            case DISCARD_WEAPON:
                player.rmWeapon((int) cmdObj.getObject());
                player.notifyEndAction();
                break;
            case SHOOT:
                checkedShoot(cmdObj);
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
    public boolean move(Cell cell, PlayerCommand playerCommand){
        if(player.getCell() == cell) return true;

        EnumActionParam actionParam;
        if(!boardController.isFinalFrenzy()) {
            //NOT FINAL FRENZY
            switch (playerCommand) {
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
        return false;
    }

    /**
     *
     * @param cell
     * @param i
     * @return
     * @deprecated
     */
    @Deprecated
    public boolean move(Cell cell, int i) {
        switch(i) {
            case 10: //normal move
                if(cell == player.getCell())
                    return true;
                if (!this.boardController.isFinalFrenzy()) {//non final Frenzy
                    if (!billboard.canMove(player.getPawn().getCell(), cell, 3))
                        return false; }
                else if (!billboard.canMove(player.getPawn().getCell(), cell, 4) || !boardController.verifyTwoTurnsFrenzy())
                    return false;
                setCell(cell);
                return true;

            case 11: //move from grab
                if(cell == player.getCell())
                    return true;
                if (!this.boardController.isFinalFrenzy()) { //not FinalFrenzy
                    if (player.getPlayerBoard().getNumDamages() > 2) {
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
                            return false; }
                    else if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                        return false; }
                else {//Final Frenzy
                    if (boardController.verifyTwoTurnsFrenzy()) {
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
                            return false; }
                    else if (!billboard.canMove(player.getPawn().getCell(), cell, 3))
                        return false;
                }
                setCell(cell);
                return true;

            case 12: //move from shoot
                if(cell == player.getCell())
                    return true;
                if (!boardController.isFinalFrenzy()) { //not FinalFrenzy
                    if (player.getPlayerBoard().getNumDamages() > 5){
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                            return false;}
                    else return false;
                    }
                else {//Final Frenzy
                        if (boardController.verifyTwoTurnsFrenzy()) {
                            if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                                return false; }
                        else if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
                            return false;
                    }

                    setCell(cell);
                    return true;

        }
        return false;
    }

    private boolean movePlayer(@NotNull Player player, Cell cell, int i){
        return (!billboard.canMove(player.getPawn().getCell(), cell, i));
     }

    /**
     * This function controls GRAB action in a NormalCells (Grab Ammocard)
     * @param cell Destination cell
     * @return success of operation
     */
    private boolean grabAmmo(NormalCell cell){
        AmmoCard ammoCard = (AmmoCard) boardController.getBoard().giveCardFromCell(cell, player, 0);
        if(ammoCard!=null) {
            if (ammoCard.verifyPowerUp())
                boardController.getBoard().giveCardFromPowerUpDeck(player);
            boardController.getBoard().addAmmoDiscardDeck(ammoCard);
            modifyCell.add(cell);
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
            //Can player draw an other WeaponCard?
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

        boolean isGoodAttack = false;
        if (selector==0){
            //BASE ATTACK
        //    if(shootBaseAttack(weaponCard)) isGoodAttack = true;
        }else if (!weaponCard.getAttacks().isEmpty()) {
            //BASE ATTACK + OPTIONAL ATTACK
            //if(shootOptionalAttack(weaponCard)) isGoodAttack = true;
            askWhichOptionalAttack(weaponCard);
        }else if(weaponCard.getAlternativeAttack()!=null){
            //ALTERNATIVE ATTACK
      //      if(shootAlternativeAttack(weaponCard)) isGoodAttack = true;
        }

        if(isGoodAttack) {
            numAction++;
            player.notifyEndAction();
        }else {
            viewPrintError("Failed attack");
        }
    }

    /**
     * This function asks a shooter which optional attack wants to use and targets he wants to hit.
     * Then he shoots using base attack and selected optional attack
     * @param weaponCard weaponCard to use
     * @return true if min one opponent was hit
     */
    private void askWhichOptionalAttack(WeaponCard weaponCard) {
        //List<Integer> indexes = playerView.chooseOptionalAttack(weaponCard, true);
        cmdForView(new CommandObj(CHOOSE_OPTIONAL_ATTACK, new ArrayList<>(Arrays.asList(weaponCard, true))));
    }

    private void setOptionalAttacks(WeaponCard weaponCard, List<Integer> indexes){
        if (indexes.isEmpty()) {
 //           return shootBaseAttack(weaponCard);
        }

        int maxTarget;
        List<Attack> attacks = new ArrayList<>();

        attacks.add(weaponCard.getBaseAttack());
        indexes.forEach(x -> attacks.add(weaponCard.getAttack(x)));

        maxTarget = attacks.stream().mapToInt(Attack::getTarget).max().orElse(0);
        List<Player> opponents = getTargets(weaponCard.getBaseAttack().getTargetType(), maxTarget);
        //User can't attack or decided to not attack
        if (opponents.isEmpty()){
            viewPrintError("Failed attack");
            return;
        }
        //Add null opponents to have opponents.size() == attack.target
        stdPlayerList(opponents, maxTarget);

        weaponCard.shoot(0,player, opponents, null);

        for(Integer index : indexes) {
            if (!player.canPay(weaponCard.getAttack(index).getCost())) {
                viewPrintError("You have not enough bullet to use this attack");
            }else{
                int i = index +1;
                weaponCard.shoot(i, player, opponents, null);
                player.useAmmo(weaponCard.getAttack(index).getCost());
            }
        }
    }

    /**
     * This function invokes shootSingleAttack to use BaseAttack
     * @param weaponCard WeaponCard to use to attack
     * @param pw shooter playerView
     * @return a base single attack
     */
    private boolean shootBaseAttack(WeaponCard weaponCard, PlayerView pw){
        return shootSingleAttack(weaponCard,pw, true);
    }

    /**
     * This function invokes shootSingleAttack to use AlternativeAttack
     * @param weaponCard WeaponCard  to use to attack
     * @param pw shooter playerView
     * @return an optional single attack
     */
    private boolean shootAlternativeAttack(WeaponCard weaponCard, PlayerView pw) {
        if (!player.canPay(weaponCard.getAlternativeAttack().getCost())) {
            pw.printError("You have not enough bullet to use this attack");
            return false;
        }

        if (shootSingleAttack(weaponCard, pw, false)) {
            player.useAmmo(weaponCard.getAlternativeAttack().getCost());
            return true;
        } else return false;
    }

    /**
     * This function asks a player which opponents he wants to hit, then verifies if they can be hit and finally shoots
     * @param weaponCard Waeponcard with the attack
     * @param pw Shooter PlayerView
     * @param baseAttack if true uses baseAttack, else uses alternativeAttack
     * @return if shooter is shoot
     */
    private boolean shootSingleAttack(WeaponCard weaponCard, PlayerView pw,  boolean baseAttack){
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
            if(baseAttack){
                //TODO choose room
                opponents.addAll(boardController.getPotentialTargets(opponents.get(0).getCell(), EnumTargetSet.SAME_ROOM));
            }else {
                List<Cell> possibleCells = boardController.getPotentialDestinationCells(player.getCell(), 1);
                Cell c = pw.chooseCellToAttack(billboard, possibleCells);
                if(possibleCells.contains(c)){
                    opponents.addAll(c.getPawns().stream().map(Pawn::getPlayer).collect(Collectors.toList()));
                }else pw.printError("Selected cell cannot be selected");
            }
        } else {
            opponents.addAll(getTargets(attack.getTargetType(), maxTarget));
        }


        if (opponents.isEmpty()) return false;
        //Add null opponents to have opponents.size() == attack.target
        stdPlayerList(opponents, maxTarget);
        //FINALLY SHOOT!!
        return weaponCard.shoot(attackSelector, player, opponents, null);

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
        List<Player> choosenTargets = chooseTargets(potentialTarget, maxTarget);
        if (!hitOpponentSameCell) {
            return choosenTargets;
        }else {
            choosenTargets.addAll(boardController.getPotentialTargets(choosenTargets.get(0).getCell(), EnumTargetSet.SAME_CELL));
            return choosenTargets;
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
            List<Player> opponents = playerView.chooseTargets(maxTarget, potentialTargets);

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
                else powers.add(power);

            }
        }
        return powers;
    }



    public List<Cell> getModifyCell() {
        return modifyCell;
    }

    protected void myTurn(){
        modifyCell = new ArrayList<>();

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
    
    private void cmdForView(CommandObj cmd){
        setChanged();
        notifyObservers(cmd);
    }

    /**
     * This function creates an array which is used to verify if a player can pay
     * Gunsight power up
     * @return an array
     */
    private int[] payCubeGunsight(){
        int[] array = {0, 0, 0};
        if(player.getBullets().get(Color.RED) > 0)
            array[0] = 1;
        if(player.getBullets().get(Color.YELLOW) > 0)
            array[1] = 1;
        if(player.getBullets().get(Color.BLUE) > 0)
            array[2] = 1;
        return array;
    }

    private void setPlayerboardFrenzy(){
        if(boardController.isFinalFrenzy())
            player.getPlayerBoard().setFrenzy(true);
    }
}