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

/**
 * AreaWeapon is used to deal with Furnace WeaponCard particular implementation
 */
public class AreaWeapon extends WeaponCard {

    public AreaWeapon(EnumWeapon weaponType){
        this.weaponType = weaponType;
        this.name = weaponType.getName();
        this.cost = weaponType.getCost();

        switch (weaponType) {
            case FURNACE:
                baseAttack = new SimpleAttack(VISIBLE_ROOM, FURNACE_BASE,0,0,1);
                alternativeAttack = new DistanceAttack(VISIBLE,FURNACE_OPT1,1,1,1,1,1);
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
                         @JsonProperty("baseAttack") Attack baseAttack,
                         @JsonProperty("alternativeAttack") Attack alternativeAttack,
                         @JsonProperty("type") EnumWeapon weaponType){
        this.name = name;
        this.cost = cost;
        this.attacks = attacks;
        this.weaponType = weaponType;
        this.baseAttack = baseAttack;
        this.alternativeAttack = alternativeAttack;
        this.isLoaded = false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj.getClass()!=AreaWeapon.class) return false;
        AreaWeapon wc = (AreaWeapon) obj;
        return this.getWeaponType().equals(wc.getWeaponType()) && this.isLoaded==((WeaponCard) obj).isLoaded;
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
