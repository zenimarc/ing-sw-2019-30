package weapon;

import board.Board;
import board.Cell;
import board.Position;
import board.billboard.Billboard;
import board.billboard.BillboardGenerator;
import controller.BoardController;
import controller.EnumTargetSet;
import deck.Bullet;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponCardTest {

    @Test
    void getGrabCost() {
        WeaponCard weaponCard_1 = new SimpleWeapon(EnumWeapon.ELECTROSCYTHE);
        WeaponCard weaponCard_2 = new SimpleWeapon(EnumWeapon.LOCK_RIFLE);
        WeaponCard weaponCard_3 = new SimpleWeapon(EnumWeapon.HEATSEEKER);

        List<Bullet> cost_1 = weaponCard_1.getGrabCost();
        List<Bullet> cost_2 = weaponCard_2.getGrabCost();
        List<Bullet> cost_3 = weaponCard_3.getGrabCost();

        assertEquals( weaponCard_1.getCost().size() -1, cost_1.size());
        assertEquals( weaponCard_2.getCost().size() -1, cost_2.size());
        assertEquals( weaponCard_3.getCost().size() -1, cost_3.size());
    }

    /*@Test
    void cardinalShoot(){
        List<Player> players = new ArrayList<>();
        Billboard board = BillboardGenerator.createBillboard(1);
        Board boarder = new Board(8, board);
        Player p1 = new Player("Marco");
        p1.setPawnCell(board.getCellFromPosition(new Position(1, 0)));
        p1.addWeapon(new CardinalWeapon(EnumWeapon.RAILGUN));
        Player p2 = new Player("Marco1");
        p2.setPawnCell(board.getCellFromPosition(new Position(2, 1)));
        Player p3 = new Player("Marco2");
        p3.setPawnCell(board.getCellFromPosition(new Position(1, 2)));
        Player p4 = new Player("Marco3");
        p4.setPawnCell(board.getCellFromPosition(new Position(1, 3)));
        Player p5 = new Player("Marco4");
        List<Player> play = new ArrayList<>();
        play.add(p1);
        play.add(p2);
        play.add(p3);
        play.add(p4);
        play.add(p5);
        p5.setPawnCell(board.getCellFromPosition(new Position(0, 3)));
        BoardController controller = new BoardController(play, boarder);
        p1.addAmmo(new int[]{0, 2, 1});
        players = controller.getPotentialTargets(p1.getCell(), EnumTargetSet.CARDINAL_WALL_BYPASS);
        for(Player player : players)
            System.out.print(player.getName());
        p1.getWeapons().get(0).shoot(0, p1, players, null);
        p1.getWeapons().get(0).shoot(0, p1, players, null);

    }*/
}