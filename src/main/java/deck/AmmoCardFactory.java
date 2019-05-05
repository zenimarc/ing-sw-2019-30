package deck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class AmmoCardFactory {
    public static void main(String args[]) {
        int i = 0, j = 1, z = 2;
        int[] array = {i, j, z};


        AmmoCardFactory tester = new AmmoCardFactory();
        for (int f = 0; f < 6; f++){
            try {
                AmmoCard ammo = new AmmoCard(array, false);
                tester.writeJSON(ammo);
                AmmoCard ammo1 = tester.readJSON();
                System.out.println(ammo1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void writeJSON(AmmoCard ammo) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer = new FileWriter("src/resources/cards/ammocards.json");
        writer.write(gson.toJson(ammo));
        writer.close();
    }

    private AmmoCard readJSON() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/resources/cards/ammocards.json"));
        AmmoCard ammo = gson.fromJson(bufferedReader, AmmoCard.class);
        return ammo;
    }
}
