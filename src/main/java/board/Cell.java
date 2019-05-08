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

    public ArrayList<Pawn> getPawns(){return this.pawns;}

    /**
     * This function does nothing because it is implemented by sons
     * @param p variable
     * @return nothing
     */
    public abstract Card getCard(int p);

    /**
     * This function does nothing because it is implemented by sons
     * @param card variable
     * @return nothing
     */

    public abstract Card removeCard(Card card);

    /**
     * This function does nothing because it is implemented by sons
     * @param card variable
     * @return false
     */

    public abstract boolean setCard(Card card);

    /**
     * This function does nothing because it is implemented by sons
     * @param player to receive a card
     * @param i card to pick
     */
    public abstract void giveCard(Player player, int i);

    /**
     * This function adds a new pawn in the cell
     * @param pawn to be added
     */
    public void addPawn (Pawn pawn){this.pawns.add(pawn);
    }

    /**
     * This function removes the presence of a pawn from the cell
     * @param pawn which has changed position
     */
    public void removePawn (Pawn pawn){this.pawns.remove(pawn);
    }
}