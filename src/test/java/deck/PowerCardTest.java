package deck;

import constants.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import powerup.PowerCard;
import powerup.PowerUp;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void ammoTest(){
        int[] array = {1, 2, 0};
        AmmoCard ammo = new AmmoCard(array, true);
        assertTrue(ammo.verifyPowerUp());
        assertEquals(array, ammo.getAmmo());

        assertEquals(2, ammo.getSpecificAmmo(1));
        assertEquals(1, ammo.getSpecificAmmo(0));
    }

    @Test
    void powerTest(){
        PowerCard power = new PowerCard(new Bullet(Color.RED), PowerUp.TARGETING_SCOPE);
        assertEquals(Color.RED, power.getColor());
        assertEquals(PowerUp.TARGETING_SCOPE, power.getPowerUp());

    }
}