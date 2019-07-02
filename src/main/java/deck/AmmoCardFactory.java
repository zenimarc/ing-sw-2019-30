package deck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AmmoCardFactory is used to create AmmoCards for the game
 */
public class AmmoCardFactory {
    private static final String ammoResourcesAddress = "src" + File.separator +
            "resources"+ File.separator +
            "cards" + File.separator +
            "ammocards.json";

    /**
     * This function creates an ammoCard deck from a Json file
     * @return an ammoCard deck
     */

    private static List<AmmoCard> ammoCardsJackson(){
       ObjectMapper objectMapper = new ObjectMapper();

       File ammoFile = new File(ammoResourcesAddress);
       if(!ammoFile.exists()){return Collections.emptyList();}

        try {
            return objectMapper.readValue(ammoFile, new TypeReference<ArrayList<AmmoCard>>(){});
        } catch (IOException e) {
            e.fillInStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * This function adds the remaining cards which where not saved in Json file since they were clones in ammoCard deck
     * @return definitive ammoCard deck
     */
    public List<AmmoCard> getAmmoCardList(){
        List<AmmoCard> ammoCard = ammoCardsJackson();
        for(int i = 0; i < 12; i++)
            ammoCard.add(ammoCard.get(i));
        for(int i = 0; i < 9; i++)
            ammoCard.add(ammoCard.get(i));
        for(int i = 6; i < 9; i++)
            ammoCard.add(ammoCard.get(i));
        return ammoCard;
    }
}

