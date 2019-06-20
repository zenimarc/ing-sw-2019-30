package player;

import board.Board;
import constants.Constants;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  PlayerBoard contains all information about the situation of the player
 */
public class PlayerBoard implements Serializable {
    private static final int[] REWARDS_BY_DAMAGE = {8, 6, 4, 2, 1, 1};
    private static final int[] FRENZY_REWARDS_BY_DAMAGE = {2, 1, 1, 1};
    private ArrayList<Player> damageTrack;
    private HashMap<Player, Integer> marks;
    private int numDeaths;
    private boolean isDead;
    private boolean isFinalFrenzy;

    /**
     * Constructors
     */
    public PlayerBoard(){
        this.damageTrack = new ArrayList<>();
        this.marks = new HashMap<>();
        this.numDeaths = 0;
        this.isDead = false;
    }


    /**
     * This function returns the number of times the layer has died
     * @return the number of deaths
     */
    public int getNumDeaths() {
        return this.numDeaths;
    }

    public List<Player> getDamageTrack(){return this.damageTrack;}

    /**
     * This function returns the number of damage received by a player
     * @return the number of damage received
     */
    public int getNumDamages(){
        return this.damageTrack.size();
    }

    /**
     * This function returns the number of marks that have been put by the indicated player to this player board.
     * @param player player you want to know how many marks put
     * @return number of marks which have been put by indicated player
     */

    protected int getMarks(Player player){
        if (marks.get(player) != null)
            return marks.get(player);
        else
            return 0;
    }

    /**
     * This function returns a Map with all players and the marks they've given to this PlayerBoard
     * @return a Map<Player, Integer> with players and their marks.
     */
    public Map<Player, Integer> getMarks(){
        return this.marks;
    }

    /**
     * This function is a cycle for adding damage
     * @param player player who shot
     * @param num damage dealt by the shooter
     */

    protected void addDamage(Player player, int num){
        for (int i=0; i<num; i++){
            addDamage(player);
        }
    }

    /**
     * this function add a damage from the indicated player to this playerBoard
     * if there are marks from the indicated player they will be converted into damages
     * @param player who wants to add damage to this playerBoard
     */
    private void addDamage(Player player){
        if (damageTrack.size()<Constants.MAX_DAMAGE.getValue())
            damageTrack.add(player);
        //se ne ha già 12 non fa niente
        int marksToAdd = getMarks(player);
        for (int i=0; i<marksToAdd; i++) {
            if (damageTrack.size() < Constants.MAX_DAMAGE.getValue()) {
                damageTrack.add(player);
                removeMark(player);
            }
        }
        if(getNumDamages()==Constants.KILL_SHOOT.getValue()){
            isDead = true;
        }
    }

    /**
     * This function increase the number of deaths of the player
     */
    public void increaseNumDeath(){
        this.numDeaths++;
    }

    /**
     * This function adds a death to the PlayerBoard, (increment numDeath by 1)
     */
    public void addSkull(){
        this.numDeaths++;
    }

    /**
     * This function clears the damageTrack (useful when changing to frenzy mode)
     */
    public void clearDamages(){
        damageTrack.clear();
    }

    /**
     * This function is a cycle for adding marks
     * @param player the player who will give marks
     * @param marks number of marks given
     */
    protected void addMark(Player player, int marks){
        for (int i=0; i<marks; i++)
            this.addMark(player);
    }

    /**
     * This function adds a mark to the playerBoard from a specified player
     * only if it's possible to add another mark (marks < 3)
     * @param player the player who want to give a mark
     */
    private void addMark(Player player){
        Integer currentMarks = marks.putIfAbsent(player, 1);
        if (currentMarks != null && currentMarks<3){
            marks.replace(player, currentMarks+1);
        }
    }

