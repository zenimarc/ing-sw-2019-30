package cardAttack;

import attack.Attack;
import player.Player;

public class ZX2 extends Attack {

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere visisibile
        players.get(0).addDamage(getShooter(), 1);
        players.get(0).addMark(getShooter(), 2);
    }

    public void alternativeAttack(){
        //chiama una f per selezionare fino a 3 target visibili
        for(Player player : players)
            players.get(0).addMark(getShooter(), 1);
    }
}
