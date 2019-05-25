package server;


import client.Client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class LobbyImpl extends UnicastRemoteObject implements Lobby{
    private static final int MINPLAYERS = 3;
    private static final int MAXPLAYERS = 6;
    private int port;
    private int minPlayers;
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

    private GameServerImpl createGame(){
        try {
            GameServerImpl game = new GameServerImpl(MINPLAYERS, MAXPLAYERS);
            gamesList.put(game.getGameToken(), game);
            return game;
        }catch (RemoteException re){
            System.out.println(re.toString());
            re.fillInStackTrace();
        }
        return null;
    }

    public synchronized UUID register(String username, Client remoteClient) throws RemoteException {
        if (checkNickname(username)) {
            GameServerImpl gameServer = getFreeGameServer();
            if (gameServer != null) {
                remoteClient.setGameServer(gameServer);
            } else {
                gameServer = createGame();
                remoteClient.setGameServer(gameServer);
            }
            UUID userToken = UUID.randomUUID();
            System.out.println("generated token: " + userToken + " for client: " + username);
            ClientInfo clientInfo = new ClientInfo(remoteClient, userToken, gameServer.getGameToken());
            registeredClients.put(username, clientInfo);
            return userToken;
        }else{
            return null;
        }
    }
    public synchronized GameServer reconnect(String nickname, UUID userToken){
        if(registeredClients.containsKey(nickname))
            return gamesList.get(registeredClients.get(nickname).getGameToken());
        else
            return null;
    }

    private GameServerImpl getFreeGameServer(){
        return gamesList.values().stream().filter(x -> !(x.isFull())).findFirst().orElse(null);
    }

    /**
     * this function verifies if a nickname is available
     * @param nick to check if it's available
     * @return true if indicted nick is available, false if it's already used.
     */
    private boolean checkNickname(String nick){
        System.out.println("checking if "+nick+" already exist");
        return (!registeredClients.containsKey(nick));
    }

    public static void main(String[] args){
        try {
            LobbyImpl lobby = new LobbyImpl();
            lobby.start();
        }catch (RemoteException re){
            re.fillInStackTrace();
        }
    }
}
