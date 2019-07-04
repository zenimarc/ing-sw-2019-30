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
    private static final String ammoResourcesAddress =
            "data"+ File.separator +
            "cards" + File.separator;

    /**
     * This function creates an ammoCard deck from a Json file
     * @return an ammoCard deck
     */

    private static List<AmmoCard> ammoCardsJackson(){
       ObjectMapper objectMapper = new ObjectMapper();

       File ammoFile = new File(ammoResourcesAddress + "ammocards.json");

        //if folder doesn't exist store all weapon
        if(!ammoFile.exists()){storeAmmocards();}


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

    private static void storeAmmocards(){
        File folder = new File(ammoResourcesAddress);
        if (!folder.exists()) folder.mkdirs();

        File file = new File(ammoResourcesAddress + "ammocards.json");

        if(file.exists()) return;

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write("[{\"cubes\":[2,1,0], \"hasPowerUp\":false}, {\"cubes\":[2,0,1], \"hasPowerUp\":false}, {\"cubes\":[1,2,0], \"hasPowerUp\":false}, {\"cubes\":[0,2,1], \"hasPowerUp\":false}, {\"cubes\":[1,0,2], \"hasPowerUp\":false}, {\"cubes\":[0,1,2], \"hasPowerUp\":false}, {\"cubes\":[1,1,0], \"hasPowerUp\":true}, {\"cubes\":[1,0,1], \"hasPowerUp\":true}, {\"cubes\":[0,1,1], \"hasPowerUp\":true}, {\"cubes\":[2,0,0], \"hasPowerUp\":true}, {\"cubes\":[0,2,0], \"hasPowerUp\":true}, {\"cubes\":[0,0,2], \"hasPowerUp\":true}]");
            writer.flush();
        } catch (IOException ioe) {
            ioe.fillInStackTrace();
            System.out.println(ioe.getLocalizedMessage());
        }catch (NullPointerException npe){
            System.out.println(npe.getLocalizedMessage());
        }
    }

}

