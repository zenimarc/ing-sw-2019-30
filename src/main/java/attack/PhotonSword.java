package attack;

public class PhotonSword extends Attack{

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere nella stessa cella in cui si è
        players.get(0).getPlayerBoard().addDamage(getShooter(), 2);
    }

    public void priorityMove(){
        //chiama una funzione per selezionare dove muoversi ed ha effetto prima o dopo effetto base

    }

    public void optionalAttack() {
        //chiama una f per selezionare il target e deve esssere nella stessa cella in cui si è e diverso da quello dell'attacco base
        players.get(0).getPlayerBoard().addDamage(getShooter(), 2);
    }
}
