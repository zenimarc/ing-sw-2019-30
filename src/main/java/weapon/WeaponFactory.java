package weapon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WeaponFactory {

    private static final String WEAPON_RESOURCES_ADDRESS = "src"+File.separator+
            "resources"+File.separator+
            "weapon" + File.separator;

    /**
     * This return a list of weapon stored in WEAPON_RESOURCES_ADDRESS, if weapons don't exist create them
     * @return list of weapon stored in local
     */
    private List<WeaponCard> loadWeaponCardsJackson(){
        List<WeaponCard> weaponCards = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        File folder = new File(WEAPON_RESOURCES_ADDRESS);
        //if folder doesn't exist store all weapon
        if(!folder.exists()){storeAllWeapons();}

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
        File file = new File(WEAPON_RESOURCES_ADDRESS +
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
    public void storeAllWeapons() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WeaponCard wp;

        File folder = new File(WEAPON_RESOURCES_ADDRESS);
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

        return loadWeaponCardsJackson();
    }

    public static void main(String[] arg) {
        WeaponFactory factory = new WeaponFactory();


        factory.getWeaponCardList()
                .stream()
                .forEach(x -> System.out.println(x.toString().substring(0,3)+"\ttype: "+x.weaponType));
    }
}