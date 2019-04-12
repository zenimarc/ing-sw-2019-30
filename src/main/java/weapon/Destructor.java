package weapon;

import attack.Attack_;
import attack.SingleAttack;
import deck.Bullet;
import player.Player;

import java.util.ArrayList;
import java.util.List;

public class Destructor extends WeaponCard {
    private int maxTargets = 1;

    public Destructor(String name, List<Bullet> cost, List<Attack_> attacks) {
        super(name,cost, attacks);
    }


    public Destructor(){
        super(enumWeapon.DESTRUCTOR.getName(),enumWeapon.DESTRUCTOR.getCost());

        attacks.add(new SingleAttack("Effetto base",
                2,1));

        attacks.add(new SingleAttack("Secondo Aggancio",
                0,1));
    }


    @Override
    public boolean shoot(int idAttack, Player shooter, ArrayList<Player> opponents) {
        if(idAttack>=attacks.size()) return false;
        Attack_ attack = attacks.get(idAttack);
        Player opponent = opponents.get(0);

        if(shooter.canView(opponent)) {
            attack.attack(shooter, opponent);
            return true;
        }
        return false;
    }

}
