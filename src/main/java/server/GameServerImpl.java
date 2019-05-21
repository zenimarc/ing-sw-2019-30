package server;

import client.Client;
import controller.BoardController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {

    private BoardController boardController; //ho il riferimento al controller, però non lascio chiamare al client i suoi metodi
    private ArrayList<Client> clients;

    public GameServerImpl(List<Client> clients) throws RemoteException {
        boardController = new BoardController();
        this.clients.addAll(clients);
    }
}