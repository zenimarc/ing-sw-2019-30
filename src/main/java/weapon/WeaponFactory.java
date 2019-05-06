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

            File file = new File("src"+File.separator+
                    "resources"+File.separator+
                    "weapon" + File.separator
                    + weapon + ".json");

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
            case LOCK_RIFLE:
                return new SimpleWeapon(enumWeapon.LOCK_RIFLE);
            case MACHINE_GUN:
                return  new SimpleWeapon(enumWeapon.MACHINE_GUN);
            case ELECTROSCYTHE:
                return new SimpleWeapon(enumWeapon.ELECTROSCYTHE);
            case HEATSEEKER:
                return new SimpleWeapon(enumWeapon.HEATSEEKER);
            case TRACTOR_BEAM:
                return new MovementWeapon(enumWeapon.TRACTOR_BEAM);
                default:
                    return null;
        }
    }

    public static void main(String[] arg){
        new WeaponFactory();
    }

}