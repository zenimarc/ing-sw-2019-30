package weapon;

import attack.MoveAttack;
import board.Cell;
import player.Player;

import java.util.List;

public class MovementWeapon extends WeaponCard {

    public MovementWeapon(enumWeapon weaponType){
        this.weaponType = weaponType;

        switch (weaponType){
            case TRACTOR_BEAM:
                attacks.add(new MoveAttack("Modalità base",2,1));
                attacks.add(new MoveAttack("Modalità Punitore",2,3));
                break;
                default:
                    //TODO ERROR
                    break;
        }
    }

    private boolean tractorBeamShot(int typeAttack, Player shooter, Player opponent, Cell cell){
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


    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Cell cell) {

        switch (weaponType){
            case TRACTOR_BEAM:
                return tractorBeamShot(typeAttack, shooter,opponents.get(0), cell);
        }

        return false;
    }
}
