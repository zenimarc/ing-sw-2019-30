package client;

import server.GameServer;
import server.Lobby;
import server.LobbyImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.UUID;

public class ClientRMI extends UnicastRemoteObject implements Client {
    private String nickname;
    private Registry registry;
    private transient Lobby lobby;
    private transient GameServer gameServer;


    public ClientRMI() throws RemoteException {
        //new generic client RMI
    }


    public void connect(String host) throws RemoteException {
        connect(host, 1099);
    }

    public void connect(String host, int port) throws RemoteException {
        this.registry = LocateRegistry.getRegistry(host, port);
        try {
            this.lobby = (Lobby) registry.lookup("lobby");
            System.out.println("lobby trovata");
        } catch (NotBoundException nbe) {
            System.out.println(nbe.toString());
            nbe.fillInStackTrace();
        }
    }

    public boolean register() {
        try {
            UUID myToken = this.lobby.register(this.nickname, this);
            if (myToken != null)
                return true;
            else
                return false;
        } catch (RemoteException re) {
            System.out.println(re.toString());
            re.fillInStackTrace();
        }
        return false;
    }

    public void loggedIn() {
        System.out.println("Login success");
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
        try {
            System.out.println("ho ricevuto il gameserver: " + gameServer.getGameToken());
        } catch (RemoteException re) {
            re.fillInStackTrace();
        }
    }

    public String getName() {
        return this.nickname;
    }

    public static void main(String[] args) {
        try {
            ClientRMI clientRMI = new ClientRMI();
            clientRMI.connect("127.0.0.1", 1099);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter username");
            String userName = scanner.nextLine();
            clientRMI.setNickname(userName);
            while (!clientRMI.register()) {
                System.out.println("Enter another username");
                userName = scanner.nextLine();
                clientRMI.setNickname(userName);
            }

        } catch (RemoteException re) {
            System.out.println(re.toString());
            re.fillInStackTrace();
        }
    }
}

