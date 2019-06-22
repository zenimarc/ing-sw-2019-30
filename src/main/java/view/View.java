package view;

import board.Board;
import player.Player;


public interface View  {
    void gameStart();
    void giveMessage(String title, String mex);
    void giveError(String error);
    boolean loadWeapon();
    void myTurn();
    void notMyTurn(String nameOfWhoPlay);
    void showBoard();
    void regeneratePlayer();
    void updatePlayer(Player player);
    void updateBoard(Board board);
    void grab();

}
