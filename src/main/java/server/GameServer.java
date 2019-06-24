package server;

import attack.Attack;
import board.Board;
import board.Cell;
import board.Position;
import client.Client;
import controller.CommandObj;
import controller.PlayerController;
import player.Player;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GameServer extends Remote {
    UUID getGameToken() throws RemoteException;
    Player getPlayer(Client remoteClient) throws RemoteException;
    PlayerController getPlayerController(Client remoteClient) throws RemoteException;
    void changeTurn(Client remoteClient) throws RemoteException;
    void receiveCMD(CommandObj cmd, Client remoteClient) throws RemoteException;
    Board getBoard() throws RemoteException;
    List<Player> getTargets(List<Player> potentialTarget, int maxTarget) throws RemoteException;
    List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom) throws RemoteException;
    Position choosePositionToAttack(List<Cell> potentialCell) throws RemoteException;
}
