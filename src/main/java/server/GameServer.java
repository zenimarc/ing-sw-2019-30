package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface GameServer extends Remote {
    UUID getGameToken() throws RemoteException;
}
