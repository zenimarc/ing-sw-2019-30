package weapon;

import deck.Bullet;
import deck.Color;

import java.util.ArrayList;
import java.util.Arrays;

public enum enumWeapon {
    LOCKRIFLE("Lockrifle", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE)))),
    MACHINEGUN("Machinegun", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE), new Bullet(Color.RED)))),
    ELECTROSCYTHE("Electroscyte", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE))));


    private String name;
    private ArrayList<Bullet> cost;

    enumWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return  name;}

    public ArrayList<Bullet> getCost() { return cost;}
}

