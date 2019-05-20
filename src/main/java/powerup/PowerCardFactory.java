package powerup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import deck.AmmoCard;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerCardFactory {
    private BufferedReader bufferedReader;
    private static final String powercardResourcesAddress = "src" + File.separator +
            "resources"+ File.separator +
            "cards" + File.separator +
            "powercards.json";

    private List<PowerCard> PowerCardJson() {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            bufferedReader = new BufferedReader(new FileReader(powercardResourcesAddress));
            return gson.fromJson(bufferedReader, new TypeToken<ArrayList<PowerCard>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.fillInStackTrace();
        } finally {
            try {bufferedReader.close();}
            catch(IOException ioe){
                ioe.fillInStackTrace();
            }
        }
        return Collections.emptyList();
    }

    private List<PowerCard> powerCardsJackson(){

        ObjectMapper objectMapper = new ObjectMapper();

        File powerUpFile = new File(powercardResourcesAddress);

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

    public static void main(String[] args) {

        ArrayList<PowerCard>  powerCards = (ArrayList<PowerCard>) new PowerCardFactory().getPowerCardsList();
        for (PowerCard p : powerCards){
            System.out.println(p);
        }
    }
}

