package controller;
import board.Cell;
import player.Player;
import view.PlayerView;
import weapon.WeaponCard;

import java.util.*;

/**
 * 
 */
public class PlayerController {

    /**
     * Default constructor
     */
    public PlayerController() {
    }

    /**
     * 
     */
    private PlayerView playerView;

    /**
     * 
     */
    private Player player;


    /**
     * switch(command)
     *  case "MOVE" ecc.
     * argomento potrebbe essere la cella di dest.
     * @param command
     * @param arg
     * @return
     */
    public void update(PlayerCommand command, Object arg) {
        // TODO implement here
    }

    /**
     * @param cell 
     * @return
     */
    public boolean move(Cell cell) {
        // TODO implement here
        return false;
    }

    /**
     * @param cell 
     * @return
     */
    public boolean grab(Cell cell) {
        // TODO implement here
        return false;
    }

    /**
     * @param opponents 
     * @param weapon 
     * @return
     */
    public boolean shoot(List<Player> opponents, WeaponCard weapon) {
        // TODO implement here
        return false;
    }

}