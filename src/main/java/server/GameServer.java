package server;

import client.Client;
import controller.PlayerController;
import player.Player;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface GameServer extends Remote {
    UUID getGameToken() throws RemoteException;
    Player getPlayer(Client remoteClient) throws RemoteException;
    PlayerController getPlayerController(Client remoteClient) throws RemoteException;
}
