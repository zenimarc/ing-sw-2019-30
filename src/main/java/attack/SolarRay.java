package attack;

import attack.Attack;
import player.Player;

public class SolarRay extends Attack {
    private int distance = 1;

    public void baseAttack(){
        //chiama una f per selezionare un bersaglio visibile non nella tua cella
        players.get(0).addDamage(getShooter(), 1); //colpisce solo il primo
        for(Player player : players)//da marchi a tutti quelli che sono nella stessa cella
            player.addMark(getShooter(), 1);
    }

    public void alternativeAttack(){
        //chiama una f per selezionare un bersaglio visibile non nella tua cella
        players.get(0).addDamage(getShooter(), 1); //colpisce solo il primo
        for(Player player : players)//da marchi a tutti quelli che sono nella stessa cella
            player.addMark(getShooter(), 2);
        }

}
