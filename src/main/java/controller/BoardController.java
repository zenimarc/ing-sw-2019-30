package controller;
import board.Board;
import deck.Deck;
import player.Player;

import java.util.*;

/**
 * BoardController controls turns during the game
 */
public class BoardController {

    private int playerTurn = 0;
    private int verifyFinalFrenzyTurns = 0;
    private List<Player> listOfPlayers;
    private Board board;

    /**
     * Default constructor
     */

    public BoardController() {
        this.listOfPlayers = new ArrayList<>();
    }

    public BoardController(ArrayList<Player> players, Board board) {
        this.listOfPlayers = players;
        this.board = board;
    }

    /**
     * This function returns the player who has to do a move
     * @return the player
     */
    public Player getPlayer(){return this.listOfPlayers.get(playerTurn);}

    /**
     * This function returns a board //TODO specificarla meglio
     * @return a board
     */

    public Board getBoard(){return this.board;}

    /**
     * This function returns the number of the actual turn
     * @return the number of the turn
     */

    public int getNumTurns() { return this.playerTurn;
    }

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
     * This function sets the board of a BoardController
     * @param board to assign
     */
    public void setBoard(Board board) {this.board = board; }
}