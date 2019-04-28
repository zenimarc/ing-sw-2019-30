package controller;
import attack.Attack;
import board.Billboard;
import board.Cell;
import deck.Card;
import player.Player;
import view.PlayerView;
import weapon.WeaponCard;

import java.util.*;

import static controller.PlayerCommand.MOVE;

//TODO finire la shoot e decidere come gestirla

/**
 * PlayerController is used to control if a player can do certain actions
 */
public class PlayerController implements Observer {
    private BoardController boardControl;
    private Billboard billboard;
    private PlayerView playerView;
    private Player player;

    /**
     * Default constructor
     */
    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public void update(Observable view, Object obj){
        switch (((CommandObj)obj).getCmd()) {
            case MOVE:
                move(((CommandObj)obj).getCell(), 0);
                break;
            case GRAB:
                grab(((CommandObj)obj).getCell(), ((CommandObj)obj).getWeaponSelector());
                break;
            case SHOOT: //TODO verificare come implementarlo per bene
                //move(((CommandObj)obj).getCell(), 0);
                break;
            case END_TURN:
                boardControl.changeTurn();
                break;
        }
    }

    /*
     * switch(command)
     *  case "MOVE" ecc.
     * argomento potrebbe essere la cella di dest.
     * @param command which command a players wants to do
     * @param arg which parameter is used
     * @return
     */
/*
    TODO: Ho riadattato il metodo per poter fare override di update() degli observer, se può andare quello sopra, questo va eliminato
    public void update(PlayerCommand command, Object arg, int val) {
        switch (command) {
            case MOVE:
                move((Cell)arg, 0);
                break;
            case GRAB:
                grab((Cell)arg, val);
                break;
            case SHOOT:
                move((Cell)arg, 0);
                break;
            case END_TURN:
                boardControl.changeTurn();
                break;
        }
    }
    */
    /**
     * This function controls the MOVE action and verifies if a player can move to a cell
     * @param cell of destination
     * @return if the player can move, else false
     */
    public boolean move(Cell cell, int i) {
        switch(i) {
            case 0: //normal move
                if (!boardControl.isFinalFrenzy()) {//non final Frenzy
                    if (!billboard.canMove(player.getPawn().getCell(), cell, 3))
                        return false; }
                else if (!billboard.canMove(player.getPawn().getCell(), cell, 4))
                    return false;
                player.getPawn().setCell(cell);
                return true;

            case 1: //move from grab
                if (!boardControl.isFinalFrenzy()) { //not FinalFrenzy
                    if (player.getPlayerBoard().getNumDamages() > 2) {
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
                            return false; }
                    else if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                        return false; }
                else {//Final Frenzy
                    if (boardControl.verifyTwoTurnsFrenzy()) {
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
                            return false; }
                    else if (!billboard.canMove(player.getPawn().getCell(), cell, 3))
                        return false;
                }

                player.getPawn().setCell(cell);
                return true;

            case 2: //move from shoot
                if (!boardControl.isFinalFrenzy()) { //not FinalFrenzy
                    if (player.getPlayerBoard().getNumDamages() > 5) {
                        if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                            return false; }
                    else {//Final Frenzy
                        if (boardControl.verifyTwoTurnsFrenzy()) {
                            if (!billboard.canMove(player.getPawn().getCell(), cell, 1))
                                return false; }
                        else if (!billboard.canMove(player.getPawn().getCell(), cell, 2))
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
    public Card grab(Cell cell, int val) {//TODO aggiunge la carta ed è boolean
        if(!this.move(cell, 1))
            return null;
        //TODO lancia errore se il movimento non è valido
        return cell.getCard(val);
    }

    /**
     * This function controls the SHOOT action
     * @param cell of destination of the movement
     * @param i the weapon of choice to use
     * @return true if the attack was successful, false otherwise
     */
    public boolean shoot(Cell cell, int i/*List<Player> opponents, WeaponCard weapon*/) {
        if(!this.move(cell, 2))
           return false; //lancia errore
        // for(int j = 0; j < weapon.getAttacks().size(); j++){
        chooseAttack(chooseWeapon(i), i);//TODO modificare la seconda i perchè è relativa alla scelta degli attacchi
        //chooseTarget(attack, oggetto da colpire);
        return true;
    }

    /**
     * This function returns the weapon the player chose to use
     * @param i the number of the weapon
     * @return the weapon desired
     */
    public WeaponCard chooseWeapon(int i){
       return player.getWeapons().get(i);
    }

    /**
     * This function returns the attack the player wants to use
     * @param weapon wanted to use
     * @param k the number of the attack
     * @return the attack wanted
     */

    public boolean chooseAttack(WeaponCard weapon, int k){
        /**if(weapon.getAttacks().get(i).getattack se è Optional || è optional ed è senza priorità)
         *  return false
         * return true;
         */
        return false;
    }

    /**
     * This function verifies if the target is hittable or not
     * @param attack the attack the player wants to use
     * @param obj a cell o a list of target to hit
     * @return true if the attack was successful, false otherwise
     */
    public boolean chooseTarget(Attack attack, Object obj){
            return false;
        }
    }
