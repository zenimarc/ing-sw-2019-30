package server;

import controller.BoardController;
import controller.CommandObj;
import controller.PlayerController;
import player.Player;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

public class ServerUpdateManager implements Observer {
    private BoardController boardController;
    private GameServerImpl gameServer;

    public ServerUpdateManager(GameServerImpl gameServer, BoardController boardController) throws RemoteException {
        this.gameServer = gameServer;
        this.boardController = boardController;
        for (Player player : boardController.getListOfPlayers()) {
            player.addObserver(this);
            boardController.getPlayerController(player).addObserver(this);
        }

    }

    public void receiveCmd(CommandObj cmd, Player player){
        PlayerController playerController = boardController.getPlayerController(player);
        playerController.receiveCmd(cmd);
    }

    @Override
    public void update(Observable observable, Object obj) {
        //In this case the update come from a player, so send to all client the player cloned to get infos
        if (observable.getClass().equals(Player.class)) {
            gameServer.sendToAll((Player) obj);

        }
        //in this case the update come from a PlayerController so, the object is a PlayerCommand
        if (observable.getClass().equals(PlayerController.class)){
            try{
                gameServer.sendCMD((CommandObj)obj, ((PlayerController) observable).getPlayer());
            }catch (RemoteException re){
                re.fillInStackTrace();
            }
        }

    }
}
