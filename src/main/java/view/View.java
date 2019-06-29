package view;

import attack.Attack;
import board.Board;
import board.Position;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;
import weapon.WeaponCard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.WeakHashMap;


public interface View {
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
    void askPowerUp(ArrayList<PowerCard> cards, PowerUp power);
    void usePowerUp(ArrayList<PowerCard> cards);
    void payGunsight(int[] bullets,PowerCard card);
    void payPowerUp(PowerCard card);
    void useTeleport();
    void useKineticray(ArrayList<Player> player);
}
