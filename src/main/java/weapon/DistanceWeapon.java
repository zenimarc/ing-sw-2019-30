package weapon;

import attack.SimpleAttack;
import board.Cell;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumString.*;
public class DistanceWeapon extends WeaponCard {

    private int minDistance = -1;
    private int maxDistance = -1;

    public DistanceWeapon(enumWeapon weaponType){
        this.weaponType = weaponType;

        switch (weaponType){
            case WHISPER:
                minDistance = 2;
                attacks.add(new SimpleAttack(BASE_ATTACK_NAME,3,2,1));
                break;
            case HELLION:
                minDistance = 1;
                attacks.add(new SimpleAttack(BASE_ATTACK_NAME, 1,0,1));
                attacks.add(new SimpleAttack(HELLION_OPT1, 2,0,1));
                attacks.get(1).setCost(new int[]{1,0,0});
                attacks.add(new SimpleAttack(SUPPORT_ATTACK,0,1,-1));
                break;
            case SHOCKWAVE:
                minDistance = 1;
                maxDistance = 1;
                attacks.add(new SimpleAttack(BASE_ATTACK_NAME, 1, 0 , 3));
                attacks.add(new SimpleAttack(SHOCKWAVE_OPT1, 1, 0 , -1));
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
                attacks.get(2).attack(shooter,opponents);
                break;
            case 1:
                attacks.get(1).attack(shooter,opponents.get(0));
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
