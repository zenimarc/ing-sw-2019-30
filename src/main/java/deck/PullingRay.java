package deck;

public class PullingRay extends Attack{
    private int maxEnemyMovement = 2;

    public void baseAttack(){
        //chiama una f per selezionare un target e lo muove in una cella che vuole il player(max 2 movimenti), verificando che questa sia visibile
        players.get(0).getPlayerBoard().addDamage(players.get(0), 1);
    }

    public void alternativeAttack(){
        //chiama una f per selezionare il target lontano massimo due spazi, poi viene messo nella tua cella
        players.get(1).getPlayerBoard().addDamage(players.get(1), 3);
    }
}
