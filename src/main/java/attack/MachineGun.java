package attack;

import attack.Attack;
import player.Player;

public class MachineGun extends Attack {
    int maxTargets = 2;
    int i = 0;

    public void baseAttack(){
        //chiama una f per selezionare i target e devono esssere visibili
        for(Player player : players)
            player.getPlayerBoard().addDamage(player, 1);
    }

    public void optionalAttack1(){

        //chiama una f per selezionare uno dei due target visibili
            players.get(i).getPlayerBoard().addDamage(players.get(i), 1);
    }

    public void optionaleAttack2(){
        //chiama una f per selezionare l'altro dei due target visibili. Nel caso non sia stato attivato il primo opzionale, non serve il controllo
        players.get(i).getPlayerBoard().addDamage(players.get(i), 1);
        //chiama una f per selezionare un bersaglio diverso dai primi due e gli infligge danno(si può anche non fare)
        players.get(i).getPlayerBoard().addDamage(players.get(i), 1);
    }
}
