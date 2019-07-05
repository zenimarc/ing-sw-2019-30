package powerup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PowerCardFactory is used to create a deck of PowerCards
 */
public class PowerCardFactory {
    private static final String POWERCARD_RESOURCES_ADDRESS =
            "data"+ File.separator +
            "cards" + File.separator;

    /**
     * This function is used to create a PowerCard deck from a Jackson file
     * @return PowerCard deck
     */
    private List<PowerCard> powerCardsJackson(){

        ObjectMapper objectMapper = new ObjectMapper();

        File powerUpFile = new File(POWERCARD_RESOURCES_ADDRESS + "powercards.json");

        //if folder doesn't exist store all weapon
        if(!powerUpFile.exists()){storePowercards();}

        if(powerUpFile.exists()) {
            try {
                ArrayList<PowerCard> powerCardsList =
                        objectMapper.readValue(powerUpFile, new TypeReference<ArrayList<PowerCard>>() {});

                powerCardsList.addAll(objectMapper.readValue(powerUpFile, new TypeReference<ArrayList<PowerCard>>() {}));

                return powerCardsList;

            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }
        return Collections.emptyList();

    }

    /**
     * This function is used to get PowerCard deck
     * @return PowerCard deck
     */
    public List<PowerCard> getPowerCardsList(){
        return powerCardsJackson();
    }

    private void storePowercards(){
        File folder = new File(POWERCARD_RESOURCES_ADDRESS);
        if (!folder.exists()) folder.mkdirs();

        File file = new File(POWERCARD_RESOURCES_ADDRESS + "powercards.json");

        if(file.exists()) return;

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write("[{\"bullet\":{\"color\":\"RED\"},\"cardType\":\"TARGETING_SCOPE\"}, {\"bullet\":{\"color\":\"RED\"},\"cardType\":\"NEWTON\"}, {\"bullet\":{\"color\":\"RED\"},\"cardType\":\"TELEPORTER\"}, {\"bullet\":{\"color\":\"RED\"},\"cardType\":\"TAGBACK_GRENADE\"}, {\"bullet\":{\"color\":\"YELLOW\"},\"cardType\":\"TARGETING_SCOPE\"}, {\"bullet\":{\"color\":\"YELLOW\"},\"cardType\":\"NEWTON\"}, {\"bullet\":{\"color\":\"YELLOW\"},\"cardType\":\"TELEPORTER\"}, {\"bullet\":{\"color\":\"YELLOW\"},\"cardType\":\"TAGBACK_GRENADE\"}, {\"bullet\":{\"color\":\"BLUE\"},\"cardType\":\"TARGETING_SCOPE\"}, {\"bullet\":{\"color\":\"BLUE\"},\"cardType\":\"NEWTON\"}, {\"bullet\":{\"color\":\"BLUE\"},\"cardType\":\"TELEPORTER\"}, {\"bullet\":{\"color\":\"BLUE\"},\"cardType\":\"TAGBACK_GRENADE\"}]\n");
            writer.flush();
        } catch (IOException ioe) {
            ioe.fillInStackTrace();
            System.out.println(ioe.getLocalizedMessage());
        }catch (NullPointerException npe){
            System.out.println(npe.getLocalizedMessage());
        }
    }

}

