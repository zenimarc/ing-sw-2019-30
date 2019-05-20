package deck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AmmoCardFactory {
    private static BufferedReader bufferedReader;

    /**
     * This function creates an ammoCard deck from a Json file
     * @return an ammoCard deck
     */

   private static List<AmmoCard> ammoCardJson() {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            bufferedReader = new BufferedReader(new FileReader("src/resources/cards/ammocards.json"));
            return gson.fromJson(bufferedReader, new TypeToken<ArrayList<AmmoCard>>() {
            }.getType());

        } catch (FileNotFoundException e) {
            e.fillInStackTrace();
        }
    return Collections.emptyList();
    }

    /**
     * This function adds the remaining cards which where not saved in Json file since they were clones in ammoCard deck
     * @return definitive ammoCard deck
     */
    public List<AmmoCard> getAmmoCardList(){
        List<AmmoCard> ammoCard = ammoCardJson();
        for(int i = 0; i < 12; i++)
            ammoCard.add(ammoCard.get(i));
        for(int i = 0; i < 9; i++)
            ammoCard.add(ammoCard.get(i));
        for(int i = 6; i < 9; i++)
            ammoCard.add(ammoCard.get(i));
        return ammoCard;
    }

    /**
     * This function converts a List of ammoCards into a list of cards
     * @param ammoCards to be converted
     * @return a List of cards
     */
    public List<Card> ammoCardsToCards(List<AmmoCard> ammoCards){
        List<Card> cards = new ArrayList<>();
        for (AmmoCard ammoCard : ammoCards) {
            cards.add(ammoCard);
        }
        return cards;
    }

    /**
     * This function creates a powerCard deck from a Json file
     * @return a powerCard deck
     */
    public List<PowerCard> powerCardJson() {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            bufferedReader = new BufferedReader(new FileReader("src/resources/cards/powercards.json"));
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
    /**
     * This function adds the remaining cards which where not saved in Json file since they were clones in powerCard deck
     * @return a PowerUp deck
     */
    public List<PowerCard> getPowerCardList(){
        List<PowerCard> powerCard = powerCardJson();
        for(int i = 0; i < 12; i++)
            powerCard.add(powerCard.get(i));
        return powerCard;
    }

    /**
     * This function converts a List of PowerCards into a list of cards
     * @param powerCards to be converted
     * @return a List of cards
     */
    public List<Card> PowerCardsToCards(List<PowerCard> powerCards){
        List<Card> cards = new ArrayList<>();
        for (PowerCard powerCard : powerCards) {
            cards.add(powerCard);
        }
        return cards;
    }

}

