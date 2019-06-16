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

public class SimpleWeapon extends WeaponCard{

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
                baseAttack.setCost(new int[]{0,1,0});
                attacks.add(new SimpleAttack(VISIBLE, MACHINE_GUN_OP2, 1,0,3));
                attacks.get(1).setCost(new int[]{0,0,1});
                break;
            case ELECTROSCYTHE:
                baseAttack = new SimpleAttack(SAME_CELL, BASE_ATTACK_NAME, 1,0,-1);
                alternativeAttack = new SimpleAttack(SAME_CELL, ELECTROSCYHE_OPT1, 2,0,-1);
                alternativeAttack.setCost(new int[]{1,0,1});
                break;
            case HEATSEEKER:
                baseAttack = new SimpleAttack(NOT_VISIBLE, BASE_ATTACK_NAME, 3,0,1);
                break;
            case ZX_2:
                baseAttack = new SimpleAttack(VISIBLE, ZX_2_BASE, 1,2,1);
                alternativeAttack = new SimpleAttack(VISIBLE, ZX_2_OP1, 0,1,3);
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
            default:
                result = false;
        }

        this.setNotLoaded();
        return result;
    }
}