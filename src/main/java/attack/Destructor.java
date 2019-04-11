package attack;

import attack.Attack;

public class Destructor extends Attack {
    private int maxTargets = 1;

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere visisibile
        players.get(0).getPlayerBoard().addDamage(getShooter(), 2);
        players.get(0).getPlayerBoard().addMark(getShooter(), 1);
    }

    public void optionalAttack(){
        //chiama una f per selezionare il target visible e diverso dal primo
        players.get(1).getPlayerBoard().addMark(getShooter(), 1);
    }
}
