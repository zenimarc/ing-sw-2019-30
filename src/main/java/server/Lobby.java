package server;

import client.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface Lobby extends Remote {
    UUID register(String username, Client remoteClient) throws RemoteException;
}
