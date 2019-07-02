package view;

import attack.Attack;
import board.Board;
import board.Cell;
import board.Position;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;
import weapon.WeaponCard;

import java.util.*;
import java.util.stream.Collectors;

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
    public void gameStart(Board board) {}

    @Override
    public void giveMessage(String title, String mex) {
        playerView.print(mex);
    }

    @Override
    public void giveError(String error) {
        playerView.printError(error);
    }

    @Override
    public boolean loadWeapon(List<String> notLoaded) {
        return playerView.loadWeapon(notLoaded);
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
    public void grab(Cell cell) {
        playerView.grab(cell);
    }

    @Override
    public List<String> getTargetsName(List<Player> potentialTarget, int maxTarget) {
        return playerView.chooseTargets(maxTarget, potentialTarget)
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom){
        return playerView.chooseOptionalAttack(attacks, canRandom);
    }

    @Override
    public Position choosePositionToAttack(List<Position> potentialposition) {
        return playerView.chooseCellToAttack(potentialposition);
    }

    @Override
    public void askPowerUp(ArrayList<PowerCard> cards, PowerUp power) {
        playerView.askForPowerUp(cards, power);
    }

    @Override
    public void usePowerUp() {
        playerView.usePowerUp();
    }

    @Override
    public void payGunsight(int[] bullets, PowerCard card) {
        playerView.askPayGunsight(bullets, card);
    }

    @Override
    public void payPowerUp(PowerCard card) {
        playerView.askToPay(card);
    }

    @Override
    public void useTeleport() {
        playerView.moveTeleporter();
    }

    @Override
    public void useKineticray(List<Player> players) {
        playerView.chooseTargets(players);
    }

    @Override
    public void giveRoundScore(String playerDead, Map<String, Integer> points) {
        playerView.giveRoundScore(playerDead, points);
    }
}
