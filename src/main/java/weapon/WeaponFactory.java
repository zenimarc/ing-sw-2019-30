package weapon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO ho commentato il main di questa classe e gli inizializzatori di deck nella classe Board
public class WeaponFactory {

    private static File weaponFolder;
    private final static String weaponResourcesAddress = "src"+File.separator+
            "resources"+File.separator+
            "weapon" + File.separator;

    public WeaponFactory() {
        weaponFolder = new File(weaponResourcesAddress);
    }

    /**
     *
     * @return
     */
    @Deprecated
    private static List<WeaponCard> loadWeaponCardsGSON() {
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

    private List<WeaponCard> loadWeaponCards(){
        List<WeaponCard> weaponCards = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        File folder = new File(weaponResourcesAddress);
        if(!folder.exists()) return weaponCards;

        for (File file : folder.listFiles()){
            try {
                weaponCards.add(
                        objectMapper.readValue(file, WeaponCard.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return weaponCards;
    }

    /**
     * Store json weapon in weponResourcesAddress if doesn't exist
     * @param jsonWeapon weapons's json String
     * @param weaponName file's name
     */
    private void storeWeapon(String jsonWeapon, String weaponName){
        File file = new File(weaponResourcesAddress +
                weaponName + ".json");

        if(file.exists()) return;

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

    /**
     * Store All weapon in  EnumWeapon in "weapon" in resoures
     */
    public void storeAllWeaponJackson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WeaponCard wp;

        File folder = new File(weaponResourcesAddress);
        if (!folder.exists()) folder.mkdir();

        for (EnumWeapon weapon : EnumWeapon.values()) {
            wp = null;

            if(EnumWeapon.AreaWeaponSet.contains(weapon)){
                wp = getAreaWeapon(weapon);
            }
            if(EnumWeapon.DistanceWeaponSet.contains(weapon)){
                wp = getDistanceWeapon(weapon);
            }
            if(EnumWeapon.MovementWeaponSet.contains(weapon)){
                wp = getMovementWeapon(weapon);
            }
            if(EnumWeapon.SimpleWeaponSet.contains(weapon))
                wp = getSimpleWeapon(weapon);

            if(wp !=null) {
                try {
                    storeWeapon(objectMapper.writeValueAsString(wp), weapon.getName());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private WeaponCard getSimpleWeapon(EnumWeapon type) {return new SimpleWeapon(type);}

    private WeaponCard getDistanceWeapon(EnumWeapon type) {
        return new DistanceWeapon(type);
    }

    private WeaponCard getAreaWeapon(EnumWeapon type) {
        return new AreaWeapon(type);
    }

    private WeaponCard getMovementWeapon(EnumWeapon type) {
        return new MovementWeapon(type);
    }

    public List<WeaponCard> getWeaponCardList(){
        return loadWeaponCards();
    }


    public static void main(String[] arg) {
        WeaponFactory factory = new WeaponFactory();

        factory.storeAllWeaponJackson();

        factory.getWeaponCardList().stream().forEach(x -> System.out.println(x+"\ttype: "+x.weaponType));
    }
}