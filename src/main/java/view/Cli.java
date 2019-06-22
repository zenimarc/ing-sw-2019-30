package view;

import board.Board;
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
    public void gameStart() {}

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

    @Override
    public void regeneratePlayer() {
        playerView.regeneratesPlayer();
    }

    @Override
    public void updatePlayer(Player player) {
        playerView.setPlayer(player);
        playerBoardView.setPlayer(player);
        playerBoardView.drawPlayerboard();
    }

    @Override
    public void updateBoard(Board board) {
        boardViewCLI.setBoard(board);
        boardViewCLI.drawCLI();
    }

    @Override
    public void grab() {
        playerView.grab();
    }
}
