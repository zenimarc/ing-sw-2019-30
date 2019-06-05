package server;

import client.Client;
import controller.BoardController;
import controller.PlayerController;
import player.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {

    private transient BoardController boardController; //ho il riferimento al controller, però non lascio chiamare al client i suoi metodi
    private transient ArrayList<ClientInfo> clients;
    private transient ArrayList<Client> offlineClients;
    private int minPlayer;
    private int maxPlayer;
    private UUID gameToken;
    private boolean gameStarted;
    private int turn;

    public GameServerImpl(int minPlayer, int maxPlayer) throws RemoteException {
        boardController = new BoardController();
        this.clients = new ArrayList<>();
        this.offlineClients = new ArrayList<>();
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.gameToken = UUID.randomUUID();
        this.gameStarted = false;
        this.turn = 0;
    }

    /**
     * this function checks if the server is full
     * @return true if it's full, and false if it isn't full.
     */
    public boolean isFull(){
        System.out.println("ci sono "+clients.size()+" players" +" e il max e: "+ maxPlayer);
        return (clients.size() >= maxPlayer);
    }

    public PlayerController getPlayerController(Client remoteClient) throws RemoteException{
        return boardController.getPlayerController(remoteClient.getNickname());
    }

    public Player getPlayer(Client remoteClient) throws RemoteException{
        return boardController.getPlayer(remoteClient.getNickname());
    }

    /**
     * this function returns the gameToken of the current gameServer
     * @return the gameToken
     */
    public UUID getGameToken(){
        return this.gameToken;
    }

    public synchronized boolean isStarted(){
        return this.gameStarted;
    }

    public synchronized void startGame(){
        //TODO: far partire il gioco e avvisare tutti i client
        this.gameStarted = true;
        new Thread(new TurnHandler(this)).start();
        List<Player> players = new ArrayList<>();
        try {
            for (Client remoteClient : getActiveClients()) {
                players.add(new Player(remoteClient.getNickname()));
            }
        }catch (RemoteException re){
            //TODO cancel this game and notify players
        }
    }

    public synchronized void endGame(){
        this.gameStarted = false;
    }

    /**
     * this functions add a ClientInfo data to the gameserver
     * @param client is the ClientInfo of remote client
     */
    public synchronized void addClient(ClientInfo client){
        this.clients.add(client);
        System.out.println("ho appena aggiunto "+ client);
    }

    /**
     * this function removes the indicated remote Client
     * @param client to be definitely removed from the game.
     */
    public synchronized void removeClient(Client client){
        Optional<ClientInfo> clientInfo = this.clients.stream().filter(x -> x.getClient().equals(client)).findFirst();
        if (clientInfo.isPresent())
            clients.remove(clientInfo.get());
    }
    public synchronized List<Client> getClients(){
        return this.clients.stream().map(ClientInfo::getClient).collect(Collectors.toList());
    }

    /**
     * this functino extrapolate a list of active Client from the list of ClientInfo
     * @return a list of active Clients
     */
    public synchronized List<Client> getActiveClients() {
        return this.clients.stream().filter(x -> !x.isOffline()).map(ClientInfo::getClient).collect(Collectors.toList());
    }
    public synchronized void swapOffOn(Client client){
        System.out.print("swapOffOn "+ client);
        Optional<ClientInfo> clientInfo = clients.stream().filter(x -> x.getClient().equals(client)).findFirst();
        if (clientInfo.isPresent())
            clientInfo.get().setOn();
    }

    /**
     * this function set offline flag to true of the indicated client
     * @param client to be set as offline
     */
    public synchronized void swapOnOff(Client client){
        System.out.print("swapOnOff "+ client);
        Optional<ClientInfo> clientInfo = clients.stream().filter(x -> x.getClient().equals(client)).findFirst();
        if (clientInfo.isPresent())
            clientInfo.get().setOff();
    }

    /**
     * this function update data of a reconnected client
     * in particular set the new correct remote client reference and set it as online.
     * @param newRemoteClient new reference to the remote client
     * @param userToken of the remote client who's reconnecting
     * @return true if the remote client reconnected successfully, False if it's impossible to reconnect.
     */
    public synchronized boolean updateClient(Client newRemoteClient, UUID userToken){
        Optional<ClientInfo> clientInfo = clients.stream().filter(x -> x.getUserToken().equals(userToken)).findFirst();
        System.out.println("sto aggiornando client, ho trovato user: "+ clientInfo.isPresent());
        if (clientInfo.isPresent()) {
            System.out.println("setto nuovo remoteclient: " + newRemoteClient+" e metto on");
            clients.get(clients.indexOf(clientInfo.get())).setClient(newRemoteClient);
            clients.get(clients.indexOf(clientInfo.get())).setOn();
            return true;
        }
        return false;
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
                for (Client client : getActiveClients()) {
                    try {
                        client.isActive();
                    } catch (RemoteException re) {
                        clientsToBeSuspended.add(client);
                    }
                }
                for (Client client : clientsToBeSuspended) {
                    swapOnOff(client);
                    new Thread(() -> kick(client)).start();
                    //TODO: qui magari fare thread pool e far partire il countdown timer thread senza lambda.
                }
            }catch (InterruptedException ie){
                ie.fillInStackTrace();
                Thread.currentThread().interrupt();
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
        int TIMES = 10; //times to ping
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
                Thread.currentThread().interrupt();
            }
        }
        removeClient(client);
        System.out.println(client + " removed for inactivity");
    }

    public synchronized int changeTurn() {
        if(turn >= clients.size()-1)
            turn = 0;
        else turn++;
        return turn;
    }

    public synchronized int getTurn(){
        return this.turn;
    }

    /**
     * this function start a new gameserver with offline clients watchdog
     */
    public void start(){
        new Thread(this::checkOfflineClients).start();
        startGame();
        //TODO: far partire il checkoogìfline e anche il timer del turno (2 min per azione)
    }
}