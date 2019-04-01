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


    public Cell() {
    }


    public Card getCard(){
    // Implementation by "sons"
        return null;
    }

    public void setCard(Card card) {
        // Implementation by "sons"
    }

    public boolean isInShootable(Cell cell) {
        return shootableCells.contains(cell);
    }

    public boolean isInWalkable(Cell cell) {
        return walkableCells.contains(cell);
    }

}