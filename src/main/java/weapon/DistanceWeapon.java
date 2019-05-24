package weapon;

import attack.Attack;
import attack.DistanceAttack;
import board.Cell.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import deck.Bullet;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumString.*;
import static controller.EnumTargetSet.*;

@JsonIgnoreProperties(ignoreUnknown = true)

public class DistanceWeapon extends WeaponCard {

    public DistanceWeapon(EnumWeapon weaponType){
        this.weaponType = weaponType;
        this.name = weaponType.getName();
        this.cost = weaponType.getCost();

        switch (weaponType){
            case WHISPER:
                attacks.add(new DistanceAttack(VISIBLE,BASE_ATTACK_NAME,3,2,1,2,-1));
                break;
            case HELLION:
                attacks.add(new DistanceAttack(VISIBLE, BASE_ATTACK_NAME, 1,0,1,1,-1));
                attacks.add(new DistanceAttack(VISIBLE, SUPPORT_ATTACK, 0,1,-1,1,-1));
                attacks.add(new DistanceAttack(VISIBLE,HELLION_OPT1,0,2,-1,1,-1));
                attacks.get(2).setCost(new int[]{1,0,0});
                break;
            case SHOCKWAVE:
                attacks.add(new DistanceAttack(VISIBLE, BASE_ATTACK_NAME, 1, 0 , 3,1,1));
                attacks.add(new DistanceAttack(VISIBLE, SHOCKWAVE_OPT1, 1, 0 , -1,1,1));
                break;
            default:
                //TODO ERROR
                break;
        }
    }

    @JsonCreator
    protected DistanceWeapon(@JsonProperty("name") String name,
                             @JsonProperty("cost") List<Bullet> cost,
                             @JsonProperty("attacks")List<Attack> attacks,
                             @JsonProperty("type") EnumWeapon weaponType){
        this.name = name;
        this.cost = cost;
        this.attacks = attacks;
        this.weaponType = weaponType;
        this.isLoaded = false;
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
