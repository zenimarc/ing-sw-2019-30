package board;

import deck.Card;
import deck.Color;
import player.Pawn;

import java.util.*;

/**
 * 
 */
public abstract class  Cell {

    protected Color color;
    protected ArrayList<Pawn> pawns;
    protected List<Cell> shootableCells;
    protected List<Cell> walkableCells;

    public Cell(Color color) {
        pawns = new ArrayList<Pawn>();
        shootableCells = new ArrayList<Cell>();
        walkableCells = new ArrayList<Cell>();
        this.color = color;
    }

    public Cell(){
        this(null);
    }


    public Card getCard(int p){
    // Implementation by "sons"
        return null;
    }

    public Card removeCard(Card card){
        // Implementation by "sons"
        return null;
    }

    public boolean setCard(Card card) {
        // Implementation by "sons"
        return  false;
    }

    public boolean isInShootable(Cell cell) {
        return shootableCells.contains(cell);
    }

    public boolean isInWalkable(Cell cell) {
        return walkableCells.contains(cell);
    }

}