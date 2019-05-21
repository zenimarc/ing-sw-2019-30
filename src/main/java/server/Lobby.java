package server;


import client.Client;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Lobby extends UnicastRemoteObject implements GameServer{

    private int port;
    private int maxPlayers;
    private ArrayList<Client> clientQueue;
    private Map<String, UUID> activeClients;
    private Map<UUID, GameServer> gamesList;
    private Registry registry;

    public Lobby() throws RemoteException {
        this(1999, 5);
    }
    public Lobby(int port, int maxPlayers) throws RemoteException{
        this.port = port;
        this.maxPlayers = maxPlayers;
        clientQueue = new ArrayList<>();
        activeClients = new HashMap<>();
    }

    public void start() {
        try {
            LocateRegistry.createRegistry(port);
            Lobby lobby = new Lobby();
            registry = LocateRegistry.getRegistry();
            registry.rebind("lobby", lobby);
            System.out.println("Server running");
        }
        catch (RemoteException re){
            System.err.println("Server exception: " + re.toString());
        }
    }

    public synchronized UUID createGame(List<Client> clients){
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
}
