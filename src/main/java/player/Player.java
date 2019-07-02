package player;

import board.Board;
import board.Cell;
import constants.Constants;
import constants.Color;
import deck.Card;
import org.jetbrains.annotations.NotNull;
import powerup.PowerCard;
import weapon.EnumWeapon;
import weapon.SimpleWeapon;
import weapon.WeaponCard;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static constants.Color.*;
import static deck.Bullet.mapToString;

/**
 * Player saves all information about a player
 */
public class Player extends Observable implements Cloneable, Serializable {

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
    public Player(String nickname) {
        this.nickname = nickname;
        this.pawn = new Pawn(this);
        this.playerBoard = new PlayerBoard();
        this.points = 0; //a new player has 0 points
        this.weapons = new ArrayList<>();
        this.powerups = new ArrayList<>();
        this.ammo = new EnumMap<>(Color.class);
        ammo.put(RED,1);
        ammo.put(YELLOW, 1);
        ammo.put(BLUE, 1);
    }

    /**
     * This function returns the pawn of the player
     * @return pawn of the player
     */
    public Pawn getPawn() {
        return this.pawn;
    }

    /**
     * This function returns the position of the player
     *
     * @return player position
     */
    public Cell getCell() {
        return this.pawn.getCell();
    }
    /**
     * This function returns the name of the player
     *
     * @return the name of the player
     */
    public String getName() {
        return this.nickname;
    }

    /**
     * This function returns the points of the player
     *
     * @return the points the player has earned
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * This function returns the board which the player can see
     *
     * @return the board seen by the player
     */
    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    /**
     * This function returns the list of weapons the player has
     *
     * @return weapons the player has
     */
    public List<WeaponCard> getWeapons() {
        return this.weapons;
    }

    /**
     * This function returns the list of power ups the player has
     * @return the power up the player has
     */
    public List<PowerCard> getPowerups() {
        return this.powerups;
    }

    /**
     * This function return a List of not loaded WeaponCards
     * @return a List
     */
    public List<WeaponCard> getNotLoaded(){
        return weapons.stream().filter(x -> !x.isReady()).collect(Collectors.toList());
    }

    public List<String> getNotLoadedName(){
        return  getNotLoaded().stream().map(WeaponCard::getName).collect(Collectors.toList());
    }
    /**
     * @return an HashMap containing player's ammo by color.
     */
    public Map<Color, Integer> getBullets() {
        return this.ammo;
    }


    /**
     * This function is used to understand if a player is dead
     * @return true if dead, else false
     */
    public boolean isDead(){return this.playerBoard.isDead();}
    /**
     * This function modifies the cell of the player and changes the pawn list of initial and destination cell
     * @param cell of destination
     */
    public void setPawnCell(Cell cell) {
        if(pawn.getCell()!=null) {
            this.pawn.getCell().removePawn(this.pawn);
        }
        this.pawn.setCell(cell);
        cell.addPawn(this.pawn);
        notifyEndAction();
    }

    /**
     * This function adds the points to a player after a kill
     * @param points to give
     */
    public void addPoints(int points) {
        this.points += points;
        notifyEndAction();
    }

