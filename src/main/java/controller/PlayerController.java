package controller;
import board.Billboard;
import board.Cell;
import board.NormalCell;
import deck.AmmoCard;
import deck.Card;
import player.Player;
import view.PlayerView;
import weapon.WeaponCard;

import java.util.*;

import static controller.PlayerCommand.MOVE;

//TODO finire la shoot e decidere come gestirla

/**
 * 
 */
public class PlayerController {
    private BoardController boardControl;
    private Billboard billboard;

    /**
     * Default constructor
     */
    public PlayerController(Player player) {
        this.player = player;
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
    public void update(PlayerCommand command, Object arg, int val) {
        switch (command) {
            case MOVE:
                move((Cell)arg, 0);
                break;
            case GRAB:
                grab((Cell)arg, val);
                break;
            case SHOOT: //TODO verificare come implementarlo per bene
                move((Cell)arg, 0);
                break;
            case END_TURN:
                boardControl.changeTurn();
                break;
        }
    }

    /**
     * This function controls the MOVE action and verifies if a player can move to a cell
     * @param cell of destination
     * @return if the player can move, else false
     */
    public boolean move(Cell cell, int i) {
        switch(i) {
            case 0: //normal move
                if (!boardControl.isFinalFrenzy()) {
                    if (!billboard.canMove(player.getPawn().getCell(), cell, 3))
                        return false;
                } else if (!billboard.canMove(player.getPawn().getCell(), cell, 4))
                    return false;
                player.getPawn().setCell(cell);
                return true;

            case 1: //move from grab
                if (!boardControl.isFinalFrenzy()) { //not FinalFrenzy
                    if (player.getPlayerBoard().getNumDamages() > 2) {
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
                            return false;
                    } else if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                        return false;
                } else {//Final Frenzy
                    if (boardControl.verifyTwoTurnsFrenzy()) {
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
                            return false;
                    } else if (!billboard.canMove(player.getPawn().getCell(), cell, 3))
                        return false;
                }

                player.getPawn().setCell(cell);
                return true;

            case 2: //move from shoot
                if (!boardControl.isFinalFrenzy()) { //not FinalFrenzy
                    if (player.getPlayerBoard().getNumDamages() > 5) {
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                            return false;
                    } else {//Final Frenzy
                        if (boardControl.verifyTwoTurnsFrenzy()) {
                            if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                                return false;
                        } else if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
                            return false;
                    }

                    player.getPawn().setCell(cell);
                    return true;
                }
        }
        return false;
    }


    /**
     * This function controls the GRAB action
     * @param cell of destination
     * @return true if the action was successful, else false
     */
    public Card grab(Cell cell, int val) {
        if(!this.move(cell, 1))
            return null;
        //TODO lancia errore se il movimento non è valido
        return cell.getCard(val);
    }


    /**
     * @param opponents 
     * @param weapon 
     * @return
     */
    public boolean shoot(List<Player> opponents, WeaponCard weapon) {
       // if(!this.move(cell, 2))
        //    return false; //lancia errore
        return false;
    }

}