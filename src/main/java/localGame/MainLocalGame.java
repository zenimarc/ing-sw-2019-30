package localGame;

import controller.BoardController;
import controller.PlayerCommand;
import player.Player;
import powerup.PowerCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class is a local demo of Adrenaline, for test the game.
 */

public class MainLocalGame {

    private ArrayList<Player> players;
    private BoardController boardController;
    private Scanner reader = new Scanner(System.in);
    private Player playerWhoPlay;
    private int action;

    private MainLocalGame(){

        //initialize player and board
        players = initializePlayer();
        boardController = new BoardController(players, 8);

        //print board (text description)
        System.out.println(boardController.getBoard().getBillboard().toString());

        //print board (drawCLI)
        //boardController.getBoardViewToString();
        //print PlayerBoard for each player

        for (Player p : players) {
            p.addPowerCard((PowerCard) boardController.getBoard().getPowerUpDeck().draw());
            p.addPowerCard((PowerCard) boardController.getBoard().getPowerUpDeck().draw());
        }

        int i=0;
        while (i<5) {
            playerWhoPlay = boardController.getPlayer();
            System.out.println(playerWhoPlay);

            boardController.playerPlay(playerWhoPlay);

            if(boardController.changeTurn()==0){
                boardController.getBoardViewToString();
            }
            i++;
        }
        boardController.getBoardViewToString();

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

    private String getPlayerToPrint(){
        StringBuilder sb = new StringBuilder();
        sb.append("----- Current player: ");
        sb.append(playerWhoPlay.getName());
        sb.append(" -----\n");
        return sb.toString();
    }





    public static void main(String[] args) {
        new MainLocalGame();


    }

}
