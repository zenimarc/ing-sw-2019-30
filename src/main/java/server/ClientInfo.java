package server;

import client.Client;

import java.util.UUID;

public class ClientInfo {
    private Client client;
    private UUID gameToken;
    private UUID userToken;

    public ClientInfo(Client remoteClient, UUID userToken, UUID gameToken){
        this.client = remoteClient;
        this.userToken = userToken;
        this.gameToken = gameToken;
    }
    public UUID getGameToken(){
        return this.gameToken;
    }
    public UUID getUserToken(){
        return this.userToken;
    }
    public Client getClient(){
        return this.client;
    }

}

