package weapon;

import deck.Bullet;
import deck.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum enumWeapon {
    LOCKRIFLE("Distruttore", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE)))),
    MACHINEGUN("Mitragliatrice", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE), new Bullet(Color.RED))));


    private String name;
    private ArrayList<Bullet> cost;

    enumWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return  name;}

    public ArrayList<Bullet> getCost() { return cost;}
}


