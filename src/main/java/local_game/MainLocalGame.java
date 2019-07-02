package local_game;

import board.Position;
import controller.BoardController;
import player.Player;
import weapon.*;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * This class is a local demo of Adrenaline, for test the game.
 */

public class MainLocalGame {

    private ArrayList<Player> players;
    private BoardController boardController;
    private Player playerWhoPlay;

    private MainLocalGame(){

        //initialize player and board
        players = initializePlayer();
        boardController = new BoardController(players, 8);


        players.get(0).setPawnCell(boardController.getBoard().getBillboard().getCellFromPosition(new Position(0,2)));
        players.get(1).setPawnCell(boardController.getBoard().getBillboard().getCellFromPosition(new Position(1,2)));
        players.get(2).setPawnCell(boardController.getBoard().getBillboard().getCellFromPosition(new Position(2,2)));
        players.get(3).setPawnCell(boardController.getBoard().getBillboard().getCellFromPosition(new Position(1,2)));

        players.get(0).addWeapon(new SimpleWeapon(EnumWeapon.LOCK_RIFLE));
        players.get(1).addWeapon(new SimpleWeapon(EnumWeapon.MACHINE_GUN));
        players.get(2).addWeapon(new SimpleWeapon(EnumWeapon.ELECTROSCYTHE));

        players.get(1).addDamage(players.get(0), 3);
        players.get(1).addDamage(players.get(2), 2);
        players.get(1).addDamage(players.get(0), 1);
        players.get(1).addDamage(players.get(3), 3);
        players.get(1).addDamage(players.get(0), 1);

        for(Player p : players) {
            p.getNotLoaded().forEach(WeaponCard::setLoaded);
        }

        int i=0;
        while (true) {
            playerWhoPlay = boardController.getPlayer();

            boardController.playerPlay(playerWhoPlay);

    //        boardController.changeTurn();

            i++;
        }
    }

    private ArrayList<Player> initializePlayer(){
        Player p1 = new Player("Aldo");
        Player p2 = new Player("Bob");
        Player p3 = new Player("Carl");
        Player p4 = new Player("Doc");

        p1.addAmmo(new int[]{3,3,3});
        p2.addAmmo(new int[]{3,3,3});
        p3.addAmmo(new int[]{3,3,3});
        p4.addAmmo(new int[]{3,3,3});

       // return new ArrayList<>(Arrays.asList(p1,p2));
       // return new ArrayList<>(Arrays.asList(p1,p2,p3));
        return new ArrayList<>(Arrays.asList(p1,p2,p3,p4));

    }





    public static void main(String[] args) {
        new MainLocalGame();


    }

}
