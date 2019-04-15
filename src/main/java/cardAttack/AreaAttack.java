package cardAttack;

import attack.Attack;
import player.Player;

public class AreaAttack extends Attack {
    private int maxDistance = 0;

    public void fire() {
        for (Player player : players)
            player.addDamage(getShooter(), this.damage);
        for (Player player : players)
            player.addMark(getShooter(), this.marks);

    }
}
