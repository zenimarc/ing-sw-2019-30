package deck;

import java.util.*;

/**
 * 
 */
public class PowerCard extends Card {

    private Bullet bullet;
    private PowerUp cardType;
    /**
     * Default constructor
     */
    public PowerCard() {
        this.bullet = null;
        this.cardType = null;
    }

    public PowerCard(Bullet bullet, PowerUp cardType) {
        this.bullet = bullet;
        this.cardType = cardType;
    }

}