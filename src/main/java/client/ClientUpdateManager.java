package client;

import board.Board;
import controller.CommandObj;
import player.Player;
import view.View;

import java.util.Observable;
import java.util.Observer;

import static controller.EnumCommand.UPDATE_PLAYER;

public class ClientUpdateManager extends Observable {
    private Player player;
    private Board board;
    private View view;

    protected ClientUpdateManager() {}

    public void setView(View view) {
        this.view = view;
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

    public void executeCommand(CommandObj cmd){
        switch (cmd.getCmd()) {
            case GRAB:
                view.grab();
                break;
            case SHOW_BOARD:
                view.showBoard();
                break;
            case REG_CELL:
                view.regeneratePlayer();
                break;
            case YOUR_TURN:
                view.myTurn();
                break;
            case UPDATE_PLAYER:
                updatePlayer(cmd.getObject());
                break;
            case UPDATE_BOARD:
                updateBoard(cmd.getObject());
                break;
            case NOT_YOUR_TURN:
                view.notMyTurn((String) cmd.getObject());
                break;
            default:
                break;
        }
    }

    /**
     * Update local Player
     * @param obj object received from server
     */
    private void updatePlayer(Object obj){
        if(obj.getClass().equals(Player.class)) {
            this.player = (Player) obj;
            view.updatePlayer(player);
        }
    }

    /**
     * Update local Board
     * @param obj object received from server
     */
    private void updateBoard(Object obj){
        if(obj.getClass().equals(Board.class)) {
            this.board = (Board) obj;
            view.updateBoard(board);
        }
    }

}
