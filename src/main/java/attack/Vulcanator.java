package attack;

import attack.Attack;
import player.Player;

public class Vulcanator extends Attack {
    private int distance = 1;

    public void baseAttack(){
        //chiama una f per selezionare che puoi vedere diversa dalla tua e attacca tutti i bersagli lì dentro
        for(Player player : players)
            player.getPlayerBoard().addDamage(player, 1);
    }

    public void alternativeAttack(){
        //chiama una f per selezionare una cella distante un movimento da te ed infligge danno a tutti
        for(Player player : players) {
            player.getPlayerBoard().addDamage(player, 1);
            player.getPlayerBoard().addMark(players.get(0), 1);
        }
    }
}
