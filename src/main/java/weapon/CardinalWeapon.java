package weapon;

import attack.Attack;
import attack.CardinalAttack;
import attack.DistanceAttack;
import attack.SimpleAttack;
import board.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import deck.Bullet;
import player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static constants.EnumAttackName.*;
import static constants.EnumAttackName.SUPPORT_ATTACK;
import static controller.EnumTargetSet.*;

@JsonIgnoreProperties(ignoreUnknown = true)

//TODO serve davvero Cardinal attack? Si potrebbe trattare come distance attack per cui la massima distanza è la distanza entro cui può colpire altri target
public class CardinalWeapon extends WeaponCard{

    public CardinalWeapon(EnumWeapon weaponType){
        this.weaponType = weaponType;
        this.name = weaponType.getName();
        this.cost = weaponType.getCost();

        switch (weaponType){
            case FLAMETHROWER:
                baseAttack = new CardinalAttack(CARDINAL, FLAMETHROWER_BASE,1,0,2,1,2);
                alternativeAttack = new CardinalAttack(CARDINAL, FLAMETHROWER_OP1,2,0,-1,1,2);
                alternativeAttack.setCost(new int[]{0,2,0});
                break;
            case RAILGUN:
                baseAttack = new SimpleAttack(CARDINAL_WALL_BYPASS, RAILGUN_BASE, 3,0,1);
                alternativeAttack = new CardinalAttack(CARDINAL_WALL_BYPASS, RAILGUN_OP1,2,0,2,0, -1);

                break;
            case POWERGLOVE:
                baseAttack = new SimpleAttack(CARDINAL, POWERGLOVE_BASE, 1 , 2,1);
                alternativeAttack = new CardinalAttack(CARDINAL, POWERGLOVE_OP1, 2, 0 , 1,1,1);
                alternativeAttack.setCost(new int[]{0,0,1});
                break;
            default:
                //TODO ERROR
                break;
        }
    }

    @JsonCreator
    protected CardinalWeapon(@JsonProperty("name") String name,
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
     *
     * @param typeAttack
     * @param shooter
     * @param opponents
     * @return
     */
    private boolean flamethrowerShoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell){
        Attack supportAttack = new SimpleAttack(CARDINAL, SUPPORT_ATTACK, 1, 0, -1);

        switch (typeAttack){
            case 0:
                baseAttack.attack(shooter, opponents.get(0));
                supportAttack.attack(shooter, opponents.get(1));
                break;
            case 1:
                alternativeAttack.attack(shooter, flamethrowerSublist(opponents, opponents.get(0)));
                if(opponents.size() != 1)
                    if(flamethrowerSublist(opponents, opponents.get(opponents.size()-1)) != null)
                        supportAttack.attack(shooter, opponents.get(1));
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean railGunShoot(int typeAttack, Player shooter, List<Player> opponents){
        switch (typeAttack) {
            case 0:
                baseAttack.attack(shooter, opponents.get(0));
                break;
            case 1:
                alternativeAttack.attack(shooter, opponents);
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean powerGloveShoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell){
//la cella dello shooter diventa la stessa dell'ultimo attaccato
        switch (typeAttack) {
            case 0:
                baseAttack.attack(shooter, opponents.get(0));
                break;
            case 1:
                alternativeAttack.attack(shooter, opponents.get(0));
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
            case FLAMETHROWER:
                result = flamethrowerShoot(typeAttack, shooter, opponents, cell);
                break;
            case RAILGUN:
                result = railGunShoot(typeAttack, shooter, opponents);
                break;
            case POWERGLOVE:
                result = powerGloveShoot(typeAttack, shooter, opponents, cell);
                break;
            default:
                return false;
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

    private List<Player> flamethrowerSublist(List<Player> opponents, Player player){
        if(opponents.get(opponents.size()-1) == player && opponents.get(0).getCell() == player.getCell())
            return null;
        return opponents.stream().filter(x-> x.getCell() == player.getCell()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj.getClass()!=CardinalWeapon.class) return false;
        CardinalWeapon wc = (CardinalWeapon) obj;
        return this.getWeaponType().equals(wc.getWeaponType()) && this.isLoaded==((WeaponCard) obj).isLoaded;
    }
}
