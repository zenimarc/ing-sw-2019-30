package weapon;

import deck.Bullet;
import constants.Color;

import java.util.*;

public enum EnumWeapon {
    //SimpleWeapon
    LOCK_RIFLE("Lockrifle", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE)))),
    MACHINE_GUN("Machinegun", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE), new Bullet(Color.RED)))),
    ELECTROSCYTHE("Electroscythe", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE)))),
    HEATSEEKER("Heatseeker", new ArrayList<>(Arrays.asList(new Bullet(Color.RED),new Bullet(Color.RED),new Bullet(Color.YELLOW)))),
    ZX_2("ZX-2",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.RED))) ),
    //AreaWEapon
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
    THOR("T.H.O.R.",new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE))) ),
    CYBERBLADE("Cyberblade",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW), new Bullet(Color.RED)))),
    SHOCKWAVE("ShockWave", new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW))) ),
    RAILGUN("Railgun",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW), new Bullet(Color.YELLOW), new Bullet(Color.BLUE)))),
    ROCKET_LAUNCHER("Rocket Launcher",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.RED)))),
    GRENADE_LAUNCHER("Grenade Launcher",new ArrayList<>(Arrays.asList(new Bullet(Color.RED)))),
    PLASMA_GUN("Plasma gun", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.YELLOW))) ),
    FLAMETHROWER("Flamethrowerr",new ArrayList<>(Arrays.asList(new Bullet(Color.RED)))),
    POWERGLOVE("Cyber glove",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.BLUE)))),
    ;

    private String name;
    private ArrayList<Bullet> cost;

    protected static final Set<EnumWeapon> AreaWeaponSet = EnumSet.of(FURNACE);
    protected static final Set<EnumWeapon> DistanceWeaponSet = EnumSet.of(WHISPER, HELLION, SHOCK_WAVE);
    protected static final Set<EnumWeapon> MovementWeaponSet = EnumSet.of(TRACTOR_BEAM, VORTEX_CANNON, SHOTGUN, SLEDGEHAMMER);
    protected static final Set<EnumWeapon> SimpleWeaponSet = EnumSet.of(LOCK_RIFLE, MACHINE_GUN, ELECTROSCYTHE, HEATSEEKER, ZX_2);


    EnumWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return name;}

    public List<Bullet> getCost() {return cost;}
}


