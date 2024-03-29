package weapon;

import attack.Attack;
import attack.DistanceAttack;
import attack.MoveAttack;
import attack.SimpleAttack;
import board.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import deck.Bullet;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumAttackName.*;
import static controller.EnumTargetSet.*;


@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * MovementWeapon is a class which deals with weapon that can move other players
 */
public class MovementWeapon extends WeaponCard {

    public MovementWeapon(EnumWeapon type){
        this.weaponType = type;
        this.name = weaponType.getName();
        this.cost = weaponType.getCost();

        switch (weaponType){
            case TRACTOR_BEAM:
                baseAttack = new MoveAttack(VISIBLE, BASE_ATTACK_NAME,2,1,1);
                alternativeAttack = new MoveAttack(SAME_CELL,TRACTOR_BEAN_OPT1,2,3,1);
                alternativeAttack.setCost(new int[]{1,1,0});
                break;
            case VORTEX_CANNON:
                baseAttack = new MoveAttack(VISIBLE, BASE_ATTACK_NAME, 1,2,1);
                attacks.add(new MoveAttack(VISIBLE,VORTEX_CANNON_OPT1, 1, 1,2));
                attacks.get(0).setCost(new int[]{1,0,0});
                break;
            case SHOTGUN:
                baseAttack = new MoveAttack(SAME_CELL,BASE_ATTACK_NAME, 1,3,1);
                alternativeAttack = new DistanceAttack(VISIBLE,SHOTGUN_OP1, 2,0,1,1,1);
                break;
            case SLEDGEHAMMER:
                baseAttack = new SimpleAttack(SAME_CELL, BASE_ATTACK_NAME,2,0,1);
                alternativeAttack = new MoveAttack(SAME_CELL,SLEDGE_HAMMER,2,3,1);
                alternativeAttack.setCost(new int[]{1,0,0});
                break;
                default:
                    //TODO ERROR
                    break;
        }
    }

    @JsonCreator
    protected MovementWeapon(@JsonProperty("name") String name,
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
     * Tractor beam shoot function: move opponent in cell than shoot its
     * @param typeAttack type attack
     * @param shooter Player who shoot
     * @param opponent Player hit
     * @param cell final opponent cell
     * @return if hit true
     */
    private boolean tractorBeamShoot(int typeAttack, Player shooter, Player opponent, Cell cell){
        switch (typeAttack){
            case 0:
               baseAttack.attack(shooter,opponent,cell);
                break;
            case 1:
                alternativeAttack.attack(shooter,opponent,cell);
                break;
                default:
                    return false;
        }
        return true;
    }

    /**
     * Vortex Cannon shoot function
     * @param typeAttack type attack
     * @param shooter Player who shoot
     * @param opponents Players hit
     * @param cell final opponent cell
     * @return if hit true
     */
    private boolean vortexCannonShoot(int typeAttack, Player shooter, List<Player> opponents, Cell cell){
      baseAttack.attack(shooter,opponents.get(0),cell);
        if(typeAttack==1){
            baseAttack.attack(shooter,opponents.subList(1,2),cell);
        }
        return true;
    }

    /**
     *
     * @param typeAttack type attack
     * @param shooter player who shoot
     * @param opponents players hit
     * @param cell final opponent cell
     * @return if hit true
     */
    private  boolean shotgunShoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell){
        switch (typeAttack){
            case 0:
                if(!cell.isPresent()) return false;
                baseAttack.attack(shooter, opponents,cell.get());
                break;
            case 1:
                alternativeAttack.attack(shooter,opponents);
                break;
                default:
                    return false;
        }
        return true;
    }

    private boolean sledgehammerShot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell){
        switch (typeAttack) {
            case 0:
                baseAttack.attack(shooter,opponents);
                break;
            case 1:
                if(!cell.isPresent()) return false;
                alternativeAttack.attack(shooter,opponents,cell.get());
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Movement Weapon shoot
     * @param typeAttack type attack
     * @param shooter Player who shoot
     * @param opponents Player hit
     * @param cell final opponent cell
     * @return if hit return true
     */
    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell) {

        boolean result;

        switch (weaponType){
            case TRACTOR_BEAM:
                if(!cell.isPresent()) return false;
                result = tractorBeamShoot(typeAttack, shooter,opponents.get(0), cell.get());
                break;
            case VORTEX_CANNON:
                if(!cell.isPresent()) return false;
                result = vortexCannonShoot(typeAttack,shooter,opponents,cell.get());
                break;
            case SHOTGUN:
                result = shotgunShoot(typeAttack,shooter,opponents,cell);
                break;
            case SLEDGEHAMMER:
                result = sledgehammerShot(typeAttack,shooter,opponents,cell);
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


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj.getClass()!=MovementWeapon.class) return false;
        MovementWeapon wc = (MovementWeapon) obj;
        return this.getWeaponType().equals(wc.getWeaponType()) && this.isLoaded==((WeaponCard) obj).isLoaded;
    }
}
