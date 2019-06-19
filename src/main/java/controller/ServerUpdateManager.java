package controller;

import player.Player;
import server.GameServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;

public class ServerUpdateManager extends UnicastRemoteObject implements RemoteController, Observer {
    private BoardController boardController;
    private GameServer gameServer;

    public ServerUpdateManager(GameServer gameServer, BoardController boardController) throws RemoteException {
        this.gameServer = gameServer;
        this.boardController = boardController;
        for (Player player : boardController.getListOfPlayers())
            player.addObserver(this);
    }

    @Override
    public void update(Observable player, Object obj) {
        if (obj.getClass().equals(Player.class)) {
            try {
                gameServer.sendToAllCMD(((CommandObj) obj));
            } catch (RemoteException re) {
                re.fillInStackTrace();
            }
        }
    }
}
