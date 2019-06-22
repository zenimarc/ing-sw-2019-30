package view;

import board.Board;
import client.Client;
import client.ClientUpdateManager;
import player.Player;

import java.util.Observer;

public class Cli implements View {
    private PlayerView playerView;
    private PlayerBoardView playerBoardView;
    private BoardViewCLI boardViewCLI;

    public Cli(Player player, Board board, Observer client){
        this.playerView = new PlayerView(player, client);
        this.playerBoardView = new PlayerBoardView(player);
        this.boardViewCLI = new BoardViewCLI(board);
    }

    @Override
    public void gameStart() {
        showBoard();
    }

    @Override
    public void giveMessage(String title, String mex) {
        playerView.print(mex);
    }

    @Override
    public void giveError(String error) {
        playerView.printError(error);
    }

    @Override
    public boolean loadWeapon() {
        return playerView.loadWeapon();
    }

    @Override
    public void myTurn() {
        playerView.myTurn();
    }

    @Override
    public void notMyTurn(String nameOfWhoPlay) {
        playerView.print(playerView.stringForTurnOf(nameOfWhoPlay));
    }

    @Override
    public void showBoard() {
        boardViewCLI.drawCLI();
        playerBoardView.drawPlayerboard();
    }

}
