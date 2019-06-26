package view;

import attack.Attack;
import board.Board;
import board.Position;
import player.Player;
import weapon.WeaponCard;

import java.util.List;
import java.util.Observer;
import java.util.WeakHashMap;
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
    public void grab() {
        playerView.grab();
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
}
