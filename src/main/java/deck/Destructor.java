package deck;

public class Destructor extends Attack{
    private int maxTargets = 1;

    public void baseAttack(){
        //chiama una f per selezionare i target e deve esssere vsisibile
        players.get(0).getPlayerBoard().addDamage(players.get(0), 2);
        players.get(0).getPlayerBoard().addMark(players.get(0), 1);
    }

    public void optionalAttack(){
        //chiama una f per selezionare il target visible e diverso dal primo
        players.get(1).getPlayerBoard().addMark(players.get(1), 1);
    }
}
