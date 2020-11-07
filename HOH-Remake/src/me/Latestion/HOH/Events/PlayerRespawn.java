package me.Latestion.HOH.Events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.Latestion.HOH.Main;
import me.Latestion.HOH.Game.GameState;

public class PlayerRespawn implements Listener {

	private Main plugin;
	
	public PlayerRespawn(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void repawn(PlayerRespawnEvent event) {
		if (GameState.getCurrentGamestate() == GameState.ON) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	            	for (String s : plugin.getConfig().getStringList("Item-On-Respawn")) {
	            		String[] split = s.split(", ");
	            		ItemStack item = new ItemStack(Material.matchMaterial(split[0].toUpperCase()), Integer.parseInt(split[1]));
	            		event.getPlayer().getInventory().addItem(item);
	            	}
	            } 
	        }, 1);
			event.setRespawnLocation(plugin.hohPlayer.get(event.getPlayer().getUniqueId()).getTeam().getBeacon().getLocation().clone().add(0, 1, 0));
		}
	}
}
