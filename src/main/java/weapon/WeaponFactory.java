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

    public WeaponFactory() {
        weaponFolder = new File("src"+File.separator+
                "resources"+File.separator+
                "weapon" + File.separator);
    }


    private WeaponCard loadWeaponCardFromJSon(){

        WeaponCard weaponCard;

        return null;
    }


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

        File folder = new File("src"+File.separator+
                "resources"+File.separator+
                "weapon" + File.separator+
                "test_jackson");
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

    public List<WeaponCard> getWeaponCardList(){
        return loadWeaponCards();
    }



    private void storeWeapon(String jsonWeapon, String weaponFolderName, String weaponName){
        File file = new File("src"+File.separator+
                "resources"+File.separator+
                "weapon" + File.separator+
                weaponFolderName + File.separator +
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


        public void storeWeaponJackson(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File folder = new File("src"+File.separator+
                    "resources"+File.separator+
                    "weapon" + File.separator+
                    "test_jackson");
        if(!folder.exists()) folder.mkdir();

        for(EnumSimpleWeapon weapon : EnumSimpleWeapon.values()) {
            try {
                storeWeapon(objectMapper.writeValueAsString(getSimpleWeapon(weapon)),
                        "test_jackson", weapon.getName());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
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

    public static void main(String[] arg) {
        WeaponFactory factory = new WeaponFactory();
        //   factory.getWeaponCardList().stream().forEach(x -> System.out.println(x));
        factory.storeWeaponJackson();
        System.out.println(factory.loadWeaponCards().size());

    }

}