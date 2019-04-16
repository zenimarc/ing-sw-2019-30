package weapon;

import attack.SimpleAttack;
import player.Player;

import java.util.List;

public class Gun extends WeaponCard{

    Gun(enumWeapon type){
        this.weaponType = type;

        switch (type){
            case LOCKRIFLE:
                attacks.add(new SimpleAttack("Effetto base", 2,1));
                attacks.add(new SimpleAttack("Secondo Aggancio", 0,1));
                attacks.get(1).setCost(new int[]{1,0,0});
                break;
            case MACHINEGUN:
                attacks.add(new SimpleAttack("Effetto base", 1,0,2));
                attacks.add(new SimpleAttack("Colpo focalizzato", 1,0));
                attacks.add(new SimpleAttack("Tripode di supporto", 1,0,2));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents) {
        if(typeAttack>=attacks.size()) return false;
        switch (this.weaponType) {
            case LOCKRIFLE:
                switch (typeAttack) {
                    case 0:
                        //Base Attack
                        attacks.get(0).attack(shooter, opponents.get(0));
                        return true;
                    case 1:
                        //Optional Attack
                        if (shooter.useAmmo(attacks.get(1).getCost())) {
                            attacks.get(0).attack(shooter, opponents.get(0));
                            attacks.get(1).attack(shooter, opponents.get(1));
                            return true;
                        }
                        break;
                    default:
                        return false;
                }
                break;
            case MACHINEGUN:
                break;

            default:
                return false;
        }
        return false;
    }
}
