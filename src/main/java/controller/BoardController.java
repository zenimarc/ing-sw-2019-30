package controller;
import board.*;
import board.Cell;
import board.NormalCell;
import board.RegenerationCell;
import board.billboard.BillboardGenerator;
import constants.Color;
import deck.Deck;
import player.Player;
import powerup.PowerCard;
import view.BoardViewCLI;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BoardController controls the table game.
 */
public class BoardController{

    private int playerTurn = 0;
    private int verifyFinalFrenzyTurns = 0;
    private List<Player> listOfPlayers;
    private List<PlayerController> playerControllers;
    private Board board;
    private Player playerWhoPlay;

    /**
     * Default constructor
     */

    public BoardController(List<Player> players, Board board) {
        this(players, 8);
        this.board = board;
    }

    public BoardController(List<Player> players, Board board, List<PlayerController> controllers) {
        this(players, 8);
        this.board = board;
        this.playerControllers = controllers;

    }

    /**
     * This genetare a boardcontroller with a default board
     * @param players
     * @param numskulls
     */
    public BoardController(List<Player> players, int numskulls){
        this.listOfPlayers = players;
        this.playerControllers = new ArrayList<>();

        //TODO la Billboard da utilizzare dev'essere scelta tra le 3 possibili e memorizzate in json
        this.board = new Board(numskulls, BillboardGenerator.generateBillboard());

        for (Player player : this.listOfPlayers){
            //Create player controller
            playerControllers.add(new PlayerController(player, this));
            //Draw 2 PowerCard
            player.addPowerCard((PowerCard) board.getPowerUpDeck().draw());
            player.addPowerCard((PowerCard) board.getPowerUpDeck().draw());
        }
    }

    /**
     * This function returns the player who has to do a move
     * @return the player
     */
    public Player getPlayer(){return this.listOfPlayers.get(playerTurn);}

    /**
     * this function returns a Player from a nickname
     * @param nickname of the player you want to get
     * @return the player associated to the nickname
     */
    public Player getPlayer(String nickname){
        return listOfPlayers.stream().filter(x -> x.getName().equals(nickname)).findFirst().orElse(null);
    }

    public int getFrenzyturns (){return this.verifyFinalFrenzyTurns;}

    /**
     * This function returns the board associated with this controller
     * @return the board associated with this controller
     */

    public Board getBoard(){return this.board;}

    /**
     * This function returns the number of the actual turn
     * @return the number of the turn
     */

    public int getNumTurns() { return this.playerTurn; }

    /**
     * This function changes the number to decide which turn is
     */
    public int changeTurn() {
        if(playerTurn >= listOfPlayers.size()-1)
            playerTurn = 0;
        else playerTurn++;

        return playerTurn;
    }

    /**
     * This function verifies if the final phase of the game can begin
     * @return true if the number of skulls is zero
     */
    public boolean isFinalFrenzy() {
        return (this.board.getSkulls() == 0);
    }

    /**
     * This function changes the value of verifyFinalFrenzyTurns to decide the number of actions of a player during Final Frenzy
     */
    public void setFinalFrenzyTurns(){
        if(this.isFinalFrenzy()){
            this.verifyFinalFrenzyTurns = this.playerTurn;
        }
    }

    /**
     * This function verifies if the player can have one or two turns during Final Frenzy
     * @return true if he can have two turns, false if one
     */
    public boolean verifyTwoTurnsFrenzy(){
        return(this.verifyFinalFrenzyTurns != 0 && this.playerTurn >= this.verifyFinalFrenzyTurns);
    }

    /**
     * This function verifies if the player can really do a move
     * @param player wanted to be verified
     * @return true if it's his turn, else false
     */
    public boolean verifyTurn(Player player) {
        return(player == listOfPlayers.get(playerTurn));
    }

    /** verify if the deck is empty, potential need to recreate with discarded cards
     * @param deck to verify the emptiness
     */
    public boolean verifyDeck(Deck deck) {
        return(deck.getSize() == 0);
    }

    /**
     * This function sets the board (game) of the BoardController
     * @param board to assign
     */
    public void setBoard(Board board) {this.board = board; }

    /**
     * This function return the PlayerController associated to a specific Player
     * @param player you want to know its PlayerController
     * @return player's PlayerController if present, else null.
     */
    public PlayerController getPlayerController(Player player){
        return this.playerControllers.stream().filter(x -> x.getPlayer().getName().equals(player.getName())).findFirst().orElse(null);
    }

