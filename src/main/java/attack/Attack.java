package attack;

import player.Player;

import java.util.*;

/**
 * TODO verificare che per ogni attacco dell'arma si soddisfino le condizioni nella rispettiva categoria
 * TODO Implementare la funzione della scelta  della scelta degli attacchi e dei  bersagli
 */
public abstract class Attack {
    protected Player shooter;
    protected int damage = 0;
    protected ArrayList<Player> players;
    protected boolean isvisible;
    protected int marks = 0;
    protected boolean isOptional;
    protected boolean isAlternative;
    protected boolean isRepetitive;

    /**
     * Default constructor
     */
    public Attack() {
        this.shooter = null;
        players = new ArrayList<Player>();
    }

    public Attack(int damage, boolean is_visible, int marks){
        this.damage = damage;
        this.isvisible = is_visible;
        this.marks = marks;
    }

    public Player getShooter(){return this.shooter;}

    public boolean getOptional(){return this.isOptional;}

    public boolean getAlternative(){return this.isAlternative;}
}