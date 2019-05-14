package weapon;

import deck.Bullet;
import deck.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumDistanceWeapon {

    WHISPER("Whishper", new ArrayList<>(Arrays.asList(new Bullet(Color.BLUE),new Bullet(Color.BLUE),new Bullet(Color.YELLOW)))),
    HELLION("Hellion",new ArrayList<>(Arrays.asList(new Bullet(Color.RED), new Bullet(Color.YELLOW)))),
    SHOCKWAVE("Shockwave",new ArrayList<>(Arrays.asList(new Bullet(Color.YELLOW))))
    ;

    private String name;
    private ArrayList<Bullet> cost;

    EnumDistanceWeapon(String name, ArrayList<Bullet> bullets){
        this.name = name;
        this.cost = bullets;
    }

    public String getName(){return  name;}

    public List<Bullet> getCost() { return cost;}

}
