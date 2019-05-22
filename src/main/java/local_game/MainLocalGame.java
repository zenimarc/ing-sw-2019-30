package local_game;

import controller.BoardController;
import player.Player;
import powerup.PowerCard;

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

        for (Player p : players) {
            p.addPowerCard((PowerCard) boardController.getBoard().getPowerUpDeck().draw());
            p.addPowerCard((PowerCard) boardController.getBoard().getPowerUpDeck().draw());
        }

        //Print board
        boardController.getBoardViewToString();

        int i=0;
        while (i<10) {
            playerWhoPlay = boardController.getPlayer();
            System.out.println(playerWhoPlay);

            boardController.playerPlay(playerWhoPlay);

            if(boardController.changeTurn()==0){
                boardController.getBoardViewToString();
            }
            i++;
        }
    }

    private ArrayList<Player> initializePlayer(){
        Player p1 = new Player("Aldo");
        Player p2 = new Player("Bob");
     //   Player p3 = new Player("Carlo");

        p1.addAmmo(new int[]{1,1,1});
        p2.addAmmo(new int[]{1,1,1});
       // p3.addAmmo(new int[]{1,1,1});

        return new ArrayList<>(Arrays.asList(p1,p2));
       // return new ArrayList<>(Arrays.asList(p1,p2,p3));
    }





    public static void main(String[] args) {
        new MainLocalGame();


    }

}
