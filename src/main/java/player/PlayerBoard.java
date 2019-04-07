package player;

import constants.Constants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 */
public class PlayerBoard {
    private static final int[] REWARDS_BY_DAMAGE = {8, 6, 4, 2, 1, 1};
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
    public PlayerBoard() {
        this.damageTrack = new ArrayList<>();
        this.marks = new HashMap<>();
        this.numDeaths = 0;
    }

    public void addDamage(Player player){
        if (damageTrack.size()<Constants.MAX_DAMAGE.getValue())
            damageTrack.add(player);
        //se ne ha già 12 non fa niente
    }
    public int getNumDamages(){
        return damageTrack.size();
    }

    /**
     * This function calculates the points to give
     * to each player who have inflicted damage to the current player.
     * it uses REWARDS_BY_DAMAGE array to determine how many points
     * to give based on damage count and order of infliction.
     * @return an HashMap containing players and points they should obtain.
     */
    public Map<Player, Integer> getPoints() {

        Map<Player, Integer> pointsMap = new HashMap<>();

        //assegna un punto al giocatore che ha colpito per primo
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
     * This function calculate the points to give to a player
     * based on his position in the list of the players ordered by damage
     * @param position position of the player in the list of players ordered by damage
     * @return points to assign to this player
     */
    private int getRewardPoints(int position){
        int[] rewardsByDamage = REWARDS_BY_DAMAGE;
        if ((position + numDeaths) > rewardsByDamage.length)
            return 1;
        else
            return rewardsByDamage[position + numDeaths];
    }


    /**
     * This function counts the damage inflicted by each player
     * and create an ordered list containing players ordered by damage.
     * in case of equal damage, it's already managed the damage priority.
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
                .map(x -> x.getKey())
                .collect(Collectors.toCollection(ArrayList::new));

    }
}