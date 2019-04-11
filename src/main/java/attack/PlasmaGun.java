package attack;

import attack.Attack;

public class PlasmaGun extends Attack {
    int maxMovement = 2;

    public void baseAttack(){
        //chiama una f per selezionare i target e deve esssere vsisibile
        players.get(0).getPlayerBoard().addDamage(getShooter(), 2);
    }

    public void priorityMove(){
        //chiama una funzione per selezionare dove muoversi ed ha effetto prima o dopo effetto base

    }
    public void optionalAttack(){
        //colpisce il target scelto in precedenza
        players.get(0).getPlayerBoard().addDamage(getShooter(), 1);
    }
}
