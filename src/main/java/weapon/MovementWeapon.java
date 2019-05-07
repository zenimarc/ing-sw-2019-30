package weapon;

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
     * @return if shoot ok
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
     * @return if shoot ok
     */
    private boolean vortexCannonShoot(int typeAttack, Player shooter, List<Player> opponents, Cell cell){
        attacks.get(0).attack(shooter,opponents.get(0),cell);
        if(typeAttack==1){
            attacks.get(1).attack(shooter,opponents.subList(1,2),cell);
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
        if(!cell.isPresent()) return false;

        switch (weaponType){
            case TRACTOR_BEAM:
                return tractorBeamShoot(typeAttack, shooter,opponents.get(0), cell.get());
            case VORTEX_CANNON:
                return vortexCannonShoot(typeAttack,shooter,opponents,cell.get());
                default:
                    return false;
        }
    }
}
