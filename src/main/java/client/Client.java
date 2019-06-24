package client;

import attack.Attack;
import board.Position;
import controller.CommandObj;
import player.Player;
import server.GameServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

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

    /**
     * Ask player opponents to hit
     * @param players potential targets
     * @param maxTargets max targets to hit
     * @return List of opponents name to shoot
     * @throws RemoteException
     */
    List<String> getTargetsName(List<Player> players, int maxTargets) throws  RemoteException;
    List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom) throws RemoteException;
    Position choosePositionToAttack(List<Position> potentialposition) throws RemoteException;
}
