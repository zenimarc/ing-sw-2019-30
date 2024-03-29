package server;

import client.Client;
import player.Player;

import java.util.UUID;

public class ClientInfo {
    private Client client;
    private Player player;
    private UUID gameToken;
    private UUID userToken;
    private boolean isOffline;

    public ClientInfo(Client remoteClient, UUID userToken, UUID gameToken){
        this.client = remoteClient;
        this.userToken = userToken;
        this.gameToken = gameToken;
        this.isOffline = false;
    }
    public void setGameToken(UUID gameToken){
        this.gameToken = gameToken;
    }

    public UUID getGameToken(){
        return this.gameToken;
    }
    public UUID getUserToken(){
        return this.userToken;
    }
    public void setClient(Client remoteClient){
        this.client = remoteClient;
    }
    public void setPlayer(Player player){
        this.player = player;
    }
    public Client getClient(){
        return this.client;
    }
    public Player getPlayer() {return this.player;}
    public void setOff(){
        this.isOffline = true;
    }
    public void setOn(){
        this.isOffline = false;
    }
    public boolean isOffline(){
        return isOffline;
    }

    @Override
    public String toString() {
        return (String.valueOf(userToken) + " "+ gameToken);
    }
}

