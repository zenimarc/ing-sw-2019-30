package weapon;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;

public class WeaponFactory {

    private WeaponFactory(){

        Gson gson = new Gson();
        ArrayList<String> jsonWeapon = new ArrayList<>();

        for(enumWeapon weapon : enumWeapon.values()){
            jsonWeapon.add(gson.toJson(getFactory(weapon)));
        }

        try {
            for(String weapon : jsonWeapon) {
                Files.write(FileSystems.getDefault().getPath("weapon.json"), weapon.getBytes());
                System.out.println(weapon);
            }
        }catch (IOException ioe){ioe.fillInStackTrace();
            System.out.println(ioe.getLocalizedMessage());}

     /*   JsonReader reader = new JsonReader(new FileReader(filename));
        Review data = gson.fromJson(reader, Review.class);
        data.toScreen(); // prints to screen some values
       */
    }

    private WeaponCard getFactory(enumWeapon type) {

        switch (type){
            case LOCKRIFLE:
                return new Gun(enumWeapon.LOCKRIFLE);
            case MACHINEGUN:
                return  new Gun(enumWeapon.MACHINEGUN);
        }
        return null;
    }

    public static void main(String[] arg){
        new WeaponFactory();
    }

}
