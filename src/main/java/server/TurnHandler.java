package server;

public class TurnHandler extends Thread{
    private static final long TURNTIME = 10; //turn timeout in seconds
    private GameServerImpl gameServer;

    public TurnHandler(GameServerImpl gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void run(){
        try {
            //System.out.println("Thread turHandler partito");
            while (gameServer.isStarted() && !Thread.currentThread().isInterrupted()) {
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
