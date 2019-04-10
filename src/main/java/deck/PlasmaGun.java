package deck;

public class PlasmaGun extends Attack {
    int maxMovement = 2;

    public void baseAttack(){
        //chiama una f per selezionare i target e deve esssere vsisibile
        players.get(0).getPlayerBoard().addDamage(players.get(0), 2);
        players.get(0).getPlayerBoard().addMark(players.get(0), 1);
    }

    public void priorityMove(){
        //chiama una funzione per selezionare dove muoversi ed ha effetto prima o dopo effetto base

    }
    public void optionalAttack(){
        //colpisce il targetscelto in precedenza
        players.get(0).getPlayerBoard().addDamage(players.get(0), 1);
    }
}
