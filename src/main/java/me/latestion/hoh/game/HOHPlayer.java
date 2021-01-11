package me.latestion.hoh.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class HOHPlayer {

	private HOHGame game;
	private UUID uuid;
	private String name;
	private HOHTeam team = null;

	public boolean banned = false;
	public boolean dead = false;

	public boolean teamChat = false;
	private boolean namingTeam = false;

	public HOHPlayer(HOHGame game, UUID uuid, String name) {
		this.game = game;
		this.uuid = uuid;
		this.name = name;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public HOHTeam getTeam() {
		return team;
	}

	public UUID getUUID(){
		return uuid;
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
		this.team = t;
	}

	public void prepareTeam(Inventory inv) {
		if (game.gameState == GameState.PREPARE) {
			getPlayer().openInventory(inv);
		}
	}
	public String getName(){
		return this.name;
	}

}
