package client;

import player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void loggedIn() throws RemoteException;
    void gameStarted(Object obj) throws RemoteException;
}
