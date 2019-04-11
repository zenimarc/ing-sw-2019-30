package attack;

public class CyberGlove extends Attack {

    public void baseAttack(){
        //chiama una f per selezionare un target distante un movimento
        //chiama f per spostarsi verso il target scelto prima
        players.get(0).getPlayerBoard().addDamage(getShooter(), 1);
        players.get(0).getPlayerBoard().addMark(getShooter(), 2);
    }

    public void alternativeAttack(){
        //chiama una f per selezionare un target distante un movimento
        //chiama f per spostarsi verso il target scelto prima
        players.get(0).getPlayerBoard().addDamage(getShooter(), 2);
        //se si vuole chiama una f per selezionare un target distante un movimento e nella stessa direzione rispetto alla posizione iniziale
        //chiama f per spostarsi verso il target scelto prima
        players.get(0).getPlayerBoard().addDamage(getShooter(), 2);
    }
}
