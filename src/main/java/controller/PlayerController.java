package controller;
import board.*;
import board.Cell;
import board.billboard.Billboard;
import constants.Constants;
import constants.EnumActionParam;
import deck.AmmoCard;
import deck.Bullet;
import deck.Card;
import org.jetbrains.annotations.NotNull;
import powerup.PowerCard;
import player.Player;
import view.PlayerView;
import weapon.WeaponCard;

import java.util.*;

import static constants.Constants.ACTION_PER_TURN_NORMAL_MODE;
import static constants.EnumActionParam.*;
import static controller.PlayerCommand.MOVE;

//TODO finire la shoot e decidere come gestirla, fare overloading di alcune funzioni

/**
 * PlayerController is used to control if a player can do certain actions
 */
public class PlayerController implements Observer {
    private BoardController boardController;
    private Billboard billboard;
    private PlayerView playerView;
    private Player player;
    private int numAction = 0;
    private ArrayList<Cell> modifyCell;

    /**
     * Default constructor
     */
    public PlayerController(Player player) {
        this.player = player;
        this.playerView = new PlayerView(player, this);
        this.player.addObserver(playerView);

    }

    public PlayerController(Player player, BoardController boardController) {
        this(player);
        this.boardController = boardController;
        this.billboard = boardController.getBoard().getBillboard();

    }

    public void setBillboard(Billboard board){this.billboard = board;}

