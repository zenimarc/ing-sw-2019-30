package weapon;

import deck.Bullet;
import deck.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumMovementWeapon {

    TRACTOR_BEAM("Tractor beam",new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE))) ),
    VORTEX_CANNON("Vortex cannon",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.BLUE)))),
    SHOTGUN("Shotgun", new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW),new Bullet(Color.RED))) ),
    SLEDGEHAMMER("Sledgehammer",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW))))
    ;

    private String name;
    private ArrayList<Bullet> cost;

    EnumMovementWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return  name;}

    public List<Bullet> getCost() { return cost;}


}
