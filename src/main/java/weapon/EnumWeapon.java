package weapon;

import deck.Bullet;
import constants.Color;

import java.util.*;

/**
 * EnumWeapon is used to save all Weapons' names and cost for base attack
 */

public enum EnumWeapon {
    //SimpleWeapon
    LOCK_RIFLE("Lockrifle", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE)))),
    MACHINE_GUN("Machinegun", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE), new Bullet(Color.RED)))),
    ELECTROSCYTHE("Electroscythe", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE)))),
    HEATSEEKER("Heatseeker", new ArrayList<>(Arrays.asList(new Bullet(Color.RED),new Bullet(Color.RED),new Bullet(Color.YELLOW)))),
    ZX_2("ZX-2",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.RED))) ),
    //AreaWeapon
    FURNACE("Furnace", new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.BLUE)))),
    //DistanceWeapon
    WHISPER("Whisper", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE),new Bullet(Color.YELLOW)))),
    HELLION("Hellion",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.YELLOW)))),
    SHOCK_WAVE("Shockwave",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW)))),
    //MovementWeapon
    TRACTOR_BEAM("Tractor beam",new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE))) ),
    VORTEX_CANNON("Vortex cannon",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.BLUE)))),
    SHOTGUN("Shotgun", new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.RED))) ),
    SLEDGEHAMMER("Sledgehammer",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW)))),

    //TODO aggiungere queste weapon
    //CardinalWeapon
    FLAMETHROWER("Flamethrower",new ArrayList<>(Arrays.asList(new Bullet(Color.RED)))),
    POWERGLOVE("Power glove",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.BLUE)))),
    RAILGUN("Railgun",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW), new Bullet(Color.YELLOW), new Bullet(Color.BLUE)))),

    //THOR può essere un simple attack particolare
    THOR("T.H.O.R.",new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE))) ),

    //PriorityWeapons
    CYBERBLADE("Cyberblade",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW), new Bullet(Color.RED)))),
    ROCKET_LAUNCHER("Rocket launcher",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.RED)))),
    GRENADE_LAUNCHER("Grenade launcher",new ArrayList<>(Arrays.asList(new Bullet(Color.RED)))),
    PLASMA_GUN("Plasma gun", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.YELLOW))) ),


    ;

    private String name;
    private ArrayList<Bullet> cost;

    protected static final Set<EnumWeapon> AreaWeaponSet = EnumSet.of(FURNACE);
    protected static final Set<EnumWeapon> DistanceWeaponSet = EnumSet.of(WHISPER, HELLION, SHOCK_WAVE);
    protected static final Set<EnumWeapon> MovementWeaponSet = EnumSet.of(TRACTOR_BEAM, VORTEX_CANNON, SHOTGUN, SLEDGEHAMMER);
    protected static final Set<EnumWeapon> SimpleWeaponSet = EnumSet.of(LOCK_RIFLE, MACHINE_GUN, ELECTROSCYTHE, HEATSEEKER, ZX_2, THOR);
    protected static final Set<EnumWeapon> CardinalWeaponSet = EnumSet.of(FLAMETHROWER, RAILGUN, POWERGLOVE);
    protected static final Set<EnumWeapon> PriorityWeaponSet = EnumSet.of(CYBERBLADE, ROCKET_LAUNCHER, PLASMA_GUN, GRENADE_LAUNCHER);


    EnumWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return name;}

    public List<Bullet> getCost() {return cost;}
}


