package server;

import attack.Attack;
import board.Board;
import board.Cell;
import board.Position;
import client.Client;
import constants.Constants;
import controller.BoardController;
import controller.CommandObj;
import controller.PlayerController;
import player.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GameServerImpl is used to manage a single game
 */
public class GameServerImpl extends UnicastRemoteObject implements GameServer {
    private static final long SECONDS_BEFORE_START_GAME = Constants.SECONDS_BEFORE_START_GAME.getValue();;
    private transient BoardController boardController; //ho il riferimento al controller, però non lascio chiamare al client i suoi metodi
    private transient ArrayList<ClientInfo> clients;
    private transient ArrayList<Client> offlineClients;
    private int minPlayer;
    private int maxPlayer;
    private UUID gameToken;
    private boolean gameStarted;
    private int turn;
    private transient Thread beginCountdown;
    private transient ServerUpdateManager serverUpdateManager;

    public GameServerImpl(int minPlayer, int maxPlayer) throws RemoteException {
        this.clients = new ArrayList<>();
        this.offlineClients = new ArrayList<>();
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.gameToken = UUID.randomUUID();
        this.gameStarted = false;
        this.turn = 0;
        this.beginCountdown = new Thread();
    }



    public PlayerController getPlayerController(Client remoteClient) throws RemoteException{
        return boardController.getPlayerController(remoteClient.getNickname());
    }

    public Player getPlayer(Client remoteClient) throws RemoteException{
        return boardController.getPlayer(remoteClient.getNickname()).clonePlayer();
    }

    @Override
    public List<Player> getPlayers(){
        return boardController.getListOfPlayers();
    }

    @Override
    public Board getBoard() {
        return boardController.getBoard();
    }

    /**
     * this function checks if the server is full
     * @return true if it's full, and false if it isn't full.
     */
    public synchronized boolean isFull(){
        //System.out.println("ci sono "+clients.size()+" players" +" e il max e: "+ maxPlayer);
        return (clients.size() >= maxPlayer);
    }

    /**
     * this function returns the Client associated to the indicated Player
     * @param player associated to client
     * @return the client associated to Player or Null if not found
     */
    private synchronized Client getClient(Player player){
        Optional<ClientInfo> clientInfo = clients.stream().filter(x->x.getPlayer().equals(player)).findFirst();
        if(clientInfo.isPresent())
            return clientInfo.get().getClient();
        else
            return null;
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

    private void beginCountdown(){
        System.out.println(SECONDS_BEFORE_START_GAME + "seconds remaining before game starts");
        try {
            Thread.sleep(SECONDS_BEFORE_START_GAME * 1000);
            if(canBeginGame())
                startGame();
            else
                Thread.currentThread().interrupt();
        }catch (InterruptedException ie){
            Thread.currentThread().interrupt();
        }
    }

    /**
     * This function is used to begin the game
     */
    private void startGame(){
        System.out.println("il gameserver: "+this.getGameToken()+" ha startato il game");
        this.gameStarted = true;
        List<Player> players = new ArrayList<>();
        try {
            //this code create a Player for each active clients and store it in ClientsInfo list.
            for (Client remoteClient : getActiveClients()) {
                Player onePlayer = new Player(remoteClient.getNickname());
                Optional<ClientInfo> client = clients.stream().filter(x->x.getClient().equals(remoteClient)).findFirst();
                if (client.isPresent())
                    client.get().setPlayer(onePlayer);
                players.add(onePlayer);
            }
            boardController = new BoardController(players, 8);
            boardController.setGameServer(this);
            //Create ServerUpdate Manager
            serverUpdateManager = new ServerUpdateManager(this, boardController);
            //Add Observer to Board and Players
            boardController.getBoard().addObserver(serverUpdateManager);
            players.forEach(x -> x.addObserver(serverUpdateManager));
            //Start Game
            notifyAllGameStarted();
            boardController.firstPlay();
        }catch (RemoteException re){
            re.fillInStackTrace();
            //TODO cancel this game and notify players
        }
    }

    /**
     * This function notifies to all active clients that the game has started
     */
    private void notifyAllGameStarted(){
        for (Client client : getActiveClients())
            try {
                client.gameStarted();
            }catch (RemoteException re){
                re.fillInStackTrace();
            }
    }

    private void broadcastMsg(String msg){
        for (Client client : getActiveClients())
            try {
                client.showMsg(msg);
            }catch (RemoteException re){
                re.fillInStackTrace();
            }
    }

    /**
     * This command is used to send a command to a player
     * @param cmd to be sent
     * @param receiverPlayer of the command
     * @throws RemoteException error
     */
    void sendCMD(CommandObj cmd, Player receiverPlayer) throws RemoteException{
        Client remoteClient = getClient(receiverPlayer);
        if (remoteClient != null){
            remoteClient.receiveCMD(cmd);
        }
    }

    /**
     *
     * @param cmd
     * @param remoteClient
     * @throws RemoteException
     */
    public void receiveCMD(CommandObj cmd, Client remoteClient) throws RemoteException{
        serverUpdateManager.receiveCmd(cmd, getPlayer(remoteClient));
    }

    @Override
    public List<Player> getTargets(List<Player> potentialTarget, int maxTarget) throws RemoteException {
        Client client = getClient(boardController.getPlayer());

        if (client != null) {
            List<String> targetsName = client.getTargetsName(potentialTarget, maxTarget);
            return boardController.getListOfPlayers()
                    .stream()
                    .filter(x -> targetsName.contains(x.getName()))
                    .collect(Collectors.toList());
        }else return Collections.emptyList();
    }

    @Override
    public List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom) throws RemoteException {
        Client client = getClient(boardController.getPlayer());

        if(client!=null){
            return client.chooseIndexes(attacks, canRandom);
        }else return Collections.emptyList();
    }

