package deck;

public class Sniper extends Attack{
    private int min_distance = 2;

    public void baseAttack(){
        //chiama una f per selezionare i target e deve esssere vsisibile e lontano 2 spazi
        players.get(0).getPlayerBoard().addDamage(players.get(0), 3);
        players.get(0).getPlayerBoard().addMark(players.get(0), 1);
    }
}
