package attack;

public class IonHammer extends Attack {

    public void baseAttack() {
        //chiama una f per selezionare un target e deve esssere nella mia stessa cella
        players.get(0).addDamage(getShooter(), 2);
    }

    public void alternativeAttack() {
            //chiama una f per selezionare un target e deve esssere nella mia stessa cella
            players.get(0).addDamage(getShooter(), 3);
            //chiama f per spostare di max due movimenti in linea retta il bersaglio colpito
    }
}
