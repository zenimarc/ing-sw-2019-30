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
    SHOCKWAVE("Shockwave",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW)))),
    //MovementWeapon
    TRACTOR_BEAM("Tractor beam",new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE))) ),
    VORTEX_CANNON("Vortex cannon",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.BLUE)))),
    SHOTGUN("Shotgun", new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.RED))) ),
    SLEDGEHAMMER("Sledgehammer",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW)))),
    ;

    private String name;
    private ArrayList<Bullet> cost;

    protected static final Set<EnumWeapon> AreaWeaponSet = EnumSet.of(FURNACE);
    protected static final Set<EnumWeapon> DistanceWeaponSet = EnumSet.of(WHISPER, HELLION, SHOCKWAVE);
    protected static final Set<EnumWeapon> MovementWeaponSet = EnumSet.of(TRACTOR_BEAM, VORTEX_CANNON, SHOTGUN, SLEDGEHAMMER);
    protected static final Set<EnumWeapon> SimpleWeaponSet = EnumSet.of(LOCK_RIFLE, MACHINE_GUN, ELECTROSCYTHE, HEATSEEKER, ZX_2);


    EnumWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return name;}

    public List<Bullet> getCost() {return cost;}
}


