package controller;

import board.Billboard;
import board.Board;
import player.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is a local demo of Adrenaline, for test the game.
 */

public class MainLocalGame {

    private ArrayList<Player> players;
    private Billboard billboard;
    private Board board;
    private BoardController boardController;


    public MainLocalGame(){


        //initialize player and board
        players = initializePlayer();
        boardController = new BoardController(players, 8);

        //print board
        System.out.println(boardController.getBoard().getBillboard().toString());

    }

    private ArrayList<Player> initializePlayer(){
        Player p1, p2, p3;
        p1 = new Player("Aldo");
        p2 = new Player("Bob");
        p3 = new Player("Carlo");

        return new ArrayList<>(Arrays.asList(p1,p2,p3));
    }



    public static void main(String[] args) {
        new MainLocalGame();
    }

}
