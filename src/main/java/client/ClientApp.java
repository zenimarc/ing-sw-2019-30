package client;

import view.View;

import java.rmi.RemoteException;

public class ClientApp {
    private ClientRMI connection;
    private View view;

    public ClientApp(){
        try {
            connection = new ClientRMI();
        }catch (RemoteException re){
            re.fillInStackTrace();
        }
    }
    public static void main(String[] args){
        ClientApp clientApp = new ClientApp();
        clientApp.connection.init();
    }
}
