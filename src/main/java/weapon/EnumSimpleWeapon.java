package weapon;

import deck.Bullet;
import deck.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumSimpleWeapon {

    LOCK_RIFLE("Lockrifle", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE)))),
    MACHINE_GUN("Machinegun", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE), new Bullet(Color.RED)))),
    ELECTROSCYTHE("Electroscyte", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE)))),
    HEATSEEKER("Heatseeker", new ArrayList<>(Arrays.asList(new Bullet(Color.RED),new Bullet(Color.RED),new Bullet(Color.YELLOW)))),
    ZX_2("ZX-2",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.RED))) );


    private String name;
    private ArrayList<Bullet> cost;

    EnumSimpleWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return  name;}

    public List<Bullet> getCost() { return cost;}
}
