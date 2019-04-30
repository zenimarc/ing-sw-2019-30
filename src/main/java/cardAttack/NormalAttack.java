package cardAttack;

import attack.Attack;
import player.Player;

/** attacchi soddisfatti così:
 * distruttore tutti, mitragliatrice tutti(problema solo gestione bersagli), Torpedine, fucile al plasma, cecchino
 */
public class NormalAttack extends Attack {
    private int maxTargets = 0;
    private int minDistance = 0;

    public void fire() {
        for (Player player : players)
            player.addDamage(getShooter(), this.damage);
        for (Player player : players)
            player.addMark(getShooter(), this.marks);

    }
}