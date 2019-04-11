package attack;

import attack.Attack;

public class Torpedo extends Attack {
    public int maxTargets = 1;

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere vsisibile
        players.get(0).addDamage(getShooter(), 2);
    }

    public void optionalAttack1(){
        //chiama una f per selezionare il target e deve esssere vsisibile dal giocatore colpito in precedenza
        players.get(0).addDamage(getShooter(), 1);
    }

    public void optionaleAttack1_5(){
        //chiama una f per selezionare il target e deve esssere vsisibile dal giocatore colpito in precedenza e diverso dal primo bersaglio
        players.get(0).addDamage(getShooter(), 2);
    }
}
