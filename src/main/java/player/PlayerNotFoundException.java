package player;

public class PlayerNotFoundException extends Exception {
    private Player player;
    public PlayerNotFoundException(Player player)
    {
        // Call constructor of parent Exception
        super();
        this.player = player;
    }

    @Override
    public String toString() {
        return "Player: "+player.toString()+" not found";
    }
}
