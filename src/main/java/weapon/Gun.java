package weapon;

import attack.SimpleAttack;
import player.Player;

import java.util.ArrayList;
import java.util.Arrays;
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
    public boolean shoot(int idAttack, Player shooter, List<Player> opponets) {
        return false;
    }
}