    /**
     * This function checks if the player can pay for the indicated ammo.
     * @param ammo is an array containing ammo to pay per color
     * @return True if the player has enough ammo, else False.
     */
    public boolean canPay(@NotNull int[] ammo) {
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
    public boolean useAmmo(int[] ammo) {
        if (canPay(ammo)) {
            addAmmo(Arrays.stream(ammo).map(x -> -x).toArray());
            return true;
        } else
            return false;
    }

    /**
     * this function adds ammo to the player from an ammo[] array
     * requires ammo.length()>=3
     * player ammo are capped per color by MAX_BULLET_PER_COLOR constants
     * @param ammo array containing ammo cost for each color
     *             (ammo[0] -> RED cost, ammo[1] -> YELLOW cost, ammo[2] -> BLUE cost)
     */
    public void addAmmo(int[] ammo) {
        this.ammo.put(RED, (this.ammo.get(RED) != null) ?
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), this.ammo.get(RED) + ammo[0]) :
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), ammo[0]));
        this.ammo.put(YELLOW, (this.ammo.get(YELLOW) != null) ?
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), this.ammo.get(YELLOW) + ammo[1]) :
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), ammo[1]));
        this.ammo.put(BLUE, (this.ammo.get(BLUE) != null) ?
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), this.ammo.get(BLUE) + ammo[2]) :
                Math.min(Constants.MAX_BULLET_PER_COLOR.getValue(), ammo[2]));
    }

    /**
     * This function adds a weapon to the list of weapons the players has
     * @param weaponCard to add
     * @return true if it is possible, else false
     */
    public boolean addWeapon(WeaponCard weaponCard) {
        if (weapons.size() < Constants.MAX_WEAPON_HAND_SIZE.getValue()) {
            weapons.add(weaponCard);
            return true;
        }
        return false;
    }

    /**
     * This function unloads a WeaponCard of the player
     * @param weaponCard to be unloaded
     * @return true if unloaded, else false
     */
    public boolean setNotLoadWeapon (WeaponCard weaponCard){
        WeaponCard wp = weapons.stream()
                .filter(x -> x.equals(weaponCard))
                .findFirst()
                .orElse(null);
        if(wp!=null){
            wp.setNotLoaded();
            return true;
        }else return false;
    }

    /**
     * Remove weaponCard in position index
     * @param index index of card to remove
     * @return null
     */
    public Card rmWeapon(int index){
        if(index < weapons.size()){
            return weapons.remove(index);
        }
        return null;
    }

    /**
     * This function adds damage to this player from an opponent
     * if there are marks from the indicated opponent they will be converted into damages
     *
     * @param opponent who is giving damage
     * @param shots    to give
     */
    public void addDamage(Player opponent, int shots) {
        this.playerBoard.addDamage(opponent, shots);
    }

    public void addDamage(Player opponent) {
        this.addDamage(opponent, 1);
    }

    /**
     * This function adds marks to this player from an opponent
     * only if it's possible to add another mark (marks < 3)
     *
     * @param opponent who is giving marks
     * @param mark     number of marks given
     */
    public void addMark(Player opponent, int mark) {
        this.playerBoard.addMark(opponent, mark);
    }

    /**
     * adds single mark from indicated opponent
     *
     * @param opponent who's giving mark to this player
     */
    public void addMark(Player opponent) {
        this.addMark(opponent, 1);
    }

    /**
     * this function returns the current marks of this player from an indicated opponent
     *
     * @param opponent who has given marks to this player
     * @return marks given to this player by indicated opponent
     */
    public int getMarks(Player opponent) {
        return this.playerBoard.getMarks(opponent);
    }

    /**
     * This function returns the number of damage received by a player
     *
     * @return the number of damage received
     */
    public int getNumDamages() {
        return this.playerBoard.getNumDamages();
    }

    /**
     * This function add a Power Card to player
     * @param powerCard to be added
     * @return if possible without discarding another one
     */
    public boolean addPowerCard(PowerCard powerCard) {
        if (powerups.size() < Constants.MAX_POWER_HAND_SIZE.getValue()) {
            powerups.add(powerCard);
            return true;
        }
        return false;
    }

    /**
     * Clone this player
     * @return a clone of this player
     */
    public Player clonePlayer(){
        try{
            return (Player) this.clone();
        }catch (CloneNotSupportedException err){
            err.fillInStackTrace();
            return null;
        }
    }

    /**
     * This function resets damages given to player
     */
    public void resetDamage(){
        this.getPlayerBoard().clearDamages();
        notifyEndAction();
    }

    /**
     * This function return a string with some info about the player
     * @return a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("-.-.-.-.-.- ");
        sb.append(nickname);
        sb.append(" -.-.-.-.-.-\n");

//        sb.append("Position: " + pawn.getCell() + '\n');
        sb.append("Ammo: ");
        sb.append(mapToString(ammo));
        sb.append('\t');
        sb.append("Points: ");
        sb.append(points + '\n');

        sb.append(weaponsToString("WeaponCards in my hand", true));
        sb.append(weaponsToString("Placed WeaponCards", false));

        sb.append("PowerCard: " );
        if(!powerups.isEmpty()) {
            for (PowerCard pc : powerups) {
                sb.append(pc);
                sb.append('\t');
            }
        }else {
            sb.append("no PowerupCard");
        }
        sb.append('\n');

        sb.append("-.-.-.-.- ");
        sb.append("end ");
        sb.append(nickname);
        sb.append(" -.-.-.-.-\n");

        return sb.toString();
    }

    /**
     * This function
     * @param name
     * @param loaded
     * @return
     */
    private String weaponsToString(String name, boolean loaded){
        StringBuilder sb =new StringBuilder();
        sb.append(name);
        sb.append(": ");
        if(!weapons.isEmpty()) {
            for (WeaponCard wc : weapons) {
                if (wc.isReady() == loaded) {
                    sb.append(wc);
                    sb.append('\t');
                }
            }
        }else {
            sb.append("no Weapons");
        }
        sb.append('\n');
        return sb.toString();
    }

    /**
     * This function is used to understand if a player can pay a Power up
     * @param power to be paid
     * @return true if possible, else false
     */
    public boolean canPayPower(PowerCard power){
        return(ammo.get(power.getColor()) >= 1);

    }

    /**
     * This function is used to deal with power up payment
     * @param power to be paid
     * @param discard true if the card will be discarded
     * @return true if the PowerCard can be paid, else false
     */
    public boolean usePowerUp(PowerCard power, boolean discard){
        if(discard){
            powerups.remove(powerups.stream().filter(x -> x.equals(power)).findFirst().orElse(null));
            notifyEndAction();
            return true;
        }
        else return canPayPowerUp(power);
    }

    /**
     * This function creates an array which is used to verify if a player can pay Gunsight power up
     * @return an array
     */
    public int[] payCubeGunsight(){
        int[] array = {0, 0, 0};
        if(ammo.get(Color.RED) > 0)
            array[0] = 1;
        if(ammo.get(Color.YELLOW) > 0)
            array[1] = 1;
        if(ammo.get(Color.BLUE) > 0)
            array[2] = 1;
        return array;
    }

    /**
     * This function verifies if a PowerCard can be paid with cubes
     * @param power to be paid
     * @return true if possible, else false
     */
    public boolean canPayPowerUp(PowerCard power){
        if(ammo.get(power.getColor()) >= 1){
            ammo.replace(power.getColor(), ammo.get(power.getColor()) -1);
        return true;}
        else return false;
    }

    /**
     * This function verifies if Gunsight Power up can be paid and removes color Bullet from player
     * @param color to pay
     * @return true if possible, else false
     */
    public boolean canPayGunsight(Color color){
        if(ammo.get(color) >= 1){
            ammo.replace(color, ammo.get(color)-1);
            return true;}
        else return false;
    }

    /**
     * This function is used to print in the CLI the ammo a player has
     * @return a string
     */
    public String printPlayerAmmo(){

        return "Ammo: [R:" + ammo.get(RED) + ",Y:"+ ammo.get(YELLOW) + ",B:" + ammo.get(BLUE) + "]";
    }

    /**
     * This function is used to notify actions
     */
    public void notifyEndAction(){
        setChanged();
        notifyObservers(this.clonePlayer());
    }

}