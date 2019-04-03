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
    //private Player[] damageTrack;
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
        //this.damageTrack = new Player[Constants.MAX_DAMAGE.getValue()];
        this.damageTrack = new ArrayList<>();
        this.marks = new HashMap<Player, Integer>();
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
     * Si occuperà del conteggio effettivo dei punti da assegnare ad ogni player (guardando il numDeath ecc), verrà divisa in sottofunzioni.
     */
    public HashMap<Player, Integer> getPoints() {

        HashMap<Player, Integer> pointsMap = new HashMap<>();

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

    private int getRewardPoints(int position){
        int[] rewardsByDamage = REWARDS_BY_DAMAGE;
        if ((position + numDeaths) > rewardsByDamage.length)
            return 1;
        else
            return rewardsByDamage[position + numDeaths];
    }
    //dovrebbe restituire un'arraylist ordinata dei player che hanno fatto più danno, già gestito il caso uguali danni chi ha fatto per primo.
    private List<Player> getPlayersByDamage(){
        HashMap<Player, Integer> playersDamage = new HashMap<>();
        Integer currentDamage;
        for(Player player : damageTrack){
            currentDamage = playersDamage.putIfAbsent(player, 1); //aggiunge un player nella hashmap se non presente, altrimenti ritorna i danni che ha fatto per ora.
            if (currentDamage!=null){
                playersDamage.replace(player, currentDamage+1);
            }
        }

        //a partire dalla hashmap giocatore-danni crea lista di merito dei player che ritorna
        List<Player> playersByDamage = playersDamage.entrySet().stream()
                .sorted(Map.Entry.<Player, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey((a, b) -> damageTrack.indexOf(a) - damageTrack.indexOf(b))))
                .map(x -> x.getKey())
                .collect(Collectors.toCollection(ArrayList::new));

        return playersByDamage;
    }
    public static void main(String[] args){
        Player p1 = new Player("Marco");
        Player p2 = new Player("Christian");
        Player p3 = new Player("Giovanni");
        Player p4 = new Player("Paolo");
        p1.getPlayerBoard().addDamage(p4);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p4);
        System.out.println(p1.getPlayerBoard().getPoints().toString());

    }
}