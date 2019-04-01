package board;

import deck.Card;
import deck.Color;
import player.Pawn;

import java.util.*;

/**
 * 
 */
public class Cell {

    private Color color;
    private ArrayList<Pawn> pawns;
    private List<Cell> shootableCells;
    private List<Cell> walkableCells;


    /**
     * Default constructor
     */
    public Cell() {
    }



    /**
     * @return
     */
    public Card getCard() {
        // TODO implement here
        return null;
    }

    /**
     * @param card 
     * @return
     */
    public void setCard(Card card) {
        // TODO implement here

    }

    /**
     * @param cell 
     * @return
     */
    public boolean isInShootable(Cell cell) {
        // TODO implement here
        return false;
    }

    /**
     * @param cell Cell 
     * @return
     */
    public boolean isInWalkable(Cell cell) {
        // TODO implement here
        return false;
    }

}