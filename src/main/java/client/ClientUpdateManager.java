package client;

import board.Board;
import player.Player;

import java.util.Observable;
import java.util.Observer;

public class ClientUpdateManager extends Observable {
    private Player player;
    private Board board;

    protected ClientUpdateManager(){
    }

    public void setPlayer(Player player) {
        this.player = player;
        setChanged();
        notifyObservers(player);
    }

    public void setBoard(Board board) {
        this.board = board;
        setChanged();
        notifyObservers(board);
    }

    public Player getPlayer() {
        return player;
    }

    public Board getBoard() {
        return board;
    }
}
