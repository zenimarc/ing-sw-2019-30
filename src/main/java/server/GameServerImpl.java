package server;

import client.Client;
import controller.BoardController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {

    private transient BoardController boardController; //ho il riferimento al controller, però non lascio chiamare al client i suoi metodi
    private transient ArrayList<Client> clients;
    private transient ArrayList<Client> offlineClients;
    private int minPlayer;
    private int maxPlayer;
    private UUID gameToken;

    public GameServerImpl(int minPlayer, int maxPlayer) throws RemoteException {
        boardController = new BoardController();
        this.clients = new ArrayList<>();
        this.offlineClients = new ArrayList<>();
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.gameToken = UUID.randomUUID();
    }

    public boolean isFull(){
        System.out.println("ci sono "+clients.size()+" players" +" e il max e: "+ maxPlayer);
        return (clients.size() + offlineClients.size() >= maxPlayer);
    }
    public UUID getGameToken(){
        return this.gameToken;
    }
    public synchronized void addClient(Client client){
        this.clients.add(client);
    }
    public synchronized void removeClient(Client client){
        this.offlineClients.remove(client);
    }
    public synchronized List<Client> getClients(){
        return this.clients;
    }
    public synchronized void swapOffOn(Client client){
        this.offlineClients.remove(client);
        this.clients.add(client);
    }
    public synchronized void swapOnOff(Client client){
        System.out.print(client);
        this.clients.remove(client);
        this.offlineClients.add(client);
    }

    /**
     * This functions ping every clients of the game and if one client is offline, starts a timer to delete him.
     */
    private void checkOfflineClients(){
        List<Client> clientsToBeSuspended = new ArrayList<>();
        while(true) {
            clientsToBeSuspended.clear();
            try {
                Thread.sleep(5000);
                for (Client client : getClients()) {
                    try {
                        client.isActive();
                    } catch (RemoteException re) {
                        clientsToBeSuspended.add(client);
                    }
                }
                for (Client client : clientsToBeSuspended){
                    swapOnOff(client);
                    new Thread(() -> kick(client)).start();
                }
            }catch (InterruptedException ie){
                ie.fillInStackTrace();
            }
        }
    }

    /**
     * This functions monitors a remote client by pinging every INTERVAL seconds
     * if after TIMES times the client doesn't reply he will be removed from the game
     * @param client to be monitored
     */
    private void kick(Client client){
        int INTERVAL = 3; //Ping interval seconds
        int TIMES = 2; //times to ping
        int i=0;
        while(i<TIMES){
            try {
                Thread.sleep(1000*INTERVAL);
                i++;
                if(client.isActive()) {
                    swapOffOn(client);
                    return;
                }
            }catch (RemoteException re){
                continue;
            }catch (InterruptedException ie){
                ie.fillInStackTrace();
            }
        }
        removeClient(client);
        System.out.println(client + " removed for inactivity");
    }

    public void start(){
        new Thread(() -> checkOfflineClients()).start();
    }
}