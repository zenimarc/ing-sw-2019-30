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
    private List<Player> listOfPlayers;
    private Board board;

    /**
     * Default constructor
     */

    public BoardController() {
        this.listOfPlayers = new ArrayList<>();
    }

    public BoardController(ArrayList<Player> players) {
        this.listOfPlayers = players;
    }

    /**
     * This function returns the player who has to do a move
     * @return the player
     */
    public Player getPlayer(){return this.listOfPlayers.get(playerTurn);}

    /**
     * This function changes the number to decide which turn is
     */
    public void changeTurn() {
        if(playerTurn == listOfPlayers.size()-1)
            playerTurn = 1;
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

}