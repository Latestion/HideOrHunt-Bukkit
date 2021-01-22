package me.latestion.hoh.game;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class HOHTeam {

    public List<HOHPlayer> players = new ArrayList<>();
    public List<HOHPlayer> alivePlayers = new ArrayList<>();
    public boolean eliminated = false;
    private String name;
    private Integer id;
    private Block beacon;

    public HOHTeam(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HOHPlayer> getPlayers(){
        return this.players;
    }

    public void setPlayers(List<HOHPlayer> players) {
        this.players = players;
    }

    public void setAlivePlayers(List<HOHPlayer> alivePlayers) {
        this.alivePlayers = alivePlayers;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public void addPlayer(HOHPlayer hohPlayer) {
        players.add(hohPlayer);
        alivePlayers.add(hohPlayer);
    }

    public HOHPlayer getLeader() {
        return players.get(0);
    }

    public Block getBeacon() {
        return beacon;
    }

    public void setBeacon(Block blockPlaced) {
        this.beacon = blockPlaced;
    }

    public boolean hasBeacon() {
        return beacon != null;
    }

    public void diedPlayer(HOHPlayer player) {
        alivePlayers.remove(player);
        if (alivePlayers.size() == 0) {
            eliminated = true;
        }
        player.dead = true;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }
}
