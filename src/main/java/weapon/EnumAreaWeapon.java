package weapon;

import deck.Bullet;
import deck.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumAreaWeapon {


    FURNACE("Furnace", new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.YELLOW))));


    private String name;
    private ArrayList<Bullet> cost;

    EnumAreaWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return  name;}

    public List<Bullet> getCost() { return cost;}
}
