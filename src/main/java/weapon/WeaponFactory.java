package weapon;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WeaponFactory {

    private WeaponFactory() {

        Gson gson = new Gson();
        ArrayList<String> jsonWeapons = new ArrayList<>();
        String jsonWeapon;


        for (enumWeapon weapon : enumWeapon.values()) {
            jsonWeapons.add(gson.toJson(getFactory(weapon)));
            jsonWeapon = gson.toJson(getFactory(weapon));

            PrintWriter writer = null;
            File file = new File("src/resources/weapon/" + weapon + ".json");

            try {
                writer = new PrintWriter(file);
                writer.write(jsonWeapon);
                writer.flush();
            } catch (IOException ioe) {
                ioe.fillInStackTrace();
                System.out.println(ioe.getLocalizedMessage());
            }finally {
                writer.close();
            }
    }
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
