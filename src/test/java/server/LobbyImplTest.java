package server;

import client.Client;
import client.ClientRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class LobbyImplTest {
    LobbyImpl lobby;
    Client client;

    @Test
    public void start(){
        try{
            LobbyImpl lobby = new LobbyImpl();
            lobby.start();
            assertNotNull(lobby.register("test", new ClientRMI()));
        }catch (RemoteException re) {
            re.fillInStackTrace();
        }
    }
}