package server;

import controller.BoardController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {

    private BoardController boardController; //ho il riferimento al controller, però non lascio chiamare al client i suoi metodi

    public GameServerImpl() throws RemoteException{


    }

    public static void main(String args[]) {
        try {
            LocateRegistry.createRegistry(1099);
            GameServerImpl gameServer = new GameServerImpl();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("game-server", gameServer);
            System.out.println("Server running");
        }
        catch (RemoteException re){
            System.err.println("Server exception: " + re.toString());
        }
    }
}
