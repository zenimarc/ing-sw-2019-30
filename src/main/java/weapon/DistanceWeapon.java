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
/**
 * DistanceWeapon is class used for dealing with Weapons which can shoot from a certain distance
 */
public class DistanceWeapon extends WeaponCard {

    public DistanceWeapon(EnumWeapon weaponType){
        this.weaponType = weaponType;
        this.name = weaponType.getName();
        this.cost = weaponType.getCost();

        switch (weaponType){
            case WHISPER:
                baseAttack = new DistanceAttack(VISIBLE, WHISPER_BASE,3,1,1,2,-1);
                break;
            case HELLION:
                baseAttack = new DistanceAttack(VISIBLE, HELLION_BASE, 1,0,1,1,-1);
                alternativeAttack = new DistanceAttack(VISIBLE,HELLION_OPT1,1,0,1,1,-1);
                alternativeAttack.setCost(new int[]{1,0,0});
                break;
            case SHOCK_WAVE:
                baseAttack = new DistanceAttack(VISIBLE, SHOCK_WAVE_BASE, 1, 0 , 3,1,1);
                alternativeAttack = new DistanceAttack(VISIBLE, SHOCK_WAVE_OPT1, 1, 0 , -1,1,1);
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
                             @JsonProperty("baseAttack") Attack baseAttack,
                             @JsonProperty("alternativeAttack") Attack alternativeAttack,
                             @JsonProperty("type") EnumWeapon weaponType){
        this.name = name;
        this.cost = cost;
        this.attacks = attacks;
        this.baseAttack = baseAttack;
        this.alternativeAttack = alternativeAttack;
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
                baseAttack.attack(shooter,opponents.get(0));
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

    private boolean shockWaveShoot(int typeAttack, Player shooter, List<Player> opponents){
        switch (typeAttack) {
            case 0:
                if(opponents.stream().filter(x-> x!=null).map(Player::getCell).distinct().count()<opponents.stream().filter(x->x!=null).count()){
                    return false;
                }
                baseAttack.attack(shooter, opponents);
                break;
            case 1:
                alternativeAttack.attack(shooter, opponents);
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
                result = baseAttack.attack(shooter,opponents.get(0));
                break;
            case HELLION:
                result = hellionShoot(typeAttack, shooter, opponents);
                break;
            case SHOCK_WAVE:
                result = shockWaveShoot(typeAttack, shooter, opponents);
                break;
            default:
                return false;
        }

        if(result) {
            opponents.stream().filter(x -> x != null).forEach(Player::notifyEndAction);
            shooter.setNotLoadWeapon(this);
        }
        return result;
    }

    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cellMove, Optional<Cell> cell) {
        return false;
    }

    @Override
    public boolean shoot(Cell cell) {
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj.getClass()!=DistanceWeapon.class) return false;
        DistanceWeapon wc = (DistanceWeapon) obj;
        return this.getWeaponType().equals(wc.getWeaponType()) && this.isLoaded==((WeaponCard) obj).isLoaded;
    }
}
