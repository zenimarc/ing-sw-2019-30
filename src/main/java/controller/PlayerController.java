package controller;
import board.Billboard;
import board.Cell;
import player.Player;
import view.PlayerView;
import weapon.WeaponCard;

import java.util.*;

import static controller.PlayerCommand.MOVE;

/**
 * 
 */
public class PlayerController {
    private BoardController boardControl;
    private Billboard billboard;

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
     * @param command which command a players wants to do
     * @param arg which parameter is used
     * @return
     */
    public void update(PlayerCommand command, Object arg) {
        switch (command) {
            case MOVE:
                move((Cell)arg);
                break;
            case GRAB:
                grab((Cell)arg);
                break;
            case SHOOT: //TODO verificare come implementarlo per bene
                move((Cell)arg);
                break;
            case END_TURN:
                boardControl.changeTurn();
                break;
        }
    }

    /**
     * @param cell 
     * @return
     */
    public boolean move(Cell cell) {
        if(!boardControl.isFinalFrenzy()){
            if(billboard.canMove(player.getPawn().getCell(), cell, 3))
                return true;
            else return false;}
        else return billboard.canMove(player.getPawn().getCell(), cell, 4);
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