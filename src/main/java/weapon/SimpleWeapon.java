package weapon;

import attack.Attack;
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

public class SimpleWeapon extends WeaponCard{

    public SimpleWeapon(EnumWeapon type){
        this.weaponType = type;
        this.name = type.getName();
        this.cost = type.getCost();

        switch (weaponType){
            case LOCK_RIFLE:
                attacks.add(new SimpleAttack(VISIBLE, BASE_ATTACK_NAME, 2,1,1));
                attacks.add(new SimpleAttack(VISIBLE, LOCK_RIFLE_OPT1, 0,1,1));
                attacks.get(1).setCost(new int[]{1,0,0});
                break;
            case MACHINE_GUN:
                attacks.add(new SimpleAttack(VISIBLE, BASE_ATTACK_NAME, 1,0,2));
                attacks.add(new SimpleAttack(VISIBLE, MACHINE_GUN_OP1, 1,0,1));
                attacks.get(1).setCost(new int[]{0,1,0});
                attacks.add(new SimpleAttack(VISIBLE, MACHINE_GUN_OP2, 1,0,2));
                attacks.get(2).setCost(new int[]{0,0,1});
                break;
            case ELECTROSCYTHE:
                attacks.add(new SimpleAttack(SAME_CELL, BASE_ATTACK_NAME, 1,0,-1));
                attacks.add(new SimpleAttack(SAME_CELL, ELECTROSCYHE_OPT1, 2,0,-1));
                attacks.get(0).setCost(new int[]{1,0,1});
                break;
            case HEATSEEKER:
                attacks.add(new SimpleAttack(NOT_VISIBLE, BASE_ATTACK_NAME, 3,0,1));
                break;
            case ZX_2:
                attacks.add(new SimpleAttack(VISIBLE, BASE_ATTACK_NAME, 1,2,1));
                attacks.add(new SimpleAttack(VISIBLE, ZX_2_OP1, 0,1,3));
                break;
            default:
                //TODO ERROR
                break;
        }
    }

    @JsonCreator
    protected SimpleWeapon(@JsonProperty("name") String name,
                           @JsonProperty("cost") List<Bullet> cost,
                           @JsonProperty("attacks")List<Attack> attacks,
                           @JsonProperty("type") EnumWeapon weaponType){
        this.name = name;
        this.cost = cost;
        this.attacks = attacks;
        this.weaponType = weaponType;
        this.isLoaded = false;
    }

    public EnumWeapon getType(){return this.weaponType;}


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

        boolean result;

        if(typeAttack>=attacks.size()) return false;
        switch (this.weaponType) {
            case LOCK_RIFLE:
                result = lockrifleShoot(typeAttack,shooter,opponents);
                break;
            case MACHINE_GUN:
                result = machinegunShoot(typeAttack,shooter,opponents);
                break;
            case ELECTROSCYTHE:
                result = alternativeSimpleShoot(typeAttack,shooter, opponents);
                break;
            case HEATSEEKER:
                attacks.get(0).attack(shooter,opponents.get(0));
                result = true;
                break;
            case ZX_2:
                result = zx2Shoot(typeAttack, shooter, opponents);
                break;
            default:
                result = false;
        }

        this.setNotLoaded();
        return result;
    }
}