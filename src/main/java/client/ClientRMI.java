package client;

import attack.Attack;
import board.Board;
import controller.CommandObj;
import controller.PlayerController;
import player.Player;
import server.GameServer;
import server.Lobby;
import view.View;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientRMI extends UnicastRemoteObject implements Client, Observer {
    private String nickname;
    private transient Registry registry;
    private transient Lobby lobby;
    private transient GameServer gameServer;
    private UUID userToken;
    private Player player;
    private transient View view;
    private ClientApp clientApp;


    @Override
    public void update(Observable o, Object arg) {
        if(arg.getClass().equals(CommandObj.class)) {
            try {
                sendCMD((CommandObj) arg);
            } catch (RemoteException re) {
                view.giveError(re.getMessage());
            }
        }
    }

    public ClientRMI(ClientApp clientApp) throws RemoteException{
        this.clientApp = clientApp;
    }

    @Deprecated
    public ClientRMI() throws RemoteException{}

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
            clientLog("lobby trovata");
            return true;
        } catch (NotBoundException nbe) {
            clientLog(nbe.toString());
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

    public void receiveCMD(CommandObj cmd) throws RemoteException {
        clientApp.getClientUpdateManager().executeCommand(cmd);
    }

    public void sendCMD(CommandObj cmd) throws RemoteException{
        gameServer.receiveCMD(cmd, this);
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
                clientLog("ricevuto il mio userToken: "+this.userToken);
                return true;
            }
            else
                return false;
        } catch (RemoteException re) {
            clientLog(re.toString());
            re.fillInStackTrace();
        }
        return false;
    }

    /**
     * this function is called by remote Lobby to notify the player is succesfully login
     */
    public void loggedIn() {
        clientLog("Login success");
    }

    public void gameStarted() throws RemoteException{
        this.player = gameServer.getPlayer(this);
        Board board = gameServer.getBoard();
        clientApp.createView(player, board, this);
        this.view = clientApp.getView();
        view.giveMessage("","Ho ricevuto che il game è iniziato");
        view.gameStart();
    }

    @Override
    public List<String> getTargetsName(List<Player> players, int maxTargets) throws RemoteException {
        return view.getTargetsName(players, maxTargets);
    }

    @Override
    public List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom) throws RemoteException{
        return view.chooseIndexes(attacks, canRandom);
    }

    /**
     * this function set the nickname for the client
     * @param nickname to be set on this client
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }
    /**
     * this function returns the client's nickname
     * @return the client's nickname
     */
    public String getNickname(){
        return this.nickname;
    }

    private void changeTurn() throws RemoteException{
        this.gameServer.changeTurn(this);
    }

    /**
     * this funcion is called by remote Lobby to set on this client the gameServer assigned to play in
     * @param gameServer to play in
     */
    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
        try {
            clientLog("ho ricevuto il gameserver: " + gameServer.getGameToken());
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
            clientLog("mi sono ricollegato a: " + gameServer.getGameToken());
            return true;
        }
        else {
            clientLog("impossibile riconnettere");
            return false;
        }
    }

    /**
     * this function asks to the server for the player controller of this client (Player)
     * @return the controller associated to this client (Player)
     * @throws RemoteException error
     */
    public PlayerController getPlayerController() throws RemoteException{
        return gameServer.getPlayerController(this);
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

    public void init() {
        try {
            connect("127.0.0.1", 1099);
            Scanner scanner = new Scanner(System.in);
            clientLog("Enter username (min 3 chars max 15 chars)");
            String userName = scanner.nextLine();
            setNickname(userName);
            clientLog("Enter connect or reconnect");
            if (scanner.nextLine().equals("reconnect")) {
                clientLog("Enter your userToken");
                reconnect(userName, UUID.fromString(scanner.nextLine()));
            } else
                while (!register(getNickname())) {
                    clientLog("Enter another username");
                    userName = scanner.nextLine();
                    setNickname(userName);
                }
        } catch (RemoteException re) {
            re.fillInStackTrace();
        }
    }
    
    private void clientLog(String mex){
        System.out.println("ClRMI: " + mex);
    }

    public static void main(String[] args) {

        try {
            ClientRMI clientRMI = new ClientRMI();
            clientRMI.connect("127.0.0.1", 1099);
            Scanner scanner = new Scanner(System.in);
            clientRMI.clientLog("Enter username (min 3 chars max 15 chars)");
            String userName = scanner.nextLine();
            clientRMI.setNickname(userName);
            clientRMI.clientLog("Enter connect or reconnect");
            if(scanner.nextLine().equals("reconnect")) {
                clientRMI.clientLog("Enter your userToken");
                clientRMI.reconnect(userName, UUID.fromString(scanner.nextLine()));
            }
            else
                while (!clientRMI.register(clientRMI.getNickname())) {
                    clientRMI.clientLog("Enter another username");
                    userName = scanner.nextLine();
                    clientRMI.setNickname(userName);
                }

            String command = scanner.nextLine();
            while(!command.equals("exit")) {
                try {
                    if (command.equals("ct"))
                        clientRMI.changeTurn();
                }catch (RemoteException re) {
                    re.fillInStackTrace();
                    clientRMI.clientLog("errore di connessione");
                    System.exit(-1);
                }
                command = scanner.nextLine();
            }
        } catch (RemoteException re) {
            System.out.println(re.toString());
            re.fillInStackTrace();
            System.exit(-1);
        }
    }
}

