package weapon;

import attack.Attack;
import attack.DistanceAttack;
import attack.SimpleAttack;
import board.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.EnumTargetSet;
import deck.Bullet;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumAttackName.*;
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
                attacks.add(new DistanceAttack(VISIBLE, BASE_ATTACK_NAME, 1,1,1,1,-1));
                alternativeAttack = new DistanceAttack(VISIBLE,HELLION_OPT1,1,2,-1,1,-1);
                alternativeAttack.setCost(new int[]{1,0,0});
                break;
            case SHOCKWAVE:
                attacks.add(new DistanceAttack(VISIBLE, BASE_ATTACK_NAME, 1, 0 , 3,1,1));
                alternativeAttack = new DistanceAttack(VISIBLE, SHOCKWAVE_OPT1, 1, 0 , -1,1,1);
                alternativeAttack.setCost(new int[]{0,1,0});
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

    /**
     *
     * @param typeAttack
     * @param shooter
     * @param opponents
     * @return
     */
    private boolean hellionShoot(int typeAttack, Player shooter, List<Player> opponents){

        Attack supportAttack = new SimpleAttack(SAME_CELL, SUPPORT_ATTACK, 0, typeAttack+1, -1);

        switch (typeAttack){
            case 0:
                attacks.get(0).attack(shooter,opponents.get(0));
                supportAttack.attack(shooter, opponents.subList(1,opponents.size()));
                break;
            case 1:
                alternativeAttack.attack(shooter,opponents.get(0));
                supportAttack.attack(shooter, opponents.subList(1,opponents.size()));
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell) {
        boolean result;
        switch (this.weaponType) {
            case WHISPER:
                result = attacks.get(0).attack(shooter,opponents.get(0));
                break;
            case HELLION:
                result = hellionShoot(typeAttack, shooter, opponents);
                break;
            case SHOCKWAVE:
                result= alternativeSimpleShoot(typeAttack, shooter, opponents);
                break;
            default:
                return false;
        }
        this.setNotLoaded();
        return result;
    }
}
