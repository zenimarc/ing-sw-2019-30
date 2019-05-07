package weapon;

import attack.DistanceAttack;
import attack.SimpleAttack;
import board.Cell;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumString.*;
import static controller.EnumTargetSet.*;

public class AreaWeapon extends WeaponCard {

    protected AreaWeapon(EnumWeapon weaponType){
        this.weaponType = weaponType;

        switch (weaponType) {
            case FURNACE:
                //TODO: NOT VISIBLE ROOM
                attacks.add(new SimpleAttack(VISIBLE, BASE_ATTACK_NAME,1,0,-1));
                attacks.add(new DistanceAttack(VISIBLE,FURNACE_OPT1,1,1,-1,1,1));
                break;
            default:
                //TODO ERROR
                break;
        }

    }


    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell) {

        switch (weaponType) {
            case FURNACE:
                return alternativeSimpleShoot(typeAttack,shooter,opponents);
            default:
                return false;
        }
    }
}
