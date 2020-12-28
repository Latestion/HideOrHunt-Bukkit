package me.latestion.hoh.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.latestion.hoh.HideOrHunt;

public class HOHPlayer {

	private HideOrHunt plugin;
	private UUID player;
	private HOHTeam team = null;

	public boolean banned = false;
	public boolean dead = false;

	public boolean teamChat = false;
	private boolean namingTeam = false;

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

	public boolean hasTeam() {
		return this.team != null;
	}

	public void setNamingTeam(boolean namingTeam) {
		this.namingTeam = namingTeam;
	}

	public boolean isNamingTeam() {
		return this.namingTeam;
	}

	public void setTeam(HOHTeam t) {
		if (GameState.getCurrentGameState() != GameState.PREPARE) return;
		this.team = t;
		team.addPlayer(this);
	}

	public void prepareTeam(Inventory inv) {
		if (GameState.getCurrentGameState() == GameState.PREPARE) {
			getPlayer().openInventory(inv);
		}
	}

}
