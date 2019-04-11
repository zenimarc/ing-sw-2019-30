package attack;

import attack.Attack;
import player.Player;

public class GranadeLauncher extends Attack {

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere vsisibile
        players.get(0).getPlayerBoard().addDamage(players.get(0), 1);
        //chiama una f per muovere il bersaglio colpito di 1 movimento
    }

    public void optionalAttack(){
        //chiama una f per selezionare una cella visible e colpisce tutti. Può eesere usata prima o dopo l'attacco base
        for(Player player : players)
            player.getPlayerBoard().addDamage(player, 1);
    }
}
