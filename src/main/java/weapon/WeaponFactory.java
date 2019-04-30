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

            File file = new File("src/resources/weapon/" + weapon + ".json");

            try (PrintWriter writer = new PrintWriter(file)) {
                writer.write(jsonWeapon);
                writer.flush();
            } catch (IOException ioe) {
                ioe.fillInStackTrace();
                System.out.println(ioe.getLocalizedMessage());
            }catch (NullPointerException npe){
                System.out.println(npe.getLocalizedMessage());
            }

        }
    }

    private WeaponCard getFactory(enumWeapon type) {

        switch (type){
            case LOCKRIFLE:
                return new SimpleWeapon(enumWeapon.LOCKRIFLE);
            case MACHINEGUN:
                return  new SimpleWeapon(enumWeapon.MACHINEGUN);
            case ELECTROSCYTHE:
                return new SimpleWeapon(enumWeapon.ELECTROSCYTHE);
            case HEATSEEKER:
                return new SimpleWeapon(enumWeapon.HEATSEEKER);
            case TRACTOR_BEAM:
                return new MovementWeapon(enumWeapon.TRACTOR_BEAM);
        }
        return null;
    }

    public static void main(String[] arg){
        new WeaponFactory();
    }

}