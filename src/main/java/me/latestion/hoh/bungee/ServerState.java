package me.latestion.hoh.bungee;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerState {

    public String name;
    public boolean game;
    public int teamsize;
    public int maxPlayers;

    public List<UUID> queue = new ArrayList<>();

    public ServerState(String name, int teamsize, int maxPlayers) {
        this.name = name;
        this.game = false;
        this.teamsize = teamsize;
        this.maxPlayers = maxPlayers;
    }

}
