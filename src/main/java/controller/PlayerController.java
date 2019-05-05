package controller;
import attack.Attack;
import board.Billboard;
import board.Cell;
import deck.AmmoCard;
import deck.Card;
import deck.PowerUp;
import player.Player;
import view.PlayerView;
import weapon.WeaponCard;

import java.util.*;

import static controller.PlayerCommand.MOVE;
import static deck.PowerUp.KINETICRAY;

//TODO finire la shoot e decidere come gestirla, fare overloading di alcune funzioni

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

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(Observable view, Object obj){
        switch (((CommandObj)obj).getCmd()) {
            case MOVE:
                move(((CommandObj)obj).getCell(), 10);
                break;
            case GRAB:
                grab(((CommandObj)obj).getCell(), ((CommandObj)obj).getWeaponSelector());
                break;
            case SHOOT: //TODO verificare come implementarlo per bene
                //move(((CommandObj)obj).getCell(), 0);
                break;
            case POWERUP:
                verifyPowerUp(((CommandObj)obj).getCell(), ((CommandObj)obj).getWeaponSelector());
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
            case 10: //normal move
                if (!boardControl.isFinalFrenzy()) {//non final Frenzy
                    if (!billboard.canMove(player.getPawn().getCell(), cell, 3))
                        return false; }
                else if (!billboard.canMove(player.getPawn().getCell(), cell, 4))
                    return false;
                setCell(cell);
                return true;

            case 11: //move from grab
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

                setCell(cell);
                return true;

            case 12: //move from shoot
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

                    setCell(cell);
                    return true;
                }
        }
        return false;
    }

     public boolean movePlayer(Player player, Cell cell, int i){
        if (!billboard.canMove(player.getPawn().getCell(), cell, i))
            return false;
        setOtherCell(player, cell);
        return true;
     }

    /**
     * This function controls the GRAB action
     * @param cell of destination
     * @return true if the action was successful, else false
     */
    public boolean grab(Cell cell, int val) {//TODO aggiunge la carta ed è boolean
        if(!this.move(cell, 11))
            return false;
        cell.giveCard(this.player, val);
            return true;
    }

    /**
     * This function controls the SHOOT action
     * @param cell of destination of the movement
     * @param i the weapon of choice to use
     * @return true if the attack was successful, false otherwise
     */
    public boolean shoot(Cell cell, int i) {
        if(!this.move(cell, 12))
           return false; //lancia errore
        chooseAttack(chooseWeapon(i), i);//TODO modificare la seconda i perchè è relativa alla scelta degli attacchi
        //chooseTarget(attack, oggetto da colpire);
        return true;
    }

    /**
     * This function returns the weapon the player chooses to use
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

    /**
     * This function modifies the position of the pawn of another player
     * @param player to be moved
     * @param cell of destination
     */

    public void setOtherCell(Player player, Cell cell){
        player.getPawn().setCell(cell);
    }

    /**
     * This function modifies the position of the pawn
     * @param cell of destination
     */

    public void setCell(Cell cell){
        this.player.getPawn().setCell(cell);
    }

    public boolean verifyPowerUp(Cell cell, int i) { //TODO gestire la questione costi ed i rimanenti power up
        /* if(player.getPowerups().get(i).getPowerUp() == PowerUp.KINETICRAY){
        chooseTarget(); TODO fare overloading di chooseTarget
        movePlayer(player, cell, 2);
        return true;
    }*/
        if(player.getPowerups().get(i).getPowerUp() == PowerUp.TELEPORTER) {
            setCell(cell);
            return true;
        }
        return false;
    }

}

