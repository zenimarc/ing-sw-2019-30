package view;
import board.Cell;
import player.Player;
import weapon.WeaponCard;

/**
 * 
 */
public class PlayerView {
    private Player player;
    private PlayerBoardView myPlayerBoard;

    /**
     * Default constructor
     */
    public PlayerView(Player player, PlayerBoardView view) {
        this.player = player;
        this.myPlayerBoard = view;
    }

    public boolean move(Cell cell) {
        // TODO implement here
        return false;
    }

    public boolean grab(Cell cell) {
        /**if(player.Playerboard.damageTrack[2)!= null)
         *  player.move(cell);
         * getCard(int i);
         * player.turns--;
         */
        return false;
    }

    public boolean shoot(Cell cell, WeaponCard weapon) {
        // TODO implement here
        return false;
    }

    public void chooseTarget(){
        //TODO implement here
    }

    public void drawCLI(BoardView map) {
        map.drawCLI();
    }


    public void drawGUI() {
        // TODO implement here

    }

}