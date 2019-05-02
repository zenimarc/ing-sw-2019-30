package controller;
import board.Board;
import board.Cell;
import deck.Deck;
import player.Player;

import java.util.*;
import java.util.stream.Collectors;

import static controller.EnumTargetSet.VISIBLE;

/**
 * BoardController controls the table game.
 */
public class BoardController {

    private int playerTurn = 0;
    private int verifyFinalFrenzyTurns = 0;
    private List<Player> listOfPlayers;
    private List<PlayerController> playerControllers;
    private Board board;

    /**
     * Default constructor
     */

    public BoardController() {
        this.listOfPlayers = new ArrayList<>();
        this.playerControllers = new ArrayList<>();
    }

    public BoardController(List<Player> players, Board board) {
        this.listOfPlayers = players;
        this.board = board;
        this.playerControllers = new ArrayList<>();
        for (Player player : this.listOfPlayers){
            playerControllers.add(new PlayerController(player));
        }

    }

    /**
     * This function returns the player who has to do a move
     * @return the player
     */
    public Player getPlayer(){return this.listOfPlayers.get(playerTurn);}

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
}