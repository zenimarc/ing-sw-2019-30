package attack;

import player.Player;

public class ShockWave extends Attack {

    public void baseAttack(){
        //chiama una f per selezionare fino a 3 target e devono esssere su quadrati differenti
        for(Player player : players)
            player.getPlayerBoard().addDamage(getShooter(), 1);
    }

    public void alternativeAttack(){
        //chiama una f per colpire tutti i bersaglidistanti un movimento da te
        for(Player player : players)
            player.getPlayerBoard().addDamage(getShooter(), 1);
    }

}
