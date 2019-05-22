package powerup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import deck.Bullet;
import deck.Card;

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

    @JsonCreator
    public PowerCard(@JsonProperty("bullet") Bullet bullet, @JsonProperty("cardType") PowerUp cardType) {
        this.bullet = bullet;
        this.cardType = cardType;
    }

    /**
     * This function returns the type of power up the card has
     * @return a power up
     */
    public PowerUp getPowerUp(){return this.cardType;}

    public Color getColor(){
        return bullet.getColor();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(cardType);
        sb.append(" (");
        sb.append(bullet.getColor().getAbbreviation());
        sb.append(")");

        return sb.toString();
    }
}