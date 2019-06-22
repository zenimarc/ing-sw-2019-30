package client;

import controller.CommandObj;
import player.Player;
import server.GameServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void loggedIn() throws RemoteException;
    void setGameServer(GameServer gameServer) throws RemoteException;
    boolean isActive() throws RemoteException;
    void timeExpired() throws RemoteException;
    String getNickname() throws RemoteException;
    void setPlayer(Player player) throws RemoteException;
    Player getPlayer() throws RemoteException;
    void receiveCMD(CommandObj cmd) throws RemoteException;
 //   void receiveObj(Object obj) throws RemoteException;
    void gameStarted() throws RemoteException;

}
