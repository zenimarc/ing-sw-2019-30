package weapon;

import attack.Attack;
import attack.SimpleAttack;
import board.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import deck.Bullet;
import org.jetbrains.annotations.NotNull;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumAttackName.*;
import static controller.EnumTargetSet.*;

@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * SimpleWeapon is used for weapons' attacks which don't have particular effects
 */
public class SimpleWeapon extends WeaponCard{

    /**
     *Constructor
     */
    public SimpleWeapon(EnumWeapon type){
        this.weaponType = type;
        this.name = type.getName();
        this.cost = type.getCost();

        switch (weaponType){
            case LOCK_RIFLE:
                baseAttack = new SimpleAttack(VISIBLE, LOCK_RIFLE_BASE, 2,1,1);
                attacks.add(new SimpleAttack(VISIBLE, LOCK_RIFLE_OPT1, 0,1,2));
                attacks.get(0).setCost(new int[]{1,0,0});
                break;
            case MACHINE_GUN:
                baseAttack = new SimpleAttack(VISIBLE, MACHINE_GUN_BASE, 1,0,2);
                attacks.add(new SimpleAttack(VISIBLE, MACHINE_GUN_OP1, 1,0,2));
                attacks.get(0).setCost(new int[]{0,1,0});
                attacks.add(new SimpleAttack(VISIBLE, MACHINE_GUN_OP2, 1,0,3));
                attacks.get(1).setCost(new int[]{0,0,1});
                break;
            case ELECTROSCYTHE:
                baseAttack = new SimpleAttack(SAME_CELL, ELECTROSCYHE_BASE, 1,0,-1);
                alternativeAttack = new SimpleAttack(SAME_CELL, ELECTROSCYHE_OPT1, 2,0,-1);
                alternativeAttack.setCost(new int[]{1,0,1});
                break;
            case HEATSEEKER:
                baseAttack = new SimpleAttack(NOT_VISIBLE, HEATSEEKER_BASE, 3,0,1);
                break;
            case ZX_2:
                baseAttack = new SimpleAttack(VISIBLE, ZX_2_BASE, 1,2,1);
                alternativeAttack = new SimpleAttack(VISIBLE, ZX_2_OP1, 0,1,3);
                break;
            case THOR:
                baseAttack = new SimpleAttack(VISIBLE, THOR_BASE, 2,0,1);
                attacks.add(new SimpleAttack(VISIBLE, THOR_OPT1, 1,0,1));
                attacks.add(new SimpleAttack(VISIBLE, THOR_OPT2, 2,0,1));
                attacks.get(1).setCost(new int[]{0,0,1});
            default:
                //TODO ERROR
                break;
        }
    }

    @JsonCreator
    protected SimpleWeapon(@JsonProperty("name") String name,
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

    public EnumWeapon getType(){return this.weaponType;}


    /**
     * The following functions are used to deal with specific weapon attacks
     * @param typeAttack index of attack
     * @param shooter player who is shooting
     * @param opponents to be shot
     * @return true if attack was good, else false
     */
    private boolean lockrifleShoot(int typeAttack, Player shooter, @NotNull List<Player> opponents){

        switch (typeAttack) {
            case 0:
                baseAttack.attack(shooter, opponents.get(0));
                break;
            case 1:
                attacks.get(0).attack(shooter, opponents.get(1));
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean machinegunShoot(int typeAttack, Player shooter, List<Player> opponents){

        switch (typeAttack){
            case 0:
                baseAttack.attack(shooter,opponents.subList(0,2));
                break;
            case 1:
                attacks.get(0).attack(shooter, opponents.get(0));
                break;
            case 2:
                attacks.get(1).attack(shooter,opponents.subList(1,3));
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean thorShoot(int typeAttack, Player shooter, List<Player> opponents){

        switch (typeAttack){
            case 0:
                baseAttack.attack(shooter,opponents.get(0));
                break;
            case 1:
                attacks.get(0).attack(shooter, opponents.get(1));
                break;
            case 2:
                attacks.get(1).attack(shooter,opponents.get(2));
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj.getClass()!=SimpleWeapon.class) return false;
        SimpleWeapon wc = (SimpleWeapon) obj;
        return this.getWeaponType().equals(wc.getWeaponType()) && this.isLoaded==((WeaponCard) obj).isLoaded;
    }

    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell) {

        boolean result;

        switch (this.weaponType) {
            case LOCK_RIFLE:
                result = lockrifleShoot(typeAttack,shooter,opponents);
                break;
            case MACHINE_GUN:
                result = machinegunShoot(typeAttack,shooter,opponents);
                break;
            case ELECTROSCYTHE:
            case ZX_2:
                result = alternativeSimpleShoot(typeAttack,shooter, opponents);
                break;
            case HEATSEEKER:
                baseAttack.attack(shooter,opponents.get(0));
                result = true;
                break;
            case THOR:
                result = thorShoot(typeAttack, shooter, opponents);
                break;
            default:
                result = false;
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
}