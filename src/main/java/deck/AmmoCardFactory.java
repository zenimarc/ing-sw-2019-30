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

    public List<AmmoCard> getAmmoCardList(){
        return ammoCardJson();
    }
}

