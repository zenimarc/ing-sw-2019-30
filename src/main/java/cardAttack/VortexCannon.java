package cardAttack;

import attack.Attack;
import player.Player;

public class VortexCannon extends Attack {
    private int distance = 1;
    private int maxTargets = 2;

    public void baseAttack(){
        //chiama una f per selezionare una cella distante un movimento
        //chiama una f per selezionare un target nella cella selzionata precedente o distante 1 movimento e lo sposta lì
        players.get(0).addDamage(getShooter(), 2);
    }

    public void optionalAttack(){
        //chiama una f per selezionare i target distanti un movimento dal vortice e li sposta in quella cella(controlla che suabi diversi tra di loro
        for(Player player : players)
            player.addDamage(getShooter(), 1);
    }
}
