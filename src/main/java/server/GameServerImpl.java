package server;

import client.Client;
import controller.BoardController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {

    private transient BoardController boardController; //ho il riferimento al controller, però non lascio chiamare al client i suoi metodi
    private transient ArrayList<Client> clients;
    private int minPlayer;
    private int maxPlayer;
    private UUID gameToken;

    public GameServerImpl(int minPlayer, int maxPlayer) throws RemoteException {
        boardController = new BoardController();
        this.clients = new ArrayList<>();
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.gameToken = UUID.randomUUID();
    }

    public boolean isFull(){
        return (clients.size() >= maxPlayer);
    }
    public UUID getGameToken(){
        return this.gameToken;
    }
}