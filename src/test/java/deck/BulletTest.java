package deck;

import constants.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static constants.Color.*;
import static constants.Color.BLUE;
import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    @Test
    void toIntArray() {
        ArrayList<Bullet> bullets1 = new ArrayList<>(Arrays.asList(
                new Bullet(Color.BLUE),
                new Bullet((Color.RED)),
                new Bullet(Color.YELLOW)
        ));

        int intBullets[] = Bullet.toIntArray(bullets1);
        for(int i=0;i<3;i++){
            assertEquals(intBullets[i], 1);
        }

        ArrayList<Bullet> bullets2 = new ArrayList<>(Arrays.asList(
                new Bullet(Color.BLUE),
                new Bullet((Color.RED))
        ));

        intBullets = Bullet.toIntArray(bullets2);
        assertEquals(intBullets[0], 1);
        assertEquals(intBullets[1], 0);
        assertEquals(intBullets[2], 1);


    }

}