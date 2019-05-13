package deck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerCardFactory {
    private BufferedReader bufferedReader;

    public List<PowerCard> PowerCardJson() {
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
}

