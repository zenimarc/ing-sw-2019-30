package deck;

import java.util.*;

/**
 * PowerCard is the card which represent which ammo you can receive and if you can pick a power up
 */
public class PowerCard extends Card {

    private Bullet bullet;
    private PowerUp cardType;
    /**
     * Constructors
     */
    public PowerCard() {
        this(null, null);
    }

    public PowerCard(Bullet bullet, PowerUp cardType) {
        this.bullet = bullet;
        this.cardType = cardType;
    }

    /**
     * End constructors
     */

}