package me.Latestion.HOH.Game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.Latestion.HOH.HideOrHunt;

public class HOHPlayer {
	
	@SuppressWarnings("unused")
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
		if (GameState.getCurrentGamestate() == GameState.PREPARE)
		this.team = t;
	}

	public void prepareTeam(Inventory inv) {
		if (GameState.getCurrentGamestate() == GameState.PREPARE) {
			getPlayer().openInventory(inv);	
		}
	}
	
}
