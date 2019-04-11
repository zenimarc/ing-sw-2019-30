package attack;

import player.Player;

public class LaserGun extends Attack {

    public void baseAttack(){
        //chiama una f per selezionare un bersaglio in una direzione cardinale
        players.get(0).addDamage(getShooter(), 3);
    }

    public void alternativeAttack(){
        //chiama una f per selezionare uno o due bersagli in una stessa direzione cardinale
        for(Player player : players) {
            player.addDamage(getShooter(), 2);
        }
    }
}
