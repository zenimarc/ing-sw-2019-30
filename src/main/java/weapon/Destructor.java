package weapon;

import attack.Attack_;
import attack.SimpleAttack;
import deck.Bullet;
import player.Player;

import java.util.List;

public class Destructor extends WeaponCard {

    public Destructor(String name, List<Bullet> cost, List<Attack_> attacks) {
        super(name,cost, attacks);
    }


    public Destructor(){
        super(enumWeapon.LOCKRIFLE.getName(),enumWeapon.LOCKRIFLE.getCost());

        attacks.add(new SimpleAttack("Effetto base", 2,1));

        attacks.add(new SimpleAttack("Secondo Aggancio", 0,1));

        attacks.get(1).setCost(new int[]{1,0,0});
    }

    /**
     * typeAttack == 0 => Base Attack
     * typeAttavk == 1 => Optional Attack
     * @param typeAttack
     * @param shooter
     * @param opponents
     * @return
     */

    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents) {
   /*     //If attack outOfRange or weapon is not loaded return false
        if(typeAttack>=attacks.size() || !isLoaded) return false;
        switch (typeAttack){
            case 0:
                //Base Attack
                attacks.get(0).attack(shooter,opponents.get(0));
                return true;
            case 1:
                //Optional Attack
            /*    if(shooter.useAmmo(attacks.get(1).getCost())) {
                    attacks.get(0).attack(shooter, opponents.get(0));
                    attacks.get(1).attack(shooter, opponents.get(1));
                    return true;
                }
                return false;
                default:
     */               return false;
       // }
    }

}
