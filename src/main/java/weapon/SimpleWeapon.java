package weapon;

import attack.SimpleAttack;
import board.Cell;
import player.Player;

import java.util.List;
import java.util.Optional;

import static constants.EnumString.*;

public class SimpleWeapon extends WeaponCard{

    SimpleWeapon(enumWeapon type){
        this.weaponType = type;

        switch (type){
            case LOCKRIFLE:
                attacks.add(new SimpleAttack(BASE_ATTACK_NAME, 2,1));
                attacks.add(new SimpleAttack(LOCKRIFLE_OPT1, 0,1));
                attacks.get(1).setCost(new int[]{1,0,0});
                break;
            case MACHINEGUN:
                attacks.add(new SimpleAttack(BASE_ATTACK_NAME, 1,0,2));
                attacks.add(new SimpleAttack(MACHINEGUN_OP1, 1,0));
                attacks.add(new SimpleAttack(MACHINEGUN_OP2, 1,0,2));
                break;
            case ELECTROSCYTHE:
                attacks.add(new SimpleAttack(BASE_ATTACK_NAME, 1,0,-1));
                attacks.add(new SimpleAttack(ELECTROSCYHE_OPT1, 2,0,-1));
                break;
            case HEATSEEKER:
                attacks.add(new SimpleAttack(BASE_ATTACK_NAME, 3,0,1));
                break;
            case ZX_2:
                attacks.add(new SimpleAttack(BASE_ATTACK_NAME, 1,2,1));
                attacks.add(new SimpleAttack(ZX_2_OP1, 0,1,3));
            default:
                //TODO ERROR
                break;
        }
    }

    private boolean lockrifleShoot(int typeAttack, Player shooter, List<Player> opponents){
        attacks.get(0).attack(shooter, opponents.get(0));
        if(typeAttack==1){
            attacks.get(1).attack(shooter, opponents.get(1));
        }
        return true;
    }

    private boolean machinegunShoot(int typeAttack, Player shooter, List<Player> opponents){
        //Base Attack
        attacks.get(0).attack(shooter,opponents);

        switch (typeAttack){
            case 0:
                break;
            case 1:
                attacks.get(1).attack(shooter, opponents.get(0));
                break;
            case 2:
                attacks.get(2).attack(shooter,opponents.subList(1,2));
                break;
            case 12:
                attacks.get(1).attack(shooter, opponents.get(0));
                attacks.get(2).attack(shooter,opponents.subList(1,2));
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean electroscytheShoot(int typeAttack, Player shooter, List<Player> opponents){
        switch (typeAttack){
            case 0:
                attacks.get(0).attack(shooter,opponents);
                break;
            case 1:
                attacks.get(1).attack(shooter,opponents);
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean simpleShoot(Player shooter, Player opponent){
        attacks.get(0).attack(shooter,opponent);
        return true;
    }

    private boolean zx2Shoot(int typeAttack, Player shooter, List<Player> opponents){
        //Base Attack
        attacks.get(0).attack(shooter, opponents.get(0));
        //Optional Attack
        switch (typeAttack){
            case 0:
                break;
            case 1:
                attacks.get(1).attack(shooter, opponents);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell) {
        if(typeAttack>=attacks.size()) return false;
        switch (this.weaponType) {
            case LOCKRIFLE:
                return lockrifleShoot(typeAttack,shooter,opponents);
            case MACHINEGUN:
                return machinegunShoot(typeAttack,shooter,opponents);
            case ELECTROSCYTHE:
                return electroscytheShoot(typeAttack,shooter, opponents);
            case HEATSEEKER:
                return simpleShoot(shooter, opponents.get(0));
            case ZX_2:
                return zx2Shoot(typeAttack, shooter, opponents);
            default:
                return false;
        }
    }
}
