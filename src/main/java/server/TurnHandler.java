package server;

public class TurnHandler extends Thread{
    private static final int TURNTIME = 10; //turn timeout in seconds
    private GameServerImpl gameServer;

    public TurnHandler(GameServerImpl gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void run(){
        try {
            System.out.println("Thread turHandler partito");
            while (gameServer.isStarted()) {
                System.out.println("è il turno di: "+ gameServer.getTurn());
                Thread.sleep(TURNTIME * 1000);
                gameServer.changeTurn();
            }
            Thread.currentThread().interrupt();

        }catch (InterruptedException ie){
            ie.fillInStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
