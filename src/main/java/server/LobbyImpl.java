package server;


import client.Client;
import constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * LobbyImpl is a server used to manage games
 */
public class LobbyImpl extends UnicastRemoteObject implements Lobby{
    private static final int MIN_PLAYERS = Constants.MIN_PLAYER.getValue();
    private static final int MAX_PLAYERS = Constants.MAX_PLAYER.getValue();
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{3,15}$";
    private int port;
    private int maxPlayers;
    private Map<String, ClientInfo> registeredClients;
    private Map<UUID, GameServerImpl> gamesList;
    private Registry registry;

    public LobbyImpl() throws RemoteException {
        this(1099, 5);
    }
    public LobbyImpl(int port, int maxPlayers) throws RemoteException{
        this.port = port;
        this.maxPlayers = maxPlayers;
        registeredClients = new HashMap<>();
        gamesList = new HashMap<>();
    }

    public int getNumGameserver(){
        return this.gamesList.size();
    }

    /**
     * this function start the server and let the server be able to accept new connections
     */
    public void start() {
        try {
            registry = LocateRegistry.createRegistry(port);
            LobbyImpl lobby = new LobbyImpl();
            registry.rebind("lobby", lobby);
            System.out.println("Server running");

        }
        catch (RemoteException re){
            System.err.println("Server exception: " + re.toString());
        }
    }

    /**
     * this funcrion create a new gameserver and add the first client
     * @param clientInfo to be added to the newly created game
     * @return the newly created gameserver
     * @throws RemoteException
     */
    private GameServerImpl createGame(ClientInfo clientInfo) throws RemoteException{
        System.out.println("generating a new gameserver");
        GameServerImpl game = new GameServerImpl(MIN_PLAYERS, MAX_PLAYERS);
        gamesList.put(game.getGameToken(), game);
        clientInfo.setGameToken(game.getGameToken());
        game.addClient(clientInfo);
        game.start();
        return game;
    }

    /**
     * this function is called by remote clients for registering to the lobby and to get personal userToken.
     * in case of success the player joins a free game or creates new one.
     * @param username of the client who wants to connect
     * @param remoteClient remote client reference
     * @return a new unique userToken to the client
     * @throws RemoteException
     */
    public synchronized UUID register(String username, Client remoteClient) throws RemoteException {
        System.out.println("_____________________________________________________________________");
        System.out.println("nuova richiesta di registrazione");
        if (checkNickname(username)) {
            UUID userToken = UUID.randomUUID();
            System.out.println("generated token: " + userToken + " for client: " + username );
            ClientInfo clientInfo = new ClientInfo(remoteClient, userToken, null);
            GameServerImpl gameServer = getFreeGameServer();
            if (gameServer != null) {
                clientInfo.setGameToken(gameServer.getGameToken());
                remoteClient.setGameServer(gameServer);
                gameServer.addClient(clientInfo);
            } else {
                gameServer = createGame(clientInfo);
                remoteClient.setGameServer(gameServer);
                clientInfo.setGameToken(gameServer.getGameToken());
            }
            registeredClients.put(username, clientInfo);
            System.out.println("now there are: "+getNumGameserver()+" active games");
            return userToken;
        }else{
            return null;
        }
    }

    /**
     * this function is called by remote clients for reconnecting to a prevoius gameserver.
     * @param nickname of the client who wants to reconnect
     * @param userToken prevoiusly given to the clients who wants to reconnect
     * @param newRemoteClient remote client new reference
     * @return last gameserver the client was playing in, or null if reconnecting isn't available
     */
    public synchronized GameServer reconnect(String nickname, UUID userToken, Client newRemoteClient) {

        if (registeredClients.containsKey(nickname))
            if (registeredClients.get(nickname).getUserToken().equals(userToken)) {
                System.out.println("inizio processo di riconnessione");
                registeredClients.get(nickname).setClient(newRemoteClient);
                if (gamesList.get(registeredClients.get(nickname).getGameToken()).updateClient(newRemoteClient, userToken)) {
                    return gamesList.get(registeredClients.get(nickname).getGameToken());
                } else
                    return null;
            } else
                return null;
        return null;

    }

    /**
     * this function search a free server from games list.
     * @return the gameserver if it finds a free server, else null.
     */
    private GameServerImpl getFreeGameServer(){
        System.out.println("cerco server liberi");
        return gamesList.values().stream().filter(x -> !(x.isFull()) && !x.isStarted()).findFirst().orElse(null);
    }

    /**
     * this function verifies if a nickname is available
     * @param nick to check if it's available
     * @return true if indicted nick is available, false if it's already used.
     */
    private boolean checkNickname(String nick){
        System.out.println("checking if "+nick+" already exist and if it's valid");
        return (!registeredClients.containsKey(nick) && validateNickname(nick));
    }

    /**
     * This function validates a nickname
     * valid chars: a-z, A-Z, 0-9, '.' '_' '-',
     * min lenght: 3, max length: 15
     * @param nick to be validated
     * @return true if it is fine, else false
     */
    private boolean validateNickname(String nick){
        if (nick==null)
            return false;
        else {
            Pattern pattern = Pattern.compile(USERNAME_PATTERN, CASE_INSENSITIVE);
            return (pattern.matcher(nick).matches());
        }
    }

    private static String askIp() throws IOException {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Set your IP address");
        return bufferRead.readLine();
    }

    public static void main(String[] args) throws IOException {
        String IP = askIp();
        System.setProperty("java.rmi.server.hostname", IP);

        try {
            LobbyImpl lobby = new LobbyImpl();
            lobby.start();
        } catch (RemoteException re) {
            re.fillInStackTrace();
        }
    }
}
