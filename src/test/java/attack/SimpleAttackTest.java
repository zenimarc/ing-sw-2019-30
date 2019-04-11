package attack;

import board.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.Player;
import weapon.Gun;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SimpleAttackTest {

    private static Player p1, p2;
    private static Gun destructor;
    private static Board board;
    private static SimpleAttack myAttack;


    @BeforeAll
    public static void init(){
        int[] payment = {0,0,2};
        ArrayList<Attack_> attacks= new ArrayList<>();
        myAttack = new SimpleAttack("Effetto base", "2 danni e 1 marchio a 1 bersaglio che puoi vedere",
                2, 1);
        attacks.add(myAttack);
        destructor = new Gun("Destructor", payment, attacks);

        board = new Board();

        p1 = new Player("Player1", board);
        p2 = new Player("Player2", board);

        p1.addWeapon(destructor);
    }

    @Test
    void attack() {

        System.out.println(p1.getName()+"\tDanni: "+ p1.getPlayerBoard().getNumDamages());
        System.out.println(p2.getName()+"\tDanni: "+ p2.getPlayerBoard().getNumDamages());

        myAttack.attack(p1,p2);

        System.out.println(p1.getName()+"\tDanni: "+ p1.getPlayerBoard().getNumDamages());
        System.out.println(p2.getName()+"\tDanni: "+ p2.getPlayerBoard().getNumDamages());

        assertEquals(p2.getPlayerBoard().getNumDamages(), 2);
    }
}