package deck;

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
     * This function returns the type of power up the card has
     * @return a power up
     */
    public PowerUp getPowerUp(){return this.cardType;}

    /**
     * This function returns the bullet which characterizes the Power Card
     * @return a bullet
     */
    public Bullet getBullet(){return this.bullet;}
}