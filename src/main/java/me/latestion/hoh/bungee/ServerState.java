package me.latestion.hoh.bungee;

public class ServerState {

    public String name;
    public boolean game;
    public int teamsize;
    public int maxPlayers;

    public int totalOnlinePlayers = 0;

    public ServerState(String name, int teamsize, int maxPlayers) {
        this.name = name;
        this.game = false;
        this.teamsize = teamsize;
        this.maxPlayers = maxPlayers;
    }

}
