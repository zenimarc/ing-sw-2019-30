package cardAttack;

import attack.Attack;

public class PlasmaGun extends Attack {
    int maxMovement = 2;

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere visisibile
        players.get(0).addDamage(getShooter(), 2);
    }

    public void priorityMove(){
        //chiama una funzione per selezionare dove muoversi ed ha effetto prima o dopo effetto base

    }
    public void optionalAttack(){
        //colpisce il target scelto in precedenza
        players.get(0).addDamage(getShooter(), 1);
    }
}
