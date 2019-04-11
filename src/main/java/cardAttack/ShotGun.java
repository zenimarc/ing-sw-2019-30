package cardAttack;

import attack.Attack;

public class ShotGun extends Attack {

    public void baseAttack() {
        //chiama una f per selezionare un target e deve esssere nella mia stessa cella
        players.get(0).addDamage(getShooter(), 3);
        //chiama f per spostare di un movimento il bersaglio colpito se si vuole
    }
    public void alternativeAttack() {
        //chiama una f per selezionare un target e deve esssere distante un movimento
        players.get(0).addDamage(getShooter(), 2);
    }

}
