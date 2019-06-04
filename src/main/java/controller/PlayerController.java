package controller;
import attack.Attack;
import board.*;
import board.Cell;
import board.billboard.Billboard;
import constants.Constants;
import constants.EnumActionParam;
import deck.AmmoCard;
import deck.Bullet;
import deck.Card;
import org.jetbrains.annotations.NotNull;
import player.PlayerBoard;
import powerup.PowerCard;
import player.Player;
import view.PlayerBoardView;
import view.PlayerView;
import weapon.WeaponCard;

import java.util.*;

import static constants.Constants.ACTION_PER_TURN_NORMAL_MODE;
import static constants.EnumActionParam.*;
import static controller.PlayerCommand.MOVE;

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
    private PlayerBoardView playerBoardView;

    /**
     * Default constructor
     */
    public PlayerController(Player player) {
        this.player = player;
        this.playerView = new PlayerView(player, this);
        this.playerBoardView = new PlayerBoardView(player);
        this.player.addObserver(playerView);

    }

    public PlayerController(Player player, @NotNull BoardController boardController) {
        this(player);
        this.boardController = boardController;
        this.billboard = boardController.getBoard().getBillboard();

    }

    public void setPlayerView(PlayerView playerView){
        this.playerView = playerView;
    }

    public void setPlayerBoardView(PlayerBoardView playerBoardView){
        this.playerBoardView = playerBoardView;
    }

    public PlayerView getPlayerView() {
        return playerView;
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
    public void update(Observable view, Object obj){
        CommandObj cmdObj = (CommandObj) obj;
        switch (cmdObj.getCmd()) {
            case MOVE:
            case GRAB_MOVE:
                if(move(billboard.getCellFromPosition((Position) (cmdObj.getObject())), cmdObj.getCmd())){
                    if(cmdObj.getCmd()==MOVE) numAction++;
                    else ((PlayerView) view).grab();
                }else {
                    viewPrintError(view);
                }
                break;
            case GRAB_AMMO:
                 if(grabAmmo((NormalCell) cmdObj.getCell())){
                    numAction++;
                }else {
                    viewPrintError(view);
                }
                break;
            case GRAB_WEAPON:
                grabWeapon(view, (int) cmdObj.getObject());
                break;
            case SHOOT:
                //TODO
                //Controllare di poter usare arma
                //Controlla obiettivi
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
     * This controls GRAB action for NormalCell (Grab Ammocard)
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
     * This controls GRAB action for RegenerationCEll (Grab Weapon)
     * @param view player view
     * @param weaponIndex index of weapon to grab
     */
    private void grabWeapon(Observable view, int weaponIndex){
        WeaponCard grabWeapon = (WeaponCard) (player.getCell().getCard(weaponIndex));
        Card discardWeapon = null;

        int grabCost[] = Bullet.toIntArray(grabWeapon.getGrabCost());
        //Can player pay this weaponCard?
        if(player.canPay(grabCost)) {
            //Can player draw an other WeaponCard?
            if (player.getWeapons().size() == Constants.MAX_WEAPON_HAND_SIZE.getValue()) {
                //He can't
                int discardIndex = ((PlayerView) view).chooseWeaponToDiscard();
                if (discardIndex == -1) return;
                discardWeapon = player.rmWeapon(discardIndex);
            }
            //Draw weaponCard
            Card card = boardController.getBoard().giveCardFromCell(player.getCell(), player, weaponIndex);
            if (card!=null) {
                //Set weapon load
                ((WeaponCard) card).setLoaded();
                //Add cell to modify cell
                modifyCell.add(player.getCell());
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
     * This function verifies if the target is hittable or not
     * @param attack the attack the player wants to use
     * @param obj a cell o a list of target to hit
     * @return true if the attack was successful, false otherwise
     */
    public boolean chooseTarget(Attack attack, Object obj) {
        return true;
    }

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

    public boolean verifyPowerUp(PowerCard power, Object object) {
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
            case VENOMGRENADE:
                useGranade((Player) object);
                return true;
            case TELEPORTER:
                useTeleporter((Cell) object);
                return true;
        }
        return false;
    }

    private boolean useKineticRay(Player player, Cell cell){
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

    private void useGranade(@NotNull Player player){
        player.addMark(this.player);
    }

    private void useGunsight(@NotNull Player player){
        player.addDamage(this.player);
    }

    private void useTeleporter(Cell cell){
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