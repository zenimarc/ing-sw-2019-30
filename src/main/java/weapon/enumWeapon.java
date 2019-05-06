package weapon;

import deck.Bullet;
import deck.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum enumWeapon {
    LOCKRIFLE("Lockrifle", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE)))),
    WHISPER("Whishper", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE),new Bullet(Color.YELLOW)))),
    MACHINEGUN("Machinegun", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE), new Bullet(Color.RED)))),
    ELECTROSCYTHE("Electroscyte", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE)))),
    HEATSEEKER("Heatseeker", new ArrayList<>(Arrays.asList(new Bullet(Color.RED),new Bullet(Color.RED),new Bullet(Color.YELLOW)))),
    TRACTOR_BEAM("Tractor beam",new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE))) ),
    VORTEX_CANNON("Vortex cannon",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.BLUE))));


    private String name;
    private ArrayList<Bullet> cost;

    enumWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return  name;}

    public List<Bullet> getCost() { return cost;}
}


