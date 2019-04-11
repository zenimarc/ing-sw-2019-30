package attack;

import player.Player;

public class CardinalAttack extends Attack {
    private boolean isPierce;

    public void fire() {
        for (Player player : players)
            player.getPlayerBoard().addDamage(getShooter(), this.damage);
        for (Player player : players)
            player.getPlayerBoard().addMark(getShooter(), this.marks);

    }
}
