package weapon;

import attack.MoveAttack;
import board.Cell;
import player.Player;

import java.util.List;
import java.util.Optional;

public class MovementWeapon extends WeaponCard {

    public MovementWeapon(enumWeapon weaponType){
        this.weaponType = weaponType;

        switch (weaponType){
            case TRACTOR_BEAM:
                attacks.add(new MoveAttack("Modalità base",2,1));
                attacks.add(new MoveAttack("Modalità Punitore",2,3));
                break;
            case VORTEX_CANNON:
                attacks.add(new MoveAttack("Modalità base", 1,2));
                attacks.add(new MoveAttack("Buco Nero", 1, 1,2));
                break;
                default:
                    //TODO ERROR
                    break;
        }
    }

    /**
     * Tractor beam shoot function
     * @param typeAttack
     * @param shooter
     * @param opponent
     * @param cell
     * @return
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
     * @param typeAttack
     * @param shooter
     * @param opponents
     * @param cell
     * @return
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
     * @param typeAttack
     * @param shooter
     * @param opponents
     * @param cell
     * @return
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
