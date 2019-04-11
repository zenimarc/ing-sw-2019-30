package cardAttack;

import attack.Attack;
import player.Player;

public class RocketLauncher extends Attack {

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere visisibile e non nella tua cella
        players.get(0).addDamage(getShooter(), 2);
        //chiama una f per muovere il bersaglio colpito di 1 movimento
    }

    public void priorityMove(){
        //chiama una funzione per selezionare dove muoversi ed ha effetto prima o dopo effetto base
    }

    public void optionalAttack(){
        //chiama una f per colpire tutti quelli che erano nella stessa cella del bersaglio colpito dall'attacco base prima del movimento
        for(Player player : players)
            player.addDamage(getShooter(), 1);
    }
}