    /**
     * This function removes a mark inflicted by a specific player
     * if marks are 0 the entry is removed from the map.
     * @param player who gave the mark
     */
    public void removeMark(Player player){
        marks.put(player, marks.get(player)-1);
        if (marks.get(player).equals(0))
            marks.remove(player);
    }

    /**
     * This function calculates the points to give
     * to each player who have inflicted damage to the current player.
     * It uses REWARDS_BY_DAMAGE array to determine how many points
     * to give based on damage count and order of infliction.
     * @return a HashMap containing players and points they should obtain.
     */
    public Map<Player, Integer> getPoints(boolean isFinalFrenzy) {

        Map<Player, Integer> pointsMap = new HashMap<>();

        //assegna un punto al giocatore che ha colpito per primo se non siamo in frenzy
        if (!isFinalFrenzy)
            pointsMap.putIfAbsent(damageTrack.get(0), 1 );

        //controlla i danni dei giocatori e assegna loro i punteggi
        Integer currentDamage;
        for (int i=0; i<getPlayersByDamage().size(); i++){
            currentDamage = pointsMap.putIfAbsent(getPlayersByDamage().get(i), getRewardPoints(i, isFinalFrenzy));
            if (currentDamage != null)
                pointsMap.replace(getPlayersByDamage().get(i), currentDamage + getRewardPoints(i, isFinalFrenzy));
        }
        return pointsMap;
    }

    /**
     * This function calculate the points to give to a player (checking if we're in frenzy mode)
     * based on his position in the list of the players, ordered by damage
     * @param position position of the player in the list of players ordered by damage
     * @return points to assign to this player
     */
    private int getRewardPoints(int position, boolean isFinalFrenzy){
        int[] rewardsByDamage;
        if (isFinalFrenzy)
            rewardsByDamage = FRENZY_REWARDS_BY_DAMAGE;
        else
            rewardsByDamage = REWARDS_BY_DAMAGE;

        if ((position + numDeaths) > rewardsByDamage.length -1)
            return 1;
        else
            return rewardsByDamage[position + numDeaths];
    }


    /**
     * This function counts the damage inflicted by each player
     * and creates an ordered list containing players ordered by damage.
     * In case of equal damage, the damage priority's already managed.
     * @return an ordered list of players by damage
     */
    private List<Player> getPlayersByDamage(){
        HashMap<Player, Integer> playersDamage = new HashMap<>();
        Integer currentDamage;
        for(Player player : damageTrack){
            currentDamage = playersDamage.putIfAbsent(player, 1); //aggiunge un player nella hashmap se non presente, altrimenti ritorna i danni che ha fatto per ora.
            if (currentDamage!=null){
                playersDamage.replace(player, currentDamage+1);
            }
        }

        return playersDamage.entrySet().stream()
                .sorted(Map.Entry.<Player, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey((a, b) -> damageTrack.indexOf(a) - damageTrack.indexOf(b))))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public String rewardPointstoString(){
        StringBuilder string = new StringBuilder();
        string.append("Points given: [");
        if(!isFinalFrenzy)
            for(int i = numDeaths; i < REWARDS_BY_DAMAGE.length; i++) {
                string.append(REWARDS_BY_DAMAGE[i]);
                if(i < REWARDS_BY_DAMAGE.length-1)
                    string.append(", ");
                else string.append("]");
            }
        else  for(int i = numDeaths; i < FRENZY_REWARDS_BY_DAMAGE.length; i++) {
            string.append(REWARDS_BY_DAMAGE[i]);
            if(i < FRENZY_REWARDS_BY_DAMAGE.length-1)
                string.append(", ");
            else string.append("]");
        }
        return string.toString();
    }

    /**
     * This clear damage track
     */
    public void resetDamage(){
        this.damageTrack.clear();
    }

    public boolean isDead(){return isDead;}

    public void resurrect(){
        isDead = false;
    }

    public void setFrenzy(boolean isFrenzy) {
        this.isFinalFrenzy = isFrenzy;
    }
}