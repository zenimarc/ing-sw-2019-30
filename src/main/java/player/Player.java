package player;

import constants.Constants;
import deck.Bullet;
import deck.PowerCard;
import deck.WeaponCard;

import java.util.*;

/**
 * 
 */
public class Player {

    private String nickname;
    private Pawn pawn;
    private int points;
    private PlayerBoard playerBoard;
    private WeaponCard[] weapons;
    private PowerCard[] powerups;
    private ArrayList<Bullet> ammo;


    public Player(String nickname) {
        this.nickname = nickname;
        this.pawn = new Pawn(this);
        this.points = 0; //a new player has 0 points
        this.playerBoard = new PlayerBoard();
        this.weapons = new WeaponCard[Constants.MAX_WEAPON_HAND_SIZE.getValue()];
        this.powerups = new PowerCard[Constants.MAX_POWER_HAND_SIZE.getValue()];
        this.ammo = new ArrayList<Bullet>();
    }

    public Pawn getPawn(){return this.pawn;}
    public String getName(){return this.nickname;}

    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }

    @Override
    public String toString() {
        return this.nickname;
    }
}