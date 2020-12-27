package me.latestion.hoh.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import me.latestion.hoh.HideOrHunt;

public class HOHTeam {

	public List<HOHPlayer> players = new ArrayList<>();
	private String name;
	
	public List<HOHPlayer> alivePlayers = new ArrayList<>();
	
	public boolean eliminated = false;
	public boolean hasBeacon = false;
	
	private Block beacon;
	
	public HOHTeam(HideOrHunt plugin, String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
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
	
	public void diedPlayer(HOHPlayer player) {
		alivePlayers.remove(player);
		if (alivePlayers.size() == 0) {
			eliminated = true;
		}
		player.dead = true;
	}
	
}
