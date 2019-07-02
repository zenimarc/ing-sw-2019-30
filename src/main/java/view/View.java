package view;

import attack.Attack;
import board.Board;
import board.Cell;
import board.Position;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;
import weapon.WeaponCard;

import java.io.FileNotFoundException;
import java.util.*;


public interface View {
    void gameStart(Board board);
    void giveMessage(String title, String mex);
    void giveError(String error);
    boolean loadWeapon(List<String> notLoaded);
    void myTurn();
    void notMyTurn(String nameOfWhoPlay);
    void showBoard();
    void regeneratePlayer();
    void updatePlayer(Player player);
    void updateBoard(Board board);
    void grab(Cell cell);
    List<String> getTargetsName(List<Player> potentialTarget, int maxTarget);
    List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom);
    Position choosePositionToAttack(List<Position> potentialposition);
    void askPowerUp(ArrayList<PowerCard> cards, PowerUp power);
    void usePowerUp();
    void payGunsight(int[] bullets,PowerCard card);
    void payPowerUp(PowerCard card);
    void useTeleport();
    void useKineticray(List<Player> player);
    void giveRoundScore(String playerDead, Map<String, Integer> points);
}
