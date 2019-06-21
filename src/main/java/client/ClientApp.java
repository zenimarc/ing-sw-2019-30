package client;

import java.rmi.RemoteException;

public class ClientApp {
    private Client connection;

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
