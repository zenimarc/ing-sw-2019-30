package attack;

import player.Player;

public class CardinalAttack extends Attack {
    private boolean isPierce;

    public void fire() {
        for (Player player : players)
            player.addDamage(getShooter(), this.damage);
        for (Player player : players)
            player.addMark(getShooter(), this.marks);

    }
}
