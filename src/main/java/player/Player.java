package player;

import board.Board;
import board.Cell;
import constants.Constants;
import deck.Color;
import deck.PowerCard;
import weapon.WeaponCard;

import java.util.*;

import static deck.Color.*;

/**
 * Player saves all information about a player
 */
public class Player extends Observable {

    private String nickname;
    private Pawn pawn;
    private int points;
    private PlayerBoard playerBoard;
    private ArrayList<WeaponCard> weapons;
    private ArrayList<PowerCard> powerups;
    private Map<Color, Integer> ammo;

    /**
     * Default constructors
     */


    public Player(String nickname){
        this.nickname = nickname;
        this.pawn = new Pawn(this);
        this.points = 0; //a new player has 0 points
        this.weapons = new ArrayList<>();
        this.powerups = new ArrayList<>();
        this.ammo = new EnumMap<>(Color.class);
    }

    public Player(String nickname, Board board) {
        this(nickname);
        this.playerBoard = new PlayerBoard(board);
    }


    public void setPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * This function returns the pawn of the player
     * @return pawn of the player
     */
    public Pawn getPawn(){return this.pawn;}

    /**
     * This function returns the position of the player
     * @return player position
     */
    public Cell getCell(){ return this.pawn.getCell();}

    /**
     * This set player cell
     * @param c
     */
    public void setCell(Cell c){this.pawn.setCell(c);}

    /**
     * This function modifies the cell of the player and changes the pawn list of initial and destination cell
     * @param cell of destination
     */
    public void setPawnCell(Cell cell){
        this.pawn.getCell().removePawn(this.pawn);
        setCell(cell);
        cell.addPawn(this.pawn);
    }

    /**
     * This function returns the name of the player
     * @return the name of the player
     */
    public String getName(){return this.nickname;}

    /**
     * This function returns the points of the player
     * @return the points the player has earned
     */
    public int getPoints(){
        return this.points;
    }

    /**
     * This function returns the board which the player can see
     * @return the board seen by the player
     */
    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }

    /**
     * This function returns the list of weapons the player has
     * @return weapons the player has
     */
    public List<WeaponCard> getWeapons(){
        return this.weapons;
    }

    /**
     * This function returns the list of power ups the player has
     * @return the power up the player has
     */
    public List<PowerCard> getPowerups(){
        return this.powerups;
    }

    /**
     * This function adds the points to a player after a kill
     * @param points to give
     */
    public void addPoints(int points){
        this.points += points;
    }

    /**
     * @return an HashMap containing player's ammo by color.
     */
    public Map<Color, Integer> getBullets(){
        return this.ammo;
    }

    /**
     * This function checks if the player can pay for the indicated ammo.
     * @param ammo is an array containing ammo to pay per color
     * @return True if the player has enough ammo, else False.
     */
    public boolean canPay(int[] ammo) {
        for (int i = 0; i < ammo.length; i++)
            if (ammo[i] > this.ammo.get(Color.values()[i]) || this.ammo.get(Color.values()[i]) == null)
                return false;
        return true;
    }

    /**
     * this function first checks if the player has enough ammo, then use the indicated ammo from the array.
     * if the player cannot pay the indicated ammo, his ammo are not modified.
     * @param ammo is an array containing bullet cost for each color
     * @return True if the player can pay the requested ammo, else return False.
     */
    public boolean useAmmo(int[] ammo){
        if (canPay(ammo)) {
            addAmmo(Arrays.stream(ammo).map(x -> -x).toArray());
            setChanged();
            notifyObservers();
            return true;
        }
        else
            return false;
    }

    /**
     * this function adds ammo to the player from an ammo[] array
     * requires ammo.length()>=3
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
        setChanged();
        notifyObservers();
    }

    /**
     * This function adds a weapon to the list of weapons the players has
     * @param weaponCard to add
     * @return true if it is possible, else false
     */
    public boolean addWeapon(WeaponCard weaponCard){
        if(weapons.size()<Constants.MAX_WEAPON_HAND_SIZE.getValue()){
            weapons.add(weaponCard);
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * This function adds damage to this player from an opponent
     * if there are marks from the indicated opponent they will be converted into damages
     * @param opponent who is giving damage
     * @param shots to give
     */
    public void addDamage(Player opponent, int shots){
        this.playerBoard.addDamage(opponent,shots);
        setChanged();
        notifyObservers();
    }

    public void addDamage(Player opponent){
        this.addDamage(opponent,1);
    }

    /**
     * This function adds marks to this player from an opponent
     * only if it's possible to add another mark (marks < 3)
     * @param opponent who is giving marks
     * @param mark number of marks given
     */
    public void addMark(Player opponent, int mark){
        this.playerBoard.addMark(opponent,mark);
        setChanged();
        notifyObservers();
    }

    /**
     * adds single mark from indicated opponent
     * @param opponent who's giving mark to this player
     */
    public void addMark(Player opponent){
        this.addMark(opponent, 1);
    }

    /**
     * this function returns the current marks of this player from an indicated opponent
     * @param opponent who has given marks to this player
     * @return marks given to this player by indicated opponent
     */
    public int getMarks(Player opponent){
        return this.playerBoard.getMarks(opponent);
    }

    /**
     * This function returns the number of damage received by a player
     * @return the number of damage received
     */
    public int getNumDamages(){
        return this.playerBoard.getNumDamages();
    }

    /**
     *  This function verifies if a player is visible
     * @param opponent you want to verify if he is visible
     * @return true if visible, else false
     */
    public boolean canView(Player opponent){
        return (playerBoard.getBoard().getBillboard().isVisible(this.pawn.getCell(), opponent.pawn.getCell()));
    }

    /**
     * This function adds a power up card to the player
     * @return true if it was possible, else false
     */
    public boolean addPowerCard() {
        if(powerups.size() < Constants.MAX_POWER_HAND_SIZE.getValue()) {
            powerups.add((PowerCard)playerBoard.getBoard().getPowerUpDeck().draw());
            setChanged();
            notifyObservers();
            return true;

        }
        return false;

    }

    public boolean usePowerUp(PowerCard power, boolean discard){
        if(discard){
            powerups.remove(power);
            return true;}
        else return(canPayPowerUp(power));
    }

    public boolean canPayPowerUp(PowerCard power){
        if(ammo.get(power.getBullet().getColor()) >= 1){
            ammo.replace(power.getBullet().getColor(), ammo.get(power.getBullet().getColor()) -1);
        return true;}
        else return false;
    }

    public boolean canPayGunsight(Color color){
        if(ammo.get(color) >= 1){
            ammo.replace(color, ammo.get(color)-1);
            return true;}
        else return false;
    }
}