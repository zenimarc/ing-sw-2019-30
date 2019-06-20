package powerup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerCardFactory {
    private static final String POWERCARD_RESOURCES_ADDRESS = "src" + File.separator +
            "resources"+ File.separator +
            "cards" + File.separator +
            "powercards.json";

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
                e.printStackTrace();
            }
        }
        return Collections.emptyList();

    }


    public List<PowerCard> getPowerCardsList(){
        return powerCardsJackson();
    }

}

