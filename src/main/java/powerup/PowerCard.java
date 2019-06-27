package powerup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import deck.Bullet;
import deck.Card;

import java.io.Serializable;
import java.util.Objects;

/**
 * PowerCard is the card which represent which ammo you can receive and if you can pick a power up
 */
public class PowerCard extends Card implements Serializable {

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

    /**
     * This function returns the Color of the PowerCard
     * @return color of PowerCard
     */
    public Color getColor(){
        return bullet.getColor();
    }

    /**
     * This function is used to print Power up and color of PowerCard for CLI
     * @return a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(cardType);
        sb.append(" (");
        sb.append(bullet.getColor().getAbbreviation());
        sb.append(")");

        return sb.toString();
    }

    /**
     * This function is used to create an image of a PowerCard for GUI
     * @return a string
     */
    @Override
    public String stringGUI() {
        StringBuilder sb = new StringBuilder();

        sb.append(cardType);
        sb.append(bullet.stringGUI());
        return sb.toString();
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj!=null) {
            if (obj.getClass().equals(PowerCard.class)) {
                PowerCard powerCard = (PowerCard) obj;
                return this.getColor().equals(powerCard.getColor()) && this.cardType.equals(powerCard.cardType);
            }
        }
        return false;
    }

    @Override //TODO vedere se va bene questo override di hashcode
    public int hashCode(){
        return Objects.hashCode(this);
    }
}