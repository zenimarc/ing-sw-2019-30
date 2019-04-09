package player;

import board.Board;
import constants.Constants;
import deck.Bullet;
import deck.Color;
import deck.PowerCard;
import deck.WeaponCard;

import java.util.*;

import static deck.Color.*;

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
    private Map<Color, Integer> ammo;
    private int marksGiven; //each player can give max 3 marks to other players (the sum of marks given mustn't exceed 3)

    public Player(String nickname, Board board) {
        this.nickname = nickname;
        this.pawn = new Pawn(this);
        this.points = 0; //a new player has 0 points
        this.playerBoard = new PlayerBoard(board);
        this.weapons = new WeaponCard[Constants.MAX_WEAPON_HAND_SIZE.getValue()];
        this.powerups = new PowerCard[Constants.MAX_POWER_HAND_SIZE.getValue()];
        this.ammo = new EnumMap<>(Color.class);
        this.marksGiven = 0;
    }

    public Pawn getPawn(){return this.pawn;}
    public String getName(){return this.nickname;}

    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }

    public void addPoints(int points){
        this.points += points;
    }
    public int getPoints(){
        return this.points;
    }
    public WeaponCard[] getWeapons(){
        return this.weapons;
    }
    public PowerCard[] getPowerups(){
        return this.powerups;
    }
    public Map<Color, Integer> getBullets(){
        return this.ammo;
    }

    /**public boolean useAmmo(Map<Color, Integer> bullets){
        bullets.forEach(Map.Entry.useAmmo(x.getKey(), x.getValue()));
        return false;
    }**/
    private boolean canPay(int[] ammo) {
        for (int i = 0; i < ammo.length; i++)
            if (ammo[i] > this.ammo.get(Color.values()[i]) || this.ammo.get(Color.values()[i]) == null)
                return false;
        return true;
    }

    public boolean useAmmo(int[] ammo){
        if (canPay(ammo)) {
            addAmmo(Arrays.stream(ammo).map(x -> -x).toArray());
            return true;
        }
        else
            return false;
    }

    public boolean useBullet(Color color, int count) {
        if (count > ammo.get(color)) return false;
        else {
            this.ammo.put(color, ammo.get(color) - count);
            return true;
        }
    }

    /**
     * this function adds ammo to the player from an ammo[] array
     * player ammo are capped per color by MAX_BULLET_PER_COLOR constants
     * @param ammo array containing ammo cost for each color
     *             (ammo[0] -> RED cost, ammo[1] -> YELLOW cost, ammo[2] -> BLUE cost)
     */

    public void addAmmo(int[] ammo){
        this.ammo.put(RED, (this.ammo.get(RED) != null) ?
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), this.ammo.get(RED)+ammo[0]) :
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), ammo[0]));
        this.ammo.put(YELLOW, (this.ammo.get(YELLOW) != null) ?
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), this.ammo.get(YELLOW)+ammo[1]) :
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), ammo[1] ));
        this.ammo.put(BLUE, (this.ammo.get(BLUE) != null) ?
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), this.ammo.get(BLUE)+ammo[2]) :
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), ammo[2] ));

    }

    @Override
    public String toString() {
        return this.nickname;
    }
}