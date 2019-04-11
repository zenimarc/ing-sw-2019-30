package attack;

import attack.Attack;

public class Sniper extends Attack {
    private int min_distance = 2;

    public void baseAttack(){
        //chiama una f per selezionare i target e deve esssere visisibile e lontano 2 spazi
        players.get(0).addDamage(getShooter(), 3);
        players.get(0).addMark(getShooter(), 1);
    }
}
