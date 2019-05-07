package deck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerCardFactory {

    public List<PowerCard> PowerCardJson() {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/resources/cards/powercards.json"));
            String test = bufferedReader.readLine();
            ArrayList<PowerCard> power = gson.fromJson(test, new TypeToken<ArrayList<PowerCard>>() {
            }.getType());
            return power;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error reading file");
        }
        return Collections.emptyList();
    }
}

