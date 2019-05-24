package server;

import client.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Lobby extends Remote {
    void login(String username, Client client) throws RemoteException;
}
