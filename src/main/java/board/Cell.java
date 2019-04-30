package board;

import deck.Card;
import deck.Color;
import player.Pawn;
import player.Player;

import java.util.*;

/**
 * Cell is an abstraction of the areas which create a board
 */
public abstract class  Cell {

    protected Color color;
    protected ArrayList<Pawn> pawns;

    /**
     * Constructors
     */

    public Cell(Color color) {
        pawns = new ArrayList<>();
        this.color = color;
    }

    /**
     * This function returns the color of the cell
     * @return the color of the cell
     */
    public Color getColor(){return color;}

    /**
     * This function does nothing because it is implemented by sons
     * @param p variable
     * @return nothing
     */
    public Card getCard(int p){
        return null;
    }

    /**
     * This function does nothing because it is implemented by sons
     * @param card variable
     * @return nothing
     */

    public Card removeCard(Card card){
        return null;
    }

    /**
     * This function does nothing because it is implemented by sons
     * @param card variable
     * @return false
     */

    public boolean setCard(Card card) {
        return  false;
    }

    /**
     * This function does nothing because it is implemented by sons
     * @param player to receive a card
     * @param i card to pick
     */
    public void giveCard(Player player, int i){

    }

}