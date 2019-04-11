package cardAttack;

import attack.Attack;
import player.Player;

public class MovementAttack extends Attack {
    private int maxTargets = 0;
    private int minDistance = 0;
    private int movement = 0;

    public void fire() {
        //viene chiamata f per effettuare il movimento
        for (Player player : players)
            player.addDamage(getShooter(), this.damage);
        for (Player player : players)
            player.addMark(getShooter(), this.marks);

    }
}
