package client;

public class ClientRMI implements Client {
    private String gameId;

    public ClientRMI(){
    }

    public void gameStarted(Object obj){
        this.gameId = (String) obj;
        System.out.println("pronti a giocare nel server: "+ gameId);
    }

}
