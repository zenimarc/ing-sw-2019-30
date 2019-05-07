package weapon;

import attack.DistanceAttack;
import attack.MoveAttack;
import board.Cell;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumString.*;

public class MovementWeapon extends WeaponCard {

    public MovementWeapon(EnumWeapon weaponType){
        this.weaponType = weaponType;

        switch (weaponType){
            case TRACTOR_BEAM:
                attacks.add(new MoveAttack(BASE_ATTACK_NAME,2,1));
                attacks.add(new MoveAttack(TRACTOR_BEAN_OPT1,2,3));
                break;
            case VORTEX_CANNON:
                attacks.add(new MoveAttack(BASE_ATTACK_NAME, 1,2));
                attacks.add(new MoveAttack(VORTEX_CANNON_OPT1, 1, 1,2));
                break;
            case SHOTGUN:
                attacks.add(new MoveAttack(BASE_ATTACK_NAME, 1,3));
                attacks.add(new DistanceAttack(SHOTGUN_OP1, 2,0,1,1,1));
                break;
                default:
                    //TODO ERROR
                    break;
        }
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
                attacks.get(0).attack(shooter,opponent,cell);
                break;
            case 1:
                attacks.get(1).attack(shooter,opponent,cell);
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
        attacks.get(0).attack(shooter,opponents.get(0),cell);
        if(typeAttack==1){
            attacks.get(1).attack(shooter,opponents.subList(1,2),cell);
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
                attacks.get(0).attack(shooter, opponents,cell.get());
                break;
            case 1:
                attacks.get(0).attack(shooter,opponents);
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
     * @return if shoot ok
     */
    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell) {
        switch (weaponType){
            case TRACTOR_BEAM:
                if(!cell.isPresent()) return false;
                return tractorBeamShoot(typeAttack, shooter,opponents.get(0), cell.get());
            case VORTEX_CANNON:
                if(!cell.isPresent()) return false;
                return vortexCannonShoot(typeAttack,shooter,opponents,cell.get());
            case SHOTGUN:
                return shotgunShoot(typeAttack,shooter,opponents,cell);
                default:
                    return false;
        }
    }
}
