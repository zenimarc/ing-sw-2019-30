package weapon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * WeaponFactory is a class used to store and load all WeaponCards
 */
public class WeaponFactory {

    private static final String WEAPON_RESOURCES_ADDRESS =
            "data"+File.separator+
            "weapon" + File.separator;

    /**
     * This function returns a list of weapon stored in WEAPON_RESOURCES_ADDRESS, if weapons don't exist it creates them
     * @return list of weapons stored in local
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
                e.fillInStackTrace();
            }
        }
        return weaponCards;
    }

    /**
     * Stores json weapon in weaponResourcesAddress if it doesn't exist
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
     * Stores all weapons in  EnumWeapon in"weapon" resources
     */
    public void storeAllWeapons() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        WeaponCard wp;

        File folder = new File(WEAPON_RESOURCES_ADDRESS);
        if (!folder.exists()) folder.mkdirs();

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

            if(EnumWeapon.CardinalWeaponSet.contains(weapon))
                wp = getCardinalWeapon(weapon);

            if(EnumWeapon.PriorityWeaponSet.contains(weapon))
                wp = getPriorityWeapon(weapon);

            if(wp !=null) {
                try {
                    storeWeapon(objectMapper.writeValueAsString(wp), weapon.getName());
                } catch (JsonProcessingException e) {
                    e.fillInStackTrace();
                }
            }
        }
    }

    /**
     * These functions returns the weapon wanted based on its Weapon type
     * @param type of weapon
     * @return a WeaponCard
     */
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

    private WeaponCard getCardinalWeapon(EnumWeapon type) {
        return new CardinalWeapon(type);
    }

    private WeaponCard getPriorityWeapon(EnumWeapon type) {
        return new PriorityWeapon(type);
    }

    /**
     * This function is used to load all Weapons
     * @return a List of weapons
     */
    public List<WeaponCard> getWeaponCardList(){

        List<WeaponCard> okCards = new ArrayList<>();
        okCards.add(new SimpleWeapon(EnumWeapon.LOCK_RIFLE));
        okCards.add(new SimpleWeapon(EnumWeapon.MACHINE_GUN));
        okCards.add(new SimpleWeapon(EnumWeapon.ELECTROSCYTHE));
        okCards.add(new SimpleWeapon(EnumWeapon.HEATSEEKER));
        okCards.add(new SimpleWeapon(EnumWeapon.ZX_2));

        okCards.add(new AreaWeapon(EnumWeapon.FURNACE));

        okCards.add(new DistanceWeapon(EnumWeapon.WHISPER));
        okCards.add(new DistanceWeapon(EnumWeapon.HELLION));
        okCards.add(new DistanceWeapon(EnumWeapon.SHOCK_WAVE));

        return okCards;
   //     return loadWeaponCardsJackson();
    }

    public static void main(String[] arg) {
        WeaponFactory factory = new WeaponFactory();

        factory.getWeaponCardList()
                .stream()
                .forEach(x -> System.out.println(x.toString().substring(0,3)+"\ttype: "+x.weaponType));
    }
}