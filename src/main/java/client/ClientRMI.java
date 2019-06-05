package client;

import server.GameServer;
import server.Lobby;
import server.LobbyImpl;
import view.PlayerBoardView;
import view.PlayerView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.UUID;

public class ClientRMI extends UnicastRemoteObject implements Client {
    private String nickname;
    private transient Registry registry;
    private transient Lobby lobby;
    private transient GameServer gameServer;
    private UUID userToken;
    private PlayerView playerView;
    private PlayerBoardView playerBoardView;


    public ClientRMI() throws RemoteException {
        //new generic client RMI
    }

    public void connect(String host) throws RemoteException {
        connect(host, 1099);
    }

    /**
     * this function lookups the rmi registry at indicated hostname and port and try to obtain a remote lobby
     * @param host of rmi registry
     * @param port of rmi registry
     * @return true if it is successfully connected, else false
     * @throws RemoteException
     */
    public boolean connect(String host, int port) throws RemoteException {
        this.registry = LocateRegistry.getRegistry(host, port);
        try {
            this.lobby = (Lobby) registry.lookup("lobby");
            System.out.println("lobby trovata");
            return true;
        } catch (NotBoundException nbe) {
            System.out.println(nbe.toString());
            nbe.fillInStackTrace();
            return false;
        }
    }

    /**
     * this function is used by a gameServer to check if the client is still online
     * @return always true so if the clients it's online and can reply the server will notice the reply.
     * @throws RemoteException if the server can't connect to the client
     */
    public boolean isActive() throws RemoteException{
        return true;
    }

    /**
     * this function register this client to the remote Lobby and if the Lobby accept the
     * registration, the client saves his userToken in local attribute.
     * if it returns true, it means that the player also joined in a new game.
     * @param nickname you want to use
     * @return true if the Lobby accepts the connection, False if the Lobby refuses the connection
     * or if there are connectivity problems.
     */
    public boolean register(String nickname) {
        try {
            UUID myToken = this.lobby.register(nickname, this);
            if (myToken != null) {
                this.setNickname(nickname);
                this.userToken = myToken;
                return true;
            }
            else
                return false;
        } catch (RemoteException re) {
            System.out.println(re.toString());
            re.fillInStackTrace();
        }
        return false;
    }

    /**
     * this function is called by remote Lobby to notify the player is succesfully login
     */
    public void loggedIn() {
        System.out.println("Login success");
    }

    public void gameStarted() throws RemoteException{

    }

    public PlayerView createPlayerView() throws RemoteException{
        return this.playerView = new PlayerView(gameServer.getPlayer(this), gameServer.getPlayerController(this));
    }

    public PlayerBoardView createPlayerBoardView() throws RemoteException{
        return this.playerBoardView = new PlayerBoardView(gameServer.getPlayer(this));
    }

    /**
     * this function set the nickname for the client
     * @param nickname to be set on this client
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * this function returns the client's nickname
     * @return the client's nickname
     */
    public String getNickname(){
        return this.nickname;
    }

    /**
     * this funcion is called by remote Lobby to set on this client the gameServer assigned to play in
     * @param gameServer to play in
     */
    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
        try {
            System.out.println("ho ricevuto il gameserver: " + gameServer.getGameToken());
        } catch (RemoteException re) {
            re.fillInStackTrace();
        }
    }

    /**
     * this function tries to reconnect to the last match interrupted
     * @param username of the client
     * @param userToken of the client previously assigned by the remote Lobby.
     * @throws RemoteException if it's impossible to reconnect
     */
    public boolean reconnect(String username, UUID userToken) throws RemoteException{
        this.gameServer = lobby.reconnect(username, userToken, this);
        if (gameServer!=null) {
            System.out.println("mi sono ricollegato a: " + gameServer.getGameToken());
            return true;
        }
        else {
            System.out.println("impossibile riconnettere");
            return false;
        }
    }

    public String getName() {
        return this.nickname;
    }

    public UUID getUserToken(){
        return this.userToken;
    }

    @Override
    public void timeExpired() throws RemoteException {

    }

    public static void main(String[] args) {
        try {
            ClientRMI clientRMI = new ClientRMI();
            clientRMI.connect("127.0.0.1", 1099);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter username (min 3 chars max 15 chars)");
            String userName = scanner.nextLine();
            clientRMI.setNickname(userName);
            System.out.println("Enter connect or reconnect");
            if(scanner.nextLine().equals("reconnect")) {
                System.out.println("Enter your userToken");
                clientRMI.reconnect(userName, UUID.fromString(scanner.nextLine()));
            }
            else
                while (!clientRMI.register(clientRMI.getNickname())) {
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

