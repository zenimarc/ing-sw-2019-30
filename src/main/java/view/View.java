package view;

import attack.Attack;
import board.Board;
import board.Cell;
import board.Position;
import constants.Constants;
import controller.EnumCommand;
import deck.Card;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;
import java.util.*;


public interface View {
    void gameStart(Board board);
    void giveMessage(String title, String mex);
    void giveError(String error);
    boolean loadWeapon(List<String> notLoaded);
    void myTurn(Constants modAction);
    void notMyTurn(String nameOfWhoPlay);
    void showBoard();
    void regeneratePlayer();
    void updatePlayer(Player player);
    void updateBoard(Board board);
    void grab(Cell cell);
    List<String> getTargetsName(List<Player> potentialTarget, int maxTarget);
    List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom);
    Position choosePositionToAttack(List<Position> potentialposition);
    void askPowerUp(List<PowerCard> powers);
    void usePowerUp(PowerUp powerUpType);
    void payGunsight(int[] bullets, int card);
    void payPowerUp(PowerCard card);
    void useKineticray(List<Player> player);
    void giveScore(String playerDead, Map<String, Integer> points);
    void chooseGunsightTarget(List<Player> targets);
    void discardPowerUp(Card object2);
    void weaponIndexToDiscard(List<String> potentialWeapon, int weaponToGrab);
    void shoot();
    void move(EnumCommand typeAction);
    int askAttackPriority();
}
