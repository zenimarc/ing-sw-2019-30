package weapon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import deck.AmmoCard;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeaponFactory {

    private static File weaponFolder;

    public WeaponFactory() {
        weaponFolder = new File("src"+File.separator+
                "resources"+File.separator+
                "weapon" + File.separator);
    }


    private WeaponCard loadWeaponCardFromJSon(){

        WeaponCard weaponCard;







        return null;
    }


    private static List<WeaponCard> loadWeaponCards() {

        List<WeaponCard> weaponCards = new ArrayList<>();

        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            BufferedReader bufferedReader;

            for(final File folder : weaponFolder.listFiles()){
                if (folder.getName().equals(EnumWeaponType.SIMPLE_WEAPON.getName())){

                    for(final File weaponFile : folder.listFiles()){

                        bufferedReader = new BufferedReader(new FileReader(weaponFile));
                        weaponCards.add(gson.fromJson(bufferedReader, new TypeToken<SimpleWeapon>(){}.getType()));
                    }
                }
            }
            return weaponCards;

        } catch (FileNotFoundException e) {
            e.fillInStackTrace();
        }
        return Collections.emptyList();
    }

    public List<WeaponCard> getWeaponCardList(){
        return loadWeaponCards();
    }



    private void storeWeapon(String jsonWeapon, String weaponType, String weaponName){
        File file = new File("src"+File.separator+
                "resources"+File.separator+
                "weapon" + File.separator+
                weaponType + File.separator +
                weaponName + ".json");

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


    public void storeWeapons(){
        Gson gson = new Gson();
        File folder;

        for(EnumWeaponType type : EnumWeaponType.values()){

            folder = new File("src"+File.separator+
                    "resources"+File.separator+
                    "weapon" + File.separator+
                    type.getName());
            if(!folder.exists()) folder.mkdir();
        }

        for(EnumSimpleWeapon weapon : EnumSimpleWeapon.values()){
            storeWeapon(gson.toJson(getSimpleWeapon(weapon)), "SimpleWeapon", weapon.getName());
        }

        for(EnumDistanceWeapon weapon : EnumDistanceWeapon.values()){
            storeWeapon(gson.toJson(getDistanceWeapon(weapon)), "DistanceWeapon", weapon.getName());
        }

        for(EnumAreaWeapon weapon : EnumAreaWeapon.values()){
            storeWeapon(gson.toJson(getAreaWeapon(weapon)), "AreaWeapon", weapon.getName());
        }

        for(EnumMovementWeapon weapon : EnumMovementWeapon.values()){
            storeWeapon(gson.toJson(getMovementWeapon(weapon)), "MovementWeapon", weapon.getName());
        }

    }

    private WeaponCard getSimpleWeapon(EnumSimpleWeapon type) {
        return new SimpleWeapon(type);
    }

    private WeaponCard getDistanceWeapon(EnumDistanceWeapon type) {
        return new DistanceWeapon(type);
    }
    private WeaponCard getAreaWeapon(EnumAreaWeapon type) {
        return new AreaWeapon(type);
    }
    private WeaponCard getMovementWeapon(EnumMovementWeapon type) {
        return new MovementWeapon(type);
    }

    public static void main(String[] arg){
        WeaponFactory factory = new WeaponFactory();
        factory.getWeaponCardList().stream().forEach(x -> System.out.println(x));


    }

}