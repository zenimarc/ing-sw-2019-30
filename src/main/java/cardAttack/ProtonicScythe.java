package cardAttack;

import attack.Attack;
import player.Player;

public class ProtonicScythe extends Attack {

    public void baseAttack(){
        //chiama una f per selezionare i target nella cella in cui sono
        for(Player player : players)
            player.addDamage(getShooter(), 1);
    }

    public void alternativelAttack1(){
        //chiama una f per selezionare i target nella cella in cui sono
        for(Player player : players)
            player.addDamage(getShooter(), 2);
    }
}