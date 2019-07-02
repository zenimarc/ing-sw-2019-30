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
    private static final String POWERCARD_RESOURCES_ADDRESS = "src" + File.separator +
            "resources"+ File.separator +
            "cards" + File.separator +
            "powercards.json";

    /**
     * This function is used to create a PowerCard deck from a Jackson file
     * @return PowerCard deck
     */
    private List<PowerCard> powerCardsJackson(){

        ObjectMapper objectMapper = new ObjectMapper();

        File powerUpFile = new File(POWERCARD_RESOURCES_ADDRESS);

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

}

