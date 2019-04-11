package attack;

import player.Player;

public class NormalAttack extends Attack {
    private int maxTargets = 0;
    private int minDistance = 0;

    public void fire() {
        for (Player player : players)
            player.getPlayerBoard().addDamage(getShooter(), this.damage);
        for (Player player : players)
            player.getPlayerBoard().addMark(getShooter(), this.marks);

    }
}
