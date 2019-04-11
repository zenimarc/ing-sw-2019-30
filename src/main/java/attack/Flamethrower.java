package attack;

import attack.Attack;

public class Flamethrower extends Attack {
    private int distance1 = 1;
    private int distance2 = 2;

    public void baseAttack() {
        //chiama una f per selezionare due bersagli su quadrati diversi e nella stessa direzione distanti rispettivamente 1 e 2 quadrati da te, verificando per entrambi che non ci siano muri
        players.get(0).getPlayerBoard().addDamage(players.get(0), 1);
        //seconda verifica
        players.get(1).getPlayerBoard().addDamage(players.get(1), 1);
    }

    public void alternativeAttack() {
        //chiama una f per selezionare due bersagli su quadrati diversi e nella stessa direzione distanti rispettivamente 1 e 2 quadrati da te, verificando per entrambi che non ci siano muri
        players.get(0).getPlayerBoard().addDamage(players.get(0), 2);
        //seconda verifica
        players.get(1).getPlayerBoard().addDamage(players.get(1), 1);
    }
}