    public void setBoardController(BoardController board){this.boardController = board;}

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(Observable view, Object obj){
        CommandObj cmdObj = (CommandObj) obj;
        switch (cmdObj.getCmd()) {
            case MOVE:
            case GRAB_MOVE:
                if(move(billboard.getCellFromPosition((Position) (cmdObj.getObject())), cmdObj.getCmd())){
                    if(cmdObj.getCmd()==MOVE) numAction++;
                }else {
                    viewPrintError(view);
                }
                break;
            case GRAB_AMMO:
                if(grab(cmdObj.getCell(), cmdObj.getWeaponSelector())){
                    numAction++;
                }else {
                    viewPrintError(view);
                }
                break;
            case GRAB_WEAPON:
                grabWapon(view, cmdObj);
                break;

            case SHOOT:
                //TODO verificare come implementarlo per bene
                shoot(cmdObj.getCell(), cmdObj.getWeaponSelector());
                break;
            // case POWERUP:
            //verifyPowerUp(((CommandObj)obj).getCell(), ((CommandObj)obj).getWeaponSelector());
            //break;
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
            //NOT FINAL FREZY
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

    public boolean movePlayer(@NotNull Player player, Cell cell, int i){
        return (!billboard.canMove(player.getPawn().getCell(), cell, i));
     }

    /**
     * This function controls the GRAB action
     * @param cell of destination
     * @return true if the action was successful, else false
     */
    private boolean grab(@NotNull Cell cell, int val) {
        //If you want give a weaponCard but you have 3(or more) in your hand, return false
        if(cell.getClass() == RegenerationCell.class &&
                player.getWeapons().size()>=Constants.MAX_WEAPON_HAND_SIZE.getValue()){
                return false;
        }

        Card card = boardController.getBoard().giveCardFromCell(cell, player, val);
        modifyCell.add(cell);

        if(card.getClass() == AmmoCard.class){
            AmmoCard ac = (AmmoCard) card;
            if(ac.verifyPowerUp())
                boardController.getBoard().giveCardFromPowerUpDeck(player);
            boardController.getBoard().addAmmoDiscardDeck(ac);
        }

        return true;
    }

    private void grabWapon(Observable view, @NotNull CommandObj cmdObj){

        int weaponIndex = (int) cmdObj.getObject();
        WeaponCard grabWeapon = (WeaponCard) (player.getCell().getCard(weaponIndex));
        Card discardWeapon = null;

        //Player can play this weaponCard?
        int grabCost[] = Bullet.toIntArray(grabWeapon.getGrabCost());
        if(player.canPay(grabCost)) {
            //Player can draw an other WeaponCard?
            if (player.getWeapons().size() == Constants.MAX_WEAPON_HAND_SIZE.getValue()) {
                //He can't
                int discardIndex = ((PlayerView) view).chooseWeaponToDiscard();
                if (discardIndex == -1) return;
                discardWeapon = player.rmWeapon(discardIndex);
            }
            //Draw weaponCard
            if (grab(player.getCell(), weaponIndex)) {
                if (discardWeapon != null) {
                    boardController.getBoard().addCardInCell(discardWeapon, player.getCell());
                }
                numAction++;
                player.useAmmo(grabCost);
            } else {
                viewPrintError(view, "You can't draw an other WeaponCard");
            }
        }else {
            viewPrintError(view, "You have no bullets to grab this weapon!");
        }
    }

    /**
     * This function controls the SHOOT action
     * @param cell of destination of the movement
     * @return true if the attack was successful, false otherwise
     */
    public boolean shoot(Cell cell, int weaponNumber) {
        if(!this.move(cell, 12))
            return false; //lancia errore
        //verifyWeapon(weaponNumber);//viene verificato se l'arma è carica, altrimenti si cambia arma

        return true;
    }

    /**
     * This function verifies if the weapon can be used
     * @return the weapon desired
     */
    public boolean verifyWeapon(int weaponNumber){
        //if(player.getWeapons().get(weaponNumber).isReady());//TODO gestire il caso in cui l'arma non sia carica
        //chooseAttack(weaponNumber) viene chiesto l'attacco che si vuole usare e si verifica se sia usabile
        //
        return false;
    }

    /**
     * This function returns the attack the player wants to use
     * @return the attack wanted
     */

    public boolean chooseAttack(int weaponNumber, int attack){
        //if() se è optional non valido, da errore
        if(player.canPay(player.getWeapons().get(weaponNumber).getAttack(attack).getCost()))

            //chooseTarget(attack, oggetto da colpire); si fa un switch case
        {return false;}
        return false;
    }

    /**
     * This function verifies if the target is hittable or not
     * @param attack the attack the player wants to use
     * @param obj a cell o a list of target to hit
     * @return true if the attack was successful, false otherwise
     */
   /* public boolean chooseTarget(Attack attack, Object obj){

        }*/



    /**
     * This function modifies the position of the pawn of another player
     * @param player to be moved
     * @param cell of destination
     */

    public void setOtherCell(@NotNull Player player, Cell cell){
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

    public boolean verifyPowerUp(PowerCard power, Object object, int i) {
        if(!player.usePowerUp(power, false))
            return false; //chiederà al giocatore se vuole scartarla o meno

        switch(power.getPowerUp()) {
            case KINETICRAY:
                return(useKineticRay(player, (Cell)object)); //verrà chiesta la cella di destinazione nel caso
            case GUNSIGHT:
                if(player.canPayGunsight(power.getColor())) {
                    useGunsight((Player) object);
                    return true;
                }
                else return false;
            case VENOMGRANADE:
                useGranade((Player) object);
                return true;
            case TELEPORTER:
                useTeleporter((Cell) object);
                return true;
        }
        return false;
    }

    public boolean useKineticRay(Player player, Cell cell){
        if(!movePlayer(player, cell, 2))
            return false;
        if((billboard.getCellPosition(player.getCell()).getX() == billboard.getCellPosition(cell).getX())|| (billboard.getCellPosition(player.getCell()).getY() == billboard.getCellPosition(cell).getY())) {
            setOtherCell(player, cell);
            return true;
        }
        else return false;
    }

    public List<Cell> getModifyCell() {
        return modifyCell;
    }

    public void useGranade(@NotNull Player player){
        player.addMark(this.player);
    }

    public void useGunsight(@NotNull Player player){
        player.addDamage(this.player);
    }

    public void useTeleporter(Cell cell){
        setCell(cell);
    }

    public void myTurn(){
        modifyCell = new ArrayList<>();

        if(player.getCell()==null) playerView.regPawn();

        while(numAction< ACTION_PER_TURN_NORMAL_MODE.getValue()) {
            playerView.myTurn();
        }
        numAction = 0;
    }

    /**
     *
     * @param view
     * @deprecated use viewPrintErro(Observable, String)
     */
    private void viewPrintError(@NotNull Observable view){
        if(view.getClass() == PlayerView.class) {
            ((PlayerView) view).printError();
        }
    }

    private void viewPrintError(@NotNull Observable view, String mex){
        if(view.getClass() == PlayerView.class) {
            ((PlayerView) view).printError(mex);
        }
    }
}