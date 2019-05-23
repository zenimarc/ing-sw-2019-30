package client;

import server.Lobby;
import server.LobbyImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI extends UnicastRemoteObject implements Client {
    private String gameId;
    private String nickname;
    private Registry registry;

    public ClientRMI(String nickname) throws RemoteException{
        this.nickname = nickname;
    }


    public void gameStarted(Object obj){
        this.gameId = (String) obj;
        System.out.println("pronti a giocare nel server: "+ gameId);
    }

    public void login(String nickname, String ip) throws RemoteException{
        login(ip, 1099);
    }
    public void login(String host, int port) throws RemoteException {
        this.registry = LocateRegistry.getRegistry(host, port);
        try {
            Lobby lobby = (Lobby) registry.lookup("lobby");
            lobby.login(this.nickname, this);
        }catch (NotBoundException nbe){
            nbe.fillInStackTrace();
        }


    }

    public void loggedIn(){
        System.out.println("Login success");
    }

    public String getName(){
        return this.nickname;
    }

    public static void main(String[] args){
        try{
            ClientRMI clientRMI = new ClientRMI("marco");
            clientRMI.login("127.0.0.1", 1099);
        }catch (RemoteException re){
            System.out.println(re.toString()+"no ok");
            re.fillInStackTrace();
        }
    }
}
