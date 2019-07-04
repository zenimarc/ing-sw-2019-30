package server;

import board.Board;
import controller.BoardController;
import controller.CommandObj;
import controller.EnumCommand;
import controller.PlayerController;
import player.Player;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

/**
 * ServerUpdateManager is used to notify
 */
public class ServerUpdateManager implements Observer {
    private BoardController boardController;
    private GameServerImpl gameServer;

    /**
     * Default constructor
     */
    public ServerUpdateManager(GameServerImpl gameServer, BoardController boardController){
        this.gameServer = gameServer;
        this.boardController = boardController;
        for (Player player : boardController.getListOfPlayers()) {
            player.addObserver(this);
            boardController.getPlayerController(player).addObserver(this);
        }

    }

    /**
     * This function is used to send commands to a player
     * @param cmd to be received
     * @param player to receive command
     */
    public void receiveCmd(CommandObj cmd, Player player){
        PlayerController playerController = boardController.getPlayerController(player);
        playerController.receiveCmd(cmd);
    }

    /**
     * This function updates  and notifies objects
     * @param observable observable
     * @param obj obj
     */
    @Override
    public void update(Observable observable, Object obj) {
        //In this case the update come from a player, so send to all client the player cloned to get infos
        if (observable.getClass().equals(Player.class)) {
            try {
                gameServer.sendCMD(new CommandObj(EnumCommand.UPDATE_PLAYER, obj), boardController.getListOfPlayers().stream().
                        filter(x -> x.getName().equals(((Player) obj).getName())).findFirst().orElse(null));
            }catch (RemoteException r){
                r.getMessage();
            }
        }

        if(observable.getClass().equals(Board.class)){
            gameServer.sendToAll(new CommandObj(EnumCommand.UPDATE_BOARD, obj));
        }

        //in this case the update come from a PlayerController so, the object is a EnumCommand
        if (observable.getClass().equals(PlayerController.class)){
            try{
                gameServer.sendCMD((CommandObj)obj, ((PlayerController) observable).getPlayer());
            }catch (RemoteException re){
                re.fillInStackTrace();
            }
        }

    }
}
