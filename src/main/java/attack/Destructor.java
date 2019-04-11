package attack;


import attack.Attack;

public class Destructor extends Attack {
    private int maxTargets = 1;

    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere visisibile
        players.get(0).addDamage(getShooter(), 2);
        players.get(0).addMark(getShooter(), 1);
    }

    public void optionalAttack(){
        //chiama una f per selezionare il target visible e diverso dal primo
        players.get(1).addMark(getShooter(), 1);
    }
}
