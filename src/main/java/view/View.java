package view;

import attack.Attack;
import board.Board;
import board.Position;
import player.Player;
import weapon.WeaponCard;

import java.util.List;
import java.util.WeakHashMap;


public interface View  {
    void gameStart();
    void giveMessage(String title, String mex);
    void giveError(String error);
    boolean loadWeapon(List<String> notLoaded);
    void myTurn();
    void notMyTurn(String nameOfWhoPlay);
    void showBoard();
    void regeneratePlayer();
    void updatePlayer(Player player);
    void updateBoard(Board board);
    void grab();
    List<String> getTargetsName(List<Player> potentialTarget, int maxTarget);
    List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom);
    Position choosePositionToAttack(List<Position> potentialposition);


}
