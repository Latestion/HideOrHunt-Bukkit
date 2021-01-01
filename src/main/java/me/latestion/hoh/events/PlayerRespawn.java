package me.latestion.hoh.events;

import me.latestion.hoh.game.HOHGame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;

public class PlayerRespawn implements Listener {

	private HideOrHunt plugin;

	public PlayerRespawn(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void repawn(PlayerRespawnEvent event) {
		if (plugin.game.gameState == GameState.ON) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					for (String s : plugin.getConfig().getStringList("Item-On-Respawn")) {
						String[] split = s.split(", ");
						ItemStack item = new ItemStack(Material.matchMaterial(split[0].toUpperCase()), Integer.parseInt(split[1]));
						event.getPlayer().getInventory().addItem(item);
					}
				}
			}, 1);
			event.setRespawnLocation(plugin.game.hohPlayers.get(event.getPlayer().getUniqueId()).getTeam().getBeacon().getLocation().clone().add(0, 1, 0));
		}
	}
}