    @Override
    public Position choosePositionToAttack(List<Cell> potentialCell) throws RemoteException {
        Client client = getClient(boardController.getPlayer());

        if(client!=null){
            List<Position> positions = new ArrayList<>();
            potentialCell
                    .forEach(x -> positions.add(boardController.getBoard().getBillboard().getCellPosition(x)));
            return client.choosePositionToAttack(positions);
        }
        return null;
    }

    /**
     * this function send to all clients a command object
     * @param commandObj command object coming from server
     */
    void sendToAll(CommandObj commandObj){
        for (Client client : getActiveClients()) {
            try {
                client.receiveCMD(commandObj);
            }catch (RemoteException re){
                re.fillInStackTrace();
            }
        }
    }


    private synchronized void endGame(){
        boardController.totalPoints();
        this.gameStarted = false;
        for(Client client : getActiveClients())
            try{
                client.timeExpired();
            }catch (RemoteException re){
                re.fillInStackTrace();
            }
        Thread.currentThread().interrupt();
    }

    /**
     * this functions add a ClientInfo data to the gameserver and check if the game can start,
     * in this case start a countdown to begin the game.
     * @param client is the ClientInfo of remote client
     */
    public synchronized void addClient(ClientInfo client){
        this.clients.add(client);
        System.out.println("ho appena aggiunto "+ client.getUserToken()+" al gameserver: "+this.gameToken+"\n ora ci sono "+
                clients.size()+" players\n servono almeno "+minPlayer+" players per iniziare il game");

        if (canBeginGame() && !beginCountdown.isAlive()) {
            beginCountdown = new Thread(this::beginCountdown);
            beginCountdown.start();
        }
    }

    @Deprecated
    public List<Client> getClientsFromPlayers(List<Player> players){
        return this.clients.stream().filter(x -> !x.isOffline()
                && players.contains(x.getPlayer())).map(ClientInfo::getClient).collect(Collectors.toList());
    }

    /**
     * this function checks if there are enough players to start the game
     * @return true if there are enough players to start, else false.
     */
    private boolean canBeginGame(){
        return getActiveClients().size()>=minPlayer;
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
    public void removeClient(Player player){
        try{
            Client client = this.getClient(player);
            swapOnOff(client);
            if (client!=null)
                client.timeExpired();
        }catch (RemoteException | NullPointerException ex){
            ex.fillInStackTrace();
        }
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
    private synchronized void swapOffOn(Client client){
        System.out.print("swapOffOn "+ client);
        Optional<ClientInfo> clientInfo = clients.stream().filter(x -> x.getClient().equals(client)).findFirst();
        if (clientInfo.isPresent()) {
            clientInfo.get().setOn();
        }

    }

    /**
     * this function set offline flag to true of the indicated client
     * @param client to be set as offline
     */
    public void swapOnOff(Client client){
        System.out.print("swapOnOff "+ client);
        Optional<ClientInfo> clientInfo = clients.stream().filter(x -> x.getClient().equals(client)).findFirst();
        if (clientInfo.isPresent()) {
            clientInfo.get().setOff();
            boardController.suspend(clientInfo.get().getPlayer());
            broadcastMsg(clientInfo.get().getPlayer().getName()+" ha lasciato il gioco");
            if(boardController.getNumActivePlayers()<minPlayer)
                endGame();
        }
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
            boardController.reactivate(clientInfo.get().getPlayer());
            return true;
        }
        return false;
    }

    @Override
    public void askStatus(Client remoteClient) throws RemoteException {
        Player p = getPlayer(remoteClient);
        if (p != null)
            boardController.updatePlayerMenuStatus(p);
    }

    /**
     * This functions ping every clients of the game and if one client is offline, starts a timer to delete him.
     */
    public void checkOfflineClients(){
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
                    //new Thread(() -> kick(client)).start(); TODO: mettere se si vuole impedire riconnessione dopo tot secs.
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
    public void kick(Client client){
        final long INTERVAL = (Constants.SECONDS_BEFORE_DEFINITELY_KICK_A_PLAYER.getValue())/10; //Ping interval seconds
        final int TIMES = 10; //times to ping
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
                re.fillInStackTrace();
            }catch (InterruptedException ie){
                ie.fillInStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        removeClient(client);
        System.out.println("\n"+client + " removed for inactivity");
        if(this.clients.size()<this.minPlayer) {
            System.out.println("termino partita per pochi client");
            endGame();
        }
    }
    @Deprecated
    public synchronized int changeTurn() {

        boardController.changeTurn();
        System.out.println("server: "+this.getGameToken()+"\nè il turno di "+this.getTurn());
        return turn;
    }

    public synchronized int getTurn(){
        return this.turn;
    }

    @Deprecated
    public synchronized void changeTurn(Client remoteClient){
        if(clients.get(this.getTurn()).getClient().equals(remoteClient))
            this.changeTurn();
    }

    /**
     * this function start a new gameserver with offline clients watchdog
     */
    public void start(){
        new Thread(this::checkOfflineClients).start();
    }
}