package view;
import board.Cell;
import player.Player;
import weapon.WeaponCard;

/**
 * 
 */
public class PlayerView {


    /**
     * Default constructor
     */
    public PlayerView() {
    }

    /**
     * 
     */
    public Player player;

    /**
     * 
     */
    public PlayerBoardView myPlayerBoard;



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


    private boolean finalMove(Cell cell) {
        // TODO implement here
        return false;
    }


    private boolean finalGrab(Cell cell) {
        /**player.move(cell)
         * getCard(int i);
         * player.turns--;
         */
        return false;
    }


    private boolean finaShoot(Cell cell, WeaponCard weapon) {
        // TODO implement here
        return false;
    }


    public void drawCLI() {
        // TODO implement here

    }


    public void drawGUI() {
        // TODO implement here

    }

}