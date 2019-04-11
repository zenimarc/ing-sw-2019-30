package cardAttack;

import attack.Attack;

public class HeatMissile extends Attack {

    public void baseAttack() {
        //chiama una f per selezionare un target e deve esssere non visisibile
        players.get(0).addDamage(getShooter(), 3);
    }
}
