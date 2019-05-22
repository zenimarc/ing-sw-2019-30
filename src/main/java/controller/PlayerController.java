package controller;
import board.Billboard;
import board.Cell;
import board.Position;
import constants.Color;
import constants.EnumActionParam;
import powerup.PowerCard;
import player.Player;
import view.PlayerBoardView;
import view.PlayerView;

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
    private PlayerBoardView playerBoardView;
    private Player player;
    private int numAction = 0;


    /**
     * Default constructor
     */
    public PlayerController(Player player) {
        this.player = player;
        this.playerView = new PlayerView(player, this);
        this.playerBoardView = new PlayerBoardView(player);

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
                if(move(billboard.getCellFromPosition((Position) (cmdObj.getObject())), MOVE)){
                    numAction++;
                }else{
                    if(view.getClass() == PlayerView.class) {
                        ((PlayerView) view).printError();
                    }
                }
                break;
            case GRAB_AMMO:
            case GRAB_WEAPON:
                grab(cmdObj.getCell(), cmdObj.getWeaponSelector());
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
                boardController.setRegenerationCell(player, cmdObj.getCellColor());
                break;
            case GET_DESTINATION_CELL:

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
                case GRAB_WEAPON:// move from grab
                case GRAB_AMMO:
                    actionParam = player.getNumDamages()<ADRENALINIC_FIRST_STEP.getNum() ? NORMAL_GRAB_MOVE : ADRENALINIC_GRAB_MOVE;
                    break;
                case SHOOT: //move from shoot
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

     public boolean movePlayer(Player player, Cell cell, int i){
        return (!billboard.canMove(player.getPawn().getCell(), cell, i));
     }

    /**
     * This function controls the GRAB action
     * @param cell of destination
     * @return true if the action was successful, else false
     */
    public boolean grab(Cell cell, int val) {
        cell.giveCard(this.player, val); //TODO se non si può prendere la carta lancia errore, stessa cosa per il movimento
            return true;
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

    public void setOtherCell(Player player, Cell cell){
        player.getCell().removePawn(player.getPawn());
        player.getPawn().setCell(cell);
        cell.addPawn(player.getPawn());
    }

    /**
     * This function modifies the position of the pawn
     * @param cell of destination
     */

    public void setCell(Cell cell){
        this.player.setPawnCell(cell);
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

    public void useGranade(Player player){
        player.addMark(this.player);
    }

    public void useGunsight(Player player){
        player.addDamage(this.player);
    }

    public void useTeleporter(Cell cell){
        setCell(cell);
    }

    public void myTurn(){
        while(numAction< ACTION_PER_TURN_NORMAL_MODE.getValue()) {
            playerView.myTurn();
        }
        numAction = 0;
    }

}