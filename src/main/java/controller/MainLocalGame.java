package controller;

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
        while (i<3) {
            playerWhoPlay = boardController.getPlayer();
            System.out.println(playerWhoPlay);

            if(playerWhoPlay.getCell()==null){
               int index = choosePowerUp4Regeneration();
               boardController.setRegenerationCell(playerWhoPlay, index);
            }


            boardController.changeTurn();
            i++;
        }
        boardController.getBoardViewToString();

    }

    private ArrayList<Player> initializePlayer(){
        Player p1 = new Player("Aldo");
        Player p2 = new Player("Bob");
        Player p3 = new Player("Carlo");

        p1.addAmmo(new int[]{1,1,1});
        p2.addAmmo(new int[]{1,1,1});
        p3.addAmmo(new int[]{1,1,1});

        return new ArrayList<>(Arrays.asList(p1,p2,p3));
    }

    /**
     * This ask player what powerUpCard wants discard to be regenerated
     * @return
     */
    private int choosePowerUp4Regeneration(){
        int slt;
        List<PowerCard> powerUps = playerWhoPlay.getPowerups();

        while (true) {

            StringBuilder sb = new StringBuilder();
            sb.append("Set your RegeneretionCell. Your PowerUpCard are:\n");
            for (PowerCard pc : powerUps) {
                sb.append(powerUps.indexOf(pc));
                sb.append(") ");
                sb.append(pc);
                sb.append('\n');
            }
            sb.append("What RegenerationCell color you want? (Enter number)[0] ");

            System.out.print(sb.toString());

            slt = reader.nextInt();
            if (slt < powerUps.size()) return slt;
        }
    }


    public static void main(String[] args) {
        new MainLocalGame();


    }

}
