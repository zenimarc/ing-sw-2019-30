package weapon;

import attack.Attack;
import attack.Attack_;
import attack.SingleAttack;
import deck.Bullet;

import java.util.ArrayList;
import java.util.List;

public class Destructor extends WeaponCard {
    private int maxTargets = 1;

    public Destructor(String name, List<Bullet> cost, List<Attack_> attacks) {
        super(name,cost, attacks);
    }


    public Destructor(){
        super(enumWeapon.DESTRUCTOR.getName(),enumWeapon.DESTRUCTOR.getCost());

        attacks.add(new SingleAttack("Effetto base",
                "Dai 2 danni e 1 marchio a 1 bersaglio che puoi vedere.",
                2,1));
        attacks.add(new SingleAttack("Secondo Aggancio",
                "Dai 1 marchio a un altro bersaglio che puoi vedere",
                0,1));
    }



    /*
    public void baseAttack(){
        //chiama una f per selezionare il target e deve esssere visisibile
        players.get(0).addDamage(getShooter(), 2);
        players.get(0).addMark(getShooter(), 1);
    }

    public void optionalAttack(){
        //chiama una f per selezionare il target visible e diverso dal primo
        players.get(1).addMark(getShooter(), 1);
    }*/
}
