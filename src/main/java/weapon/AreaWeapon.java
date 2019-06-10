package weapon;

import attack.Attack;
import attack.DistanceAttack;
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

public class AreaWeapon extends WeaponCard {

    protected AreaWeapon(EnumWeapon weaponType){
        this.weaponType = weaponType;
        this.name = weaponType.getName();
        this.cost = weaponType.getCost();

        switch (weaponType) {
            case FURNACE:
                //TODO: other room - gio
                attacks.add(new SimpleAttack(VISIBLE, BASE_ATTACK_NAME,1,0,-1));
                attacks.add(new DistanceAttack(VISIBLE,FURNACE_OPT1,1,1,-1,1,1));
                break;
            default:
                //TODO ERROR
                break;
        }

    }

    @JsonCreator
    protected AreaWeapon(@JsonProperty("name") String name,
                         @JsonProperty("cost") List<Bullet> cost,
                         @JsonProperty("attacks")List<Attack> attacks,
                         @JsonProperty("type") EnumWeapon weaponType){
        this.name = name;
        this.cost = cost;
        this.attacks = attacks;
        this.weaponType = weaponType;
        this.isLoaded = false;
    }

    @Override
    public boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell) {

        boolean result;

        switch (weaponType) {
            case FURNACE:
                result = alternativeSimpleShoot(typeAttack,shooter,opponents);
                break;
            default:
                result = false;
        }

        this.setNotLoaded();
        return result;
    }
}
