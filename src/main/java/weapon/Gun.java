package weapon;

import attack.AttackSinglePlayerInterface;
import attack.Attack_;
import player.Player;

import java.util.List;

public class Gun extends WeaponCard {

    public Gun(String name, int[] cost, List<Attack_> attacks) {
        super();
    }

    @Override
    public boolean shoot(Player shooter, Player opposite, int idAttack) {
        if(attacks.size()<=idAttack) return false;
        attacks.get(idAttack).attack(shooter,opposite);
        return false;
    }
}
