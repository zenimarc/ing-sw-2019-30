package controller;

import constants.Constants;
import controller.BoardController;

/**
 * TurnHandler is used to handle the maximum time a player can use to choose an action
 */

public class TurnHandler extends Thread{
    private static final long TURN_TIME = Constants.TURN_TIME.getValue(); //turn timeout in seconds
    private BoardController boardController;

    /**
     * Constructor
     */
    public TurnHandler(BoardController boardController){
        this.boardController = boardController;
    }

    /**
     * Overrides run method and begin countdown
     */
    @Override
    public void run(){
        try {
            System.out.println("Thread turHandler partito");
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(TURN_TIME * 1000);
                boardController.changeTurn();
            }
            Thread.currentThread().interrupt();

        }catch (InterruptedException ie){
            ie.fillInStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
