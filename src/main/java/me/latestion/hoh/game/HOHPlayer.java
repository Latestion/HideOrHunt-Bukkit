package me.latestion.hoh.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.latestion.hoh.HideOrHunt;

public class HOHPlayer {
	
	private HideOrHunt plugin;
	private UUID player;
	private HOHTeam team;
	
	public boolean banned = false;
	public boolean dead = false;
	
	public boolean teamChat = false;
	
	public HOHPlayer(HideOrHunt plugin, UUID player) {
		this.plugin = plugin;
		this.player = player;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}
	
	public HOHTeam getTeam() {
		return team;
	}
	
	public void setTeam(HOHTeam t) {
		if (GameState.getCurrentGameState() != GameState.PREPARE) return;
		this.team = t;
        plugin.hohTeam.put(t.getName(), team);
        team.addPlayer(plugin.hohPlayer.get(player));
        plugin.game.addTeam(team);
        plugin.cache.remove(plugin.chat.indexOf(getPlayer()));
        plugin.chat.remove(getPlayer());
		
	}

	public void prepareTeam(Inventory inv) {
		if (GameState.getCurrentGameState() == GameState.PREPARE) {
			getPlayer().openInventory(inv);	
		}
	}
	
}
