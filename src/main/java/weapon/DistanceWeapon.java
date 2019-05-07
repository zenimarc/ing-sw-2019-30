package weapon;

import attack.DistanceAttack;
import attack.SimpleAttack;
import board.Cell;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumString.*;
public class DistanceWeapon extends WeaponCard {

    public DistanceWeapon(EnumWeapon weaponType){
        this.weaponType = weaponType;

        switch (weaponType){
            case WHISPER:
                attacks.add(new DistanceAttack(BASE_ATTACK_NAME,3,2,1,2,-1));
                break;
            case HELLION:
                attacks.add(new DistanceAttack(BASE_ATTACK_NAME, 1,0,1,1,-1));
                attacks.add(new DistanceAttack(SUPPORT_ATTACK, 0,1,-1,1,-1));
                attacks.add(new DistanceAttack(HELLION_OPT1,0,2,-1,1,-1));
                attacks.get(2).setCost(new int[]{1,0,0});
                break;
            case SHOCKWAVE:
                attacks.add(new DistanceAttack(BASE_ATTACK_NAME, 1, 0 , 3,1,1));
                attacks.add(new DistanceAttack(SHOCKWAVE_OPT1, 1, 0 , -1,1,1));
                break;
            default:
                //TODO ERROR
                break;
        }
    }

    private boolean hellionShoot(int typeAttack, Player shooter, List<Player> opponents){
        switch (typeAttack){
            case 0:
                attacks.get(0).attack(shooter,opponents.get(0));
                attacks.get(1).attack(shooter,opponents);
                break;
            case 2:
                attacks.get(0).attack(shooter,opponents.get(0));
                attacks.get(2).attack(shooter,opponents);
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell) {

        switch (this.weaponType) {
            case WHISPER:
                attacks.get(0).attack(shooter,opponents.get(0));
                return true;
            case HELLION:
                return hellionShoot(typeAttack, shooter, opponents);
            case SHOCKWAVE:
                return alternativeSimpleShoot(typeAttack, shooter, opponents);
            default:
                return false;
        }
    }
}
