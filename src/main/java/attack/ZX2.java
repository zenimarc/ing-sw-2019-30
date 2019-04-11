package attack;

import player.Player;

public class ZX2 extends Attack {

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere vsisibile
        players.get(0).getPlayerBoard().addDamage(getShooter(), 1);
        players.get(0).getPlayerBoard().addMark(getShooter(), 2);
    }

    public void alternativeAttack(){
        //chiama una f per selezionare fino a 3 target visibili
        for(Player player : players)
            players.get(0).getPlayerBoard().addMark(getShooter(), 1);
    }
}
