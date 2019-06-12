package local_game;

import controller.BoardController;
import player.Player;

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


        int i=0;
        while (true) {
            playerWhoPlay = boardController.getPlayer();

            boardController.playerPlay(playerWhoPlay);

            boardController.changeTurn();

            i++;
        }
    }

    private ArrayList<Player> initializePlayer(){
        Player p1 = new Player("Aldo");
        Player p2 = new Player("Bob");
        Player p3 = new Player("Carl");
        Player p4 = new Player("Doc");

        p1.addAmmo(new int[]{1,1,1});
        p2.addAmmo(new int[]{1,1,1});
        p3.addAmmo(new int[]{1,1,1});
        p4.addAmmo(new int[]{1,1,1});

       // return new ArrayList<>(Arrays.asList(p1,p2));
       // return new ArrayList<>(Arrays.asList(p1,p2,p3));
        return new ArrayList<>(Arrays.asList(p1,p2,p3,p4));

    }





    public static void main(String[] args) {
        new MainLocalGame();


    }

}
