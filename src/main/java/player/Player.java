package player;

import board.Board;
import constants.Constants;
import deck.Bullet;
import deck.Color;
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
    private ArrayList<Bullet> ammo; //TODO: in che modo vogliamo memorizzare le ammo nell'arraylist

    public Player(String nickname, Board board) {
        this.nickname = nickname;
        this.pawn = new Pawn(this);
        this.points = 0; //a new player has 0 points
        this.playerBoard = new PlayerBoard(board);
        this.weapons = new WeaponCard[Constants.MAX_WEAPON_HAND_SIZE.getValue()];
        this.powerups = new PowerCard[Constants.MAX_POWER_HAND_SIZE.getValue()];
        this.ammo = new ArrayList<>();
    }

    public Pawn getPawn(){return this.pawn;}
    public String getName(){return this.nickname;}

    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }

    public void addPoints(int points){
        this.points += points;
    }
    public WeaponCard[] getWeapons(){
        return this.weapons;
    }
    public PowerCard[] getPowerups(){
        return this.powerups;
    }
    public List<Bullet> getBullets(){
        return this.ammo;
    }

    @Override
    public String toString() {
        return this.nickname;
    }
}