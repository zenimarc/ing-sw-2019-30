package weapon;

import deck.Bullet;
import deck.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumWeapon {
    LOCK_RIFLE("Lockrifle", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE)))),
    WHISPER("Whishper", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE),new Bullet(Color.YELLOW)))),
    MACHINE_GUN("Machinegun", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE), new Bullet(Color.RED)))),
    ELECTROSCYTHE("Electroscyte", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE)))),
    HEATSEEKER("Heatseeker", new ArrayList<>(Arrays.asList(new Bullet(Color.RED),new Bullet(Color.RED),new Bullet(Color.YELLOW)))),
    TRACTOR_BEAM("Tractor beam",new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE))) ),
    VORTEX_CANNON("Vortex cannon",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.BLUE)))),
    ZX_2("ZX-2",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.RED))) ),
    HELLION("Hellion",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.YELLOW)))),
    SHOCKWAVE("Shockwave",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW)))),
    FURNACE("Furnace", new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.YELLOW))))
    ;


    private String name;
    private ArrayList<Bullet> cost;

    EnumWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return  name;}

    public List<Bullet> getCost() { return cost;}
}


