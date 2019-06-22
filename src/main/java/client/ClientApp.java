package client;

import board.Board;
import player.Player;
import view.Cli;
import view.View;

import java.rmi.RemoteException;
import java.util.Observer;

public class ClientApp {
    private ClientRMI connection;
    private View view;

    public ClientApp(){
        try {
            connection = new ClientRMI(this);
        }catch (RemoteException re){
            re.fillInStackTrace();
        }
    }

    public void createView(Player player, Board board, Observer client){
        createCLIView(player, board, client);
    }

    public void createCLIView(Player player, Board board, Observer client){
        view = new Cli(player, board, client);
    }

    public View getView(){
        return this.view;
    }

    public static void main(String[] args) throws RemoteException {
        ClientApp clientApp = new ClientApp();
        clientApp.connection.init();

    }
}
