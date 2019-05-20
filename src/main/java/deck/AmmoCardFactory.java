package deck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AmmoCardFactory {

    private static BufferedReader bufferedReader;
    private static final String ammoResourcesAddress = "src" + File.separator +
            "resources"+ File.separator +
            "cards" + File.separator +
            "ammocards.json";

    private static List<AmmoCard> ammoCardsGson() {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            bufferedReader = new BufferedReader(new FileReader(ammoResourcesAddress));
            return gson.fromJson(bufferedReader, new TypeToken<ArrayList<AmmoCard>>() {
            }.getType());

        } catch (FileNotFoundException e) {
            e.fillInStackTrace();
        }
        return Collections.emptyList();
    }

    private static void storeAmmoCardsJackson() {
        //TODO ha senso fare load e store nella stessa factory - Gio'
    }

    private static List<AmmoCard> ammoCardsJackson(){
       ObjectMapper objectMapper = new ObjectMapper();

       File ammoFile = new File(ammoResourcesAddress);
       if(!ammoFile.exists()){return Collections.emptyList();}

        try {
            return objectMapper.readValue(ammoFile, new TypeReference<ArrayList<AmmoCard>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<AmmoCard> getAmmoCardList(){
        return ammoCardsJackson();
    }
}

