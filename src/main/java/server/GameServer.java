package server;

import client.Client;
import controller.CommandObj;
import controller.PlayerController;
import player.Player;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface GameServer extends Remote {
    UUID getGameToken() throws RemoteException;
    Player getPlayer(Client remoteClient) throws RemoteException;
    PlayerController getPlayerController(Client remoteClient) throws RemoteException;
    void changeTurn(Client remoteClient) throws RemoteException;
    void receiveCMD(CommandObj cmd, Client remoteClient) throws RemoteException;
}
