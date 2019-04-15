package player;

import board.Board;
import constants.Constants;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  PlayerBoard contains all information about the situation of the player
 */
public class PlayerBoard {
    private static final int[] REWARDS_BY_DAMAGE = {8, 6, 4, 2, 1, 1};
    private static final int[] FRENZY_REWARDS_BY_DAMAGE = {2, 1, 1, 1};
    private Board board;
    private ArrayList<Player> damageTrack;
    private HashMap<Player, Integer> marks;
    private int numDeaths;

    /**
     * Constructors
     */
    public PlayerBoard(Board board) {
        this.board = board;
        this.damageTrack = new ArrayList<>();
        this.marks = new HashMap<>();
        this.numDeaths = 0;
    }


    /**
     * This function returns the number of times the layer has died
     * @return the number of deaths
     */
    public int getNumDeaths() {
        return this.numDeaths;
    }

    /**
     * This function returns the number of damage received by a player
     * @return the number of damage received
     */
    public int getNumDamages(){
        return damageTrack.size();
    }

    /**
     * This function returns the number of marks that have been put by the indicated player to this player board.
     * @param player player you want to know how many marks put
     * @return number of marks which have been put by indicated player
     */

    public int getMarks(Player player){
        if (marks.get(player) != null)
            return marks.get(player);
        else
            return 0;
    }

    /**
     * This function returns the boardof the player
     * @return the board of the player
     */

    public Board getBoard(){return this.board;}

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
            if (damageTrack.size()<Constants.MAX_DAMAGE.getValue())
                damageTrack.add(player);
            removeMark(player);
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
        if (this.board.getSkulls() > 0)
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
     * @param player who gave the mark
     */
    public void removeMark(Player player){
        marks.put(player, marks.get(player)-1);
    }

    /**
     * This function calculates the points to give
     * to each player who have inflicted damage to the current player.
     * It uses REWARDS_BY_DAMAGE array to determine how many points
     * to give based on damage count and order of infliction.
     * @return a HashMap containing players and points they should obtain.
     */
    public Map<Player, Integer> getPoints() {

        Map<Player, Integer> pointsMap = new HashMap<>();

        //assegna un punto al giocatore che ha colpito per primo se non siamo in frenzy
        if (!board.isFinalFrenzy())
            pointsMap.putIfAbsent(damageTrack.get(0), 1 );

        //controlla i danni dei giocatori e assegna loro i punteggi
        Integer currentDamage;
        for (int i=0; i<getPlayersByDamage().size(); i++){
            currentDamage = pointsMap.putIfAbsent(getPlayersByDamage().get(i), getRewardPoints(i));
            if (currentDamage != null)
                pointsMap.replace(getPlayersByDamage().get(i), currentDamage + getRewardPoints(i));
        }
        return pointsMap;
    }

    /**
     * This function calculate the points to give to a player (checking if we're in frenzy mode)
     * based on his position in the list of the players, ordered by damage
     * @param position position of the player in the list of players ordered by damage
     * @return points to assign to this player
     */
    private int getRewardPoints(int position){
        int[] rewardsByDamage;
        if (board.isFinalFrenzy())
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
}