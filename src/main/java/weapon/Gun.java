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

    private boolean lockrifleShot(int typeAttack, Player shooter, List<Player> opponents){
        //Base Attack
        attacks.get(0).attack(shooter, opponents.get(0));
        //Optional attack
        if(typeAttack==1) {
            if (shooter.useAmmo(attacks.get(1).getCost())) {
                attacks.get(1).attack(shooter, opponents.get(1));
                return true;
            } else return false;
        }
        return true;
    }

    private boolean machinegunShot(int typeAttack, Player shooter, List<Player> opponents){
        //Base Attack
        attacks.get(0).attack(shooter,opponents);

        //Optional attack
        switch (typeAttack){
            case 1:
                if(shooter.useAmmo(attacks.get(1).getCost())){
                    attacks.get(1).attack(shooter, opponents.get(0));
                }else return false;
                break;
            case 2:
                if(shooter.useAmmo(attacks.get(2).getCost())){
                    attacks.get(2).attack(shooter,opponents.subList(1,2));
                }else return false;
                break;
            case 12:
                if(shooter.canPay(attacks.get(1).getCost()) && shooter.canPay(attacks.get(2).getCost())){
                    machinegunShot(1,shooter,opponents);
                    machinegunShot(2,shooter,opponents);
                }else return false;
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents) {
        if(typeAttack>=attacks.size()) return false;
        switch (this.weaponType) {
            case LOCKRIFLE:
                return lockrifleShot(typeAttack,shooter,opponents);
            case MACHINEGUN:
                return machinegunShot(typeAttack,shooter,opponents);
            default:
                return false;
        }
    }
}
