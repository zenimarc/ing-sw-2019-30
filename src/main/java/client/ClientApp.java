package client;

import board.Board;
import player.Player;
import view.Cli;
import view.View;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Observer;

public class ClientApp {
    private ClientRMI connection;
    private View view;
    private ClientUpdateManager clientUpdateManager;

    public ClientApp(){
        try {
            connection = new ClientRMI(this);
        }catch (RemoteException re){
            re.fillInStackTrace();
        }
        this.clientUpdateManager = new ClientUpdateManager();
    }

    protected void createView(Player player, Board board, Observer client){
        createCLIView(player, board, client);
        clientUpdateManager.setView(view);
        view.gameStart(board);
    }

    private void createCLIView(Player player, Board board, Observer client){
        view = new Cli(player, board, client);
    }

    public View getView(){
        return this.view;
    }

    protected ClientUpdateManager getClientUpdateManager() {
        return clientUpdateManager;
    }

    public static void main(String[] args) {
        ClientApp clientApp = new ClientApp();
        File folder = new File("data");
        if (!folder.exists()) folder.mkdir();
        clientApp.connection.init();

    }

    public void beginApp(){
        connection.init();
    }
}