    /**
     * this function return the PlayerController associated to a specific nickname of a Player
     * @param nickname of the player you want to know its PlayerController
     * @return player's PlayerController if present, else null.
     */
    public PlayerController getPlayerController(String nickname){
        return this.playerControllers.stream().filter(x -> x.getPlayer().getName().equals(nickname)).findFirst().orElse(null);
    }

    /**
     * This function returns the list of players associated to this board (this game)
     * @return the list of player associated to this board
     */
    public List<Player> getListOfPlayers(){
        return listOfPlayers;
    }

    /**
     * This function, given the shooter cell, returns a list of players who are possible targets compatible with the attack's target type.
     * @param shooterCell is the shooter's Cell
     * @param targetType is the attack's target type, examples: Visible, Same room... (see EnumTargetSet for complete enumeration)
     * @return a list of players the shooter che hit with the selected targetType
     */
    public List<Player> getPotentialTargets(Cell shooterCell, EnumTargetSet targetType) {

        //If there are not opponents return empty list
        if(this.listOfPlayers.size()==1) return Collections.emptyList();

        List<Player> opponents = new ArrayList<>(this.listOfPlayers);
        opponents.remove(playerWhoPlay);

        switch (targetType) {
            case VISIBLE:
                return opponents.stream().filter(x -> board.getBillboard().visibleCells(shooterCell).contains(x.getCell())).collect(Collectors.toList());
            case NOT_VISIBLE:
                return opponents.stream().filter(x -> !(board.getBillboard().visibleCells(shooterCell).contains(x.getCell()))).collect(Collectors.toList());
            case VISIBLE_ROOM:
                return opponents.stream().filter(x -> board.getBillboard().canSeeThroughDoor(shooterCell, x.getCell())).collect(Collectors.toList());
            case SAME_ROOM:
                return opponents.stream().filter(x -> board.getBillboard().hasSameColor(shooterCell, x.getCell())).collect(Collectors.toList());
            case SAME_CELL:
                return opponents.stream().filter(x -> x.getCell().equals(shooterCell)).collect(Collectors.toList());
            case CARDINAL_WALL_BYPASS:
                return opponents.stream().filter(x -> (board.getBillboard().getCellPosition(x.getCell()).getX()) == board.getBillboard().getCellPosition(shooterCell).getX() ||
                        board.getBillboard().getCellPosition(x.getCell()).getY() == board.getBillboard().getCellPosition(shooterCell).getY()).collect(Collectors.toList());
            case CARDINAL:
                /*
                it's the same of Cardinal_Wall_Bypass but it has to check if there are walls that obstacle the line.
                so do the same checks as upper case, and if there aren't walls between shooter_cell and target_cell,
                need to be true this sentence: the shooter could potentially move to the target_cell
                in a number of steps that are the distance between the two cells.
                please note that if the two cells are on the same axe (has to be true due the first check) the distance between
                the two cell is the minimum possible steps to reach the target cell.
                 */
                return opponents.stream().filter(x -> ((board.getBillboard().getCellPosition(x.getCell()).getX()) == board.getBillboard().getCellPosition(shooterCell).getX() ||
                        board.getBillboard().getCellPosition(x.getCell()).getY() == board.getBillboard().getCellPosition(shooterCell).getY())
                        && board.getBillboard().canMove(shooterCell, x.getCell(), board.getBillboard().cellDistance(shooterCell, x.getCell()))).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * This function, given the shooter cell, returns a list of players who are possible targets compatible with the attack's target type
     * who are in cell in range [minDistance;maxDistance]
     * @param shooterCell is the shooter's Cell
     * @param targetType is the attack's target type, examples: Visible, Same room... (see EnumTargetSet for complete enumeration)
     * @param minDistance min distance of opponents
     * @param maxDistance  max distance of opponents
     * @return a list of players the shooter che hit with the selected targetType
     */
    public List<Player> getPotentialTargets(Cell shooterCell, EnumTargetSet targetType, int minDistance, int maxDistance) {

        List<Player> opponents = getPotentialTargets(shooterCell, targetType);
        if(opponents.isEmpty()) return Collections.emptyList();
        if(minDistance==-1 && maxDistance!=-1) {
            return opponents.stream()
                    .filter(x -> board.getBillboard().cellDistance(shooterCell, x.getCell()) <= maxDistance)
                    .collect(Collectors.toList());
        }else if(minDistance!=-1 && maxDistance==-1){
            return opponents.stream()
                    .filter(x -> board.getBillboard().cellDistance(shooterCell, x.getCell()) >= minDistance)
                    .collect(Collectors.toList());
        }else {
            return opponents.stream()
                    .filter(x -> board.getBillboard().cellDistance(shooterCell, x.getCell()) >= minDistance &&
                            board.getBillboard().cellDistance(shooterCell, x.getCell()) <= maxDistance)
                    .collect(Collectors.toList());
        }
    }

        /**
         * This function returns a subset of Cell from billboard where the player can move in tot steps
         * @param shooterCell is the player's cell
         * @param steps are the maximum steps available
         * @return a list of Cell reachable from shooterCell in tot steps
         */
    public List<Cell> getPotentialDestinationCells(Cell shooterCell, int steps){
        return board.getBillboard().getCellMap().keySet().stream().filter(x -> board.getBillboard().canMove(shooterCell, x, steps)).collect(Collectors.toList());
    }

    public String getPotentialDestinationCellsString(Cell shooterCell, int steps){
        StringBuilder sb = new StringBuilder();

        sb.append("Potential destination cell: ");
        for(Cell c: getPotentialDestinationCells(shooterCell,steps)){
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * This function returns a subset of Positions of Cells from billboard where the player can put another player using
     * Kinetic ray power up
     * @param playerCell of other player
     * @return a list
     */
    protected List<Position> getCellsKineticRay(Cell playerCell){
        List<Cell> cells = board.getBillboard().getCellMap().keySet().stream().filter(x -> board.getBillboard().cellDistance(playerCell, x) <= 2).collect(Collectors.toList());
        ArrayList<Position> positions = new ArrayList<>();
        for(Cell cell: cells)
            positions.add(board.getBillboard().getCellPosition(cell));
        return positions;
        }

    /**
     * Set RegenerationCell of a player which pawn was removed from billboard
     * @param player Player out of game
     * @param color Color of Regeneration Cell
     * @return true if it is valid, else false
     */
    public boolean setRegenerationCell(Player player, Color color){
        Cell regenerationCell = board.getBillboard().getRegenerationCell()
                .stream()
                .filter(x -> x.getColor() == color)
                .findFirst()
                .orElse(null);

        if(regenerationCell!=null){
            board.setPlayerCell(player, regenerationCell);
            return true;
        }
        return false;
    }

    public void firstPlay(){
        playerPlay(listOfPlayers.get(0));
    }

    /**
     * Say to playerController of player to start his turn
     * @param player Player how can play
     */
    public void playerPlay(Player player){
        PlayerController pc = playerControllers.stream().filter(x-> x.getPlayer()==player).findFirst().orElse(null);
        if(pc!=null) {
            playerWhoPlay = pc.getPlayer();
            playerControllers.stream().filter(x-> x.getPlayer()!=player).forEach(x -> x.notMyTurn(player.getName()));

            pc.myTurn();
            restoreCell(pc.getModifyCell());
            if(listOfPlayers.stream().anyMatch(Player::isDead)){
                scoring(listOfPlayers.stream().filter(Player::isDead).collect(Collectors.toList()));
            }
        }
        playerPlay(listOfPlayers.get(changeTurn()));
    }

    /**
     * Restore NormalCell and/or RegenerationCell that player modify in his turn
     * @param cells modify cells
     */
    private void restoreCell(List<Cell> cells){
        for(Cell cell : cells){
            if(cell.getClass()== NormalCell.class){
                NormalCell nc = (NormalCell) cell;
                nc.setCard(board.getAmmoCardDeck().draw());
            }
            else if(cell.getClass() == RegenerationCell.class) {
                //If no weaponCard in deck return
                if (board.getWeaponCardDeck().getSize() == 0) return;
                //else
                RegenerationCell rc = (RegenerationCell) cell;
                rc.setCard(board.getWeaponCardDeck().draw());
            }

        }
    }

    private void scoring(List<Player> deadPlayers){
       for(Player player : deadPlayers){
           String points = givePoints(player);
           playerControllers.stream().filter(x->x.getPlayer().equals(playerWhoPlay)).findFirst().ifPresent(x -> x.viewPrintError(points));
           board.decrementSkull();
           if(board.getSkulls()>0) {
               player.getPlayerBoard().addSkull();
               player.resetDamage();
               player.getPlayerBoard().resurrect();
           }
       }
    }

    private String givePoints(Player deadPlayer){
        StringBuilder sb = new StringBuilder();
            Map<Player, Integer> points = deadPlayer.getPlayerBoard().getPoints(isFinalFrenzy());
            points.keySet().forEach(x-> {
                x.addPoints(points.get(x));
                sb.append(x);
                sb.append(": ");
                sb.append(points.get(x));
                sb.append('\n');
            });
            return sb.toString();

    }

}