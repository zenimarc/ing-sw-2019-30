package controller;
import board.BillboardGenerator;
import board.Board;
import board.Cell;
import board.RegenerationCell;
import deck.Deck;
import player.Player;
import view.BoardViewCLI;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BoardController controls the table game.
 */
public class BoardController {

    private int playerTurn = 0;
    private int verifyFinalFrenzyTurns = 0;
    private List<Player> listOfPlayers;
    private List<PlayerController> playerControllers;
    private Board board;

    private BoardViewCLI boardViewCLI;

    /**
     * Default constructor
     */

    public BoardController() {
        this.listOfPlayers = new ArrayList<>();
        this.playerControllers = new ArrayList<>();
    }

    public BoardController(List<Player> players, Board board) {
        this(players, 8);
        this.board = board;
        this.boardViewCLI = new BoardViewCLI(board);
    }

    public BoardController(List<Player> players, Board board, List<PlayerController> controllers) {
        this(players, 8);
        this.board = board;
        this.boardViewCLI = new BoardViewCLI(board);
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
        //Create player controller for each player
        for (Player player : this.listOfPlayers){
            playerControllers.add(new PlayerController(player));
        }
        //TODO la Billboard da utilizzare dev'essere scelta tra le 3 possibili e memorizzate in json
        board = new Board(numskulls, BillboardGenerator.generateBillboard());

        boardViewCLI = new BoardViewCLI(board);
    }

    //TODO da sistemare, deve diventare un return String
    public void getBoardViewToString(){
        boardViewCLI.drawCLI();
    }

    /**
     * This function returns the player who has to do a move
     * @return the player
     */
    public Player getPlayer(){return this.listOfPlayers.get(playerTurn);}

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
    public void changeTurn() {
        if(playerTurn == listOfPlayers.size()-1)
            playerTurn = 0;
        else playerTurn++;
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
        return this.playerControllers.stream().filter(x -> x.getPlayer() == player).findFirst().orElse(null);
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
        switch (targetType) {
            case VISIBLE:
                return listOfPlayers.stream().filter(x -> board.getBillboard().visibleCells(shooterCell).contains(x.getCell())).collect(Collectors.toCollection(ArrayList::new));
            case NOT_VISIBLE:
                return listOfPlayers.stream().filter(x -> !(board.getBillboard().visibleCells(shooterCell).contains(x.getCell()))).collect(Collectors.toCollection(ArrayList::new));
            case VISIBLE_ROOM:
                return listOfPlayers.stream().filter(x -> board.getBillboard().canSeeThroughDoor(shooterCell, x.getCell())).collect(Collectors.toCollection(ArrayList::new));
            case SAME_ROOM:
                return listOfPlayers.stream().filter(x -> board.getBillboard().hasSameColor(shooterCell, x.getCell())).collect(Collectors.toCollection(ArrayList::new));
            case SAME_CELL:
                return listOfPlayers.stream().filter(x -> x.getCell().equals(shooterCell)).collect(Collectors.toCollection(ArrayList::new));
            case CARDINAL_WALL_BYPASS:
                return listOfPlayers.stream().filter(x -> (board.getBillboard().getCellPosition(x.getCell()).getX()) == board.getBillboard().getCellPosition(shooterCell).getX() ||
                        board.getBillboard().getCellPosition(x.getCell()).getY() == board.getBillboard().getCellPosition(shooterCell).getY()).collect(Collectors.toCollection(ArrayList::new));
            case CARDINAL:
                /*
                it's the same of Cardinal_Wall_Bypass but it has to check if there are walls that obstacle the line.
                so do the same checks as upper case, and if there aren't walls between shooter_cell and target_cell,
                need to be true this sentence: the shooter could potentially move to the target_cell
                in a number of steps that are the distance between the two cells.
                please note that if the two cells are on the same axe (has to be true due the first check) the distance between
                the two cell is the minimum possible steps to reach the target cell.
                 */
                return listOfPlayers.stream().filter(x -> ((board.getBillboard().getCellPosition(x.getCell()).getX()) == board.getBillboard().getCellPosition(shooterCell).getX() ||
                        board.getBillboard().getCellPosition(x.getCell()).getY() == board.getBillboard().getCellPosition(shooterCell).getY())
                        && board.getBillboard().canMove(shooterCell, x.getCell(), board.getBillboard().cellDistance(shooterCell, x.getCell()))).collect(Collectors.toCollection(ArrayList::new));
        }
        return Collections.emptyList();
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

    public boolean setRegenerationCell(Player player, int powerUpIndex){
        RegenerationCell regenerationCell = board.getBillboard().getRegenerationCell()
                .stream()
                .filter(x -> x.getColor() == player.getPowerups().get(powerUpIndex).getColor())
                .findFirst()
                .orElse(null);

        if(regenerationCell!=null){
            player.setCell(regenerationCell);
            return true;
        }
        return false;
    }
}