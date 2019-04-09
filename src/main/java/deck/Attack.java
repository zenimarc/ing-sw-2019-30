package deck;

import board.Board;
import player.Player;

import java.util.*;

/**
 * Il controllo dei danni è già presente in Playerboard
 * vari tipi di attacco, ad un arma possono essere associati più attacchi.
 * qui andranno tutti i possibili attacchi che troviamo nelle carte (quelli base, quelli a costo aggiuntivo ecc.)
 * TODO implementare funzione isvisible(player 1, player 2)
 */
public class Attack {
    private Player shooter;
    private int damage = 0;
    private ArrayList<Player> players;
    private boolean isvisible;
    private int max_distance = 0;
    private int min_distance = 0;
    private boolean isdamage_area;
    private int marks = 0;
    private int movement = 0;

    /**
     * Default constructor
     */
    public Attack() {
        this.shooter = null;
        players = new ArrayList<Player>();
    }

    public Attack(int damage, boolean is_visible, int max_d, int min_d, boolean is_area, int marks, int movement){
        this.damage = damage;
        this.isvisible = is_visible;
        this.max_distance = max_d;
        this.min_distance = min_d;
        this.isdamage_area = is_area;
        this.marks = marks;
        this.movement = movement;
    }

    public void damage_visible_attack(){
        //if(player è visibile)
        for(Player player : players)
            for(int i = 0; i < damage; i++)
                player.getPlayerBoard().addDamage(shooter);

    }


    public void add_mark(){

    }

    /**public boolean range(Player player){
        if(max_distance != min_distance)
            if(Billboard.canMove(this.shooter.getPawn().getCell(), player.getPawn().getCell(), max_distance) && !Billboard.canMove(this.shooter.getPawn().getCell(), player.getPawn().getCell(), max_distance))
                return true;
        else if(Board.getBillboard().canMove(this.shooter.getPawn().getCell(), player.getPawn().getCell(), max_distance))
            return true;
        return false;
    }

    public boolean is_visible(Player player){
        if(shooter.getPawn().getCell().getColor() == player.getPawn().getCell().getColor())
            return true;
        else if()
                return true;
        return false;
    }**/

}