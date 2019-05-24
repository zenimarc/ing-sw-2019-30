package server;


import client.Client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class LobbyImpl extends UnicastRemoteObject implements Lobby{

    private int port;
    private int minPlayers;
    private int maxPlayers;
    private ArrayList<Client> clientQueue;
    private Map<String, UUID> activeClients;
    private Map<UUID, GameServer> gamesList;
    private Registry registry;

    public LobbyImpl() throws RemoteException {
        this(1099, 5);
    }
    public LobbyImpl(int port, int maxPlayers) throws RemoteException{
        this.port = port;
        this.maxPlayers = maxPlayers;
        clientQueue = new ArrayList<>();
        activeClients = new HashMap<>();
        gamesList = new HashMap<>();
    }

    public void start() {
        try {
            registry = LocateRegistry.createRegistry(port);
            LobbyImpl lobby = new LobbyImpl();
            //registry = LocateRegistry.getRegistry();
            registry.rebind("lobby", lobby);
            System.out.println("Server running");
        }
        catch (RemoteException re){
            System.err.println("Server exception: " + re.toString());
        }
    }

    private UUID createGame(List<Client> clients){
        try {
            GameServerImpl game = new GameServerImpl(clients);
            UUID uuid = UUID.randomUUID();
            gamesList.put(uuid, game);
            registry.rebind(uuid.toString(), game);
            return uuid;
        }catch (RemoteException re){
            re.fillInStackTrace();
        }
        return null;
    }

    public void login(String username, Client client){
        this.clientQueue.add(client);
        try {
            client.loggedIn();
        }catch (RemoteException re){
            System.out.println("impossible to contact" + username);
        }
        System.out.println(username + " logged in");
    }

    public synchronized boolean join(Client client) {
        /*
        if (checkNickname(client.getName())) {
            clientQueue.add(client);
            if (clientQueue.size() >= minPlayers) {
                ArrayList<Client> clientsToAdd = new ArrayList<>();
                if (clientQueue.size() >= maxPlayers) {
                    for (int i = 0; i < maxPlayers; i++)
                        clientsToAdd.add(clientQueue.remove(i));
                } else {
                    for (int i = 0; i < minPlayers; i++)
                        clientsToAdd.add(clientQueue.remove(i));
                }
                createGame(clientsToAdd);
                return true;
            }
        }

         */
        return false;

    }

    /**
     * this function verifies if a nickname is available
     * @param nick to check if it's available
     * @return true if indicted nick is available, false if it's already used.
     */
    private boolean checkNickname(String nick){
        return (activeClients.keySet().stream().noneMatch(x -> x.equals(nick)));
    }


/*
    public synchronized void join(Client client){
       clientQueue.add(client);
       if (clientQueue.size() >= maxPlayers){
           for(int i=0; i<maxPlayers; i++){
               clientQueue.remove(i).gameStarted();
           }

       }

    }

 */
    public static void main(String[] args){
        try {
            LobbyImpl lobby = new LobbyImpl();
            lobby.start();
        }catch (RemoteException re){
            re.fillInStackTrace();
        }
    }
}
