package attack;

import attack.Attack;
import player.Player;

public class SolarRay extends Attack {
    private int distance = 1;

    public void baseAttack(){
        //chiama una f per selezionare un bersaglio visibile non nella tua cella
        players.get(0).getPlayerBoard().addDamage(players.get(0), 1); //colpisce solo il primo
        for(Player player : players)//da marchi a tutti quelli che sono nella stessa cella
            player.getPlayerBoard().addMark(player, 1);
    }

    public void alternativeAttack(){
        //chiama una f per selezionare un bersaglio visibile non nella tua cella
        players.get(0).getPlayerBoard().addDamage(players.get(0), 1); //colpisce solo il primo
        for(Player player : players)//da marchi a tutti quelli che sono nella stessa cella
            player.getPlayerBoard().addMark(player, 2);
        }

}
