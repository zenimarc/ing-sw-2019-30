package deck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class PowerCardFactory {
    public static void main(String args[]) {

        PowerCardFactory tester = new PowerCardFactory();
        for (int f = 0; f < 6; f++){
            try {
                PowerCard power = new PowerCard(new Bullet(Color.RED), PowerUp.GUNSIGHT);
                tester.writeJSON(power);
                PowerCard power1 = tester.readJSON();
                System.out.println(power1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void writeJSON(PowerCard power) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer = new FileWriter("src/resources/cards/powercards.json");
        writer.write(gson.toJson(power));
        writer.close();
    }

    private PowerCard readJSON() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/resources/cards/powercards.json"));
        PowerCard power = gson.fromJson(bufferedReader, PowerCard.class);
        return power;
    }
}
