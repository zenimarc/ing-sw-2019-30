package view;

import attack.Attack;
import board.Board;
import board.Cell;
import board.Position;
import constants.Constants;
import deck.Card;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;
import weapon.WeaponCard;

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
    void askPowerUp(PowerUp power);
    void usePowerUp();
    void payGunsight(int[] bullets, int card);
    void payPowerUp(PowerCard card);
    void useTeleport();
    void useKineticray(List<Player> player);
    void giveRoundScore(String playerDead, Map<String, Integer> points);
    void chooseGunsightTarget(List<Player> targets);
    void discardPowerUp(Card object2);
    void discardWeapon(WeaponCard object);
    void shoot();
}
