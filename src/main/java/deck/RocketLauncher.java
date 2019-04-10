package deck;

import player.Player;

public class RocketLauncher extends Attack{

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere vsisibile e non nella tua cella
        players.get(0).getPlayerBoard().addDamage(players.get(0), 2);
        //chiama una f per muovere il bersaglio colpito di 1 movimento
    }

    public void priorityMove(){
        //chiama una funzione per selezionare dove muoversi ed ha effetto prima o dopo effetto base
    }

    public void optionalAttack(){
        //chiama una f per colpire tutti quelli che erano nella stessa cella del bersaglio colpito dall'attacco baseprima del movimento
        for(Player player : players)
            player.getPlayerBoard().addDamage(player, 1);
    }
}
