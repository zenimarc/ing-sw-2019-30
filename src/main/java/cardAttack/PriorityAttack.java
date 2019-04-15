package cardAttack;

import attack.Attack;
import player.Player;

/**
 * soddisfa secondo effetto fucile al plasma
 */

public class PriorityAttack extends Attack {
    private int movement;
    private boolean doesDamage; //perchè esiste un attacco che ha priorità e fa danno

    public void fire() {
        if(doesDamage) //caso eccezionale
            for (Player player : players)
                player.addDamage(getShooter(), this.damage);
        else movement = 1; //il resto fa solo movimento
    }
}
