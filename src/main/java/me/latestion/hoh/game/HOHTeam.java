package me.latestion.hoh.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import me.latestion.hoh.HideOrHunt;

public class HOHTeam {

	private String name;
	private Integer id;

	public List<HOHPlayer> players = new ArrayList<>();

	public List<HOHPlayer> alivePlayers = new ArrayList<>();

	public boolean eliminated = false;

	private Block beacon;

	public HOHTeam(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
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

	public void setName(String name) {
		this.name = name;
	}

	public void addPlayer(HOHPlayer hohPlayer) {
		players.add(hohPlayer);
		alivePlayers.add(hohPlayer);
	}

	public HOHPlayer getLeader() {
		return players.get(0);
	}

	public void setBeacon(Block blockPlaced) {
		this.beacon = blockPlaced;
	}

	public Block getBeacon() {
		return beacon;
	}

	public boolean hasBeacon(){
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
