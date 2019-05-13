package deck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AmmoCardFactory {

   private static List<AmmoCard> ammoCardJson() {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/resources/cards/ammocards.json"));
            String test = bufferedReader.readLine();
            ArrayList<AmmoCard> ammo = gson.fromJson(test, new TypeToken<ArrayList<AmmoCard>>() {
            }.getType());

            return ammo;
        } catch (FileNotFoundException e) {
            e.fillInStackTrace();
        } catch (IOException ex) {
            System.out.println("Error reading file");
        }
    return Collections.emptyList();
    }

    public List<AmmoCard> getAmmoCardList(){
        return ammoCardJson();
    }
}

