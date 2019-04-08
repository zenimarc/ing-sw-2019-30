package player;

import board.Board;
import constants.Constants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 */
public class PlayerBoard {
    private static final int[] REWARDS_BY_DAMAGE = {8, 6, 4, 2, 1, 1};
    private static final int[] FRENZY_REWARDS_BY_DAMAGE = {2, 1, 1, 1};
    private Board board;
    //tiene la lista dei giocatori che hanno colpito
    private ArrayList<Player> damageTrack;
    /**
     * tiene per ogni giocatore i marker che ha inflitto
     */
    private HashMap<Player, Integer> marks;
    private int numDeaths;

    /**
     * Default constructor
     */
    public PlayerBoard(Board board) {
        this.board = board;
        this.damageTrack = new ArrayList<>();
        this.marks = new HashMap<>();
        this.numDeaths = 0;
    }

    public void addDamage(Player player){
        if (damageTrack.size()<Constants.MAX_DAMAGE.getValue())
            damageTrack.add(player);
        //se ne ha già 12 non fa niente
    }
    public int getNumDeaths() {
        return this.numDeaths;
    }

    /**
     * add a death to the PlayerBoard, (increment numDeath by 1)
     */
    public void addSkull(){
        this.numDeaths++;
    }

    public int getNumDamages(){
        return damageTrack.size();
    }

    /**
     * this function add a mark to the board from a specified player
     * @param player the player who want to give a mark
     */
    public void addMark(Player player){
        Integer currentMarks = marks.putIfAbsent(player, 1);
        if (currentMarks != null && currentMarks<3){
            marks.replace(player, currentMarks+1);
        }
    }

    public void addMark(Player player, int marks){
        for (int i=0; i<marks; i++)
            this.addMark(player);
    }

    /**
     * this function returns the number of marks that have been put by the indicated player to this player board.
     * @param player player you want to know how many marks put
     * @return number of marks which have been put by indicated player
     * @throws PlayerNotFoundException if the indicated player hasn't put any mark
     */

    public int getMarks(Player player) throws PlayerNotFoundException{
        if (marks.get(player) != null)
            return marks.get(player);
        else
            throw new PlayerNotFoundException(player);
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