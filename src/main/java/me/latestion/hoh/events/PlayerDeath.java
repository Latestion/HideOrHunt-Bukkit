package me.latestion.hoh.events;

import java.util.Random;
import java.util.UUID;

import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.utils.Util;
import org.bukkit.inventory.ItemStack;

public class PlayerDeath implements Listener {

	private HideOrHunt plugin;
	private Util util;

	public PlayerDeath(HideOrHunt plugin) {
		this.plugin = plugin;
		this.util = new Util(plugin);
	}

	@EventHandler
	public void death(PlayerDeathEvent event) {
		if (plugin.game.gameState == GameState.ON) {
			HOHPlayer player = plugin.game.hohPlayers.get(event.getEntity().getUniqueId());
			MessageManager messageManager = plugin.getMessageManager();

			event.getDrops().stream().filter(i -> i.getType().equals(Material.BEACON)).forEach(i -> event.getDrops().remove(i));

			if (!player.getTeam().hasBeacon()) {
				plugin.getGame().eliminatePlayer(player);
			} else {
				Random rand = new Random();
				int i = rand.nextInt(100);
				if (this.plugin.getConfig().getInt("Inventory-Keep") == 0) {
					return;
				}
				if (i + 1 <= this.plugin.getConfig().getInt("Inventory-Keep")) {
					event.setKeepInventory(true);
					event.getDrops().clear();
					player.getPlayer().sendMessage(messageManager.getMessage("kept-inventory"));
				} else {
					player.getPlayer().sendMessage(messageManager.getMessage("lost-inventory"));
				}
			}

			for(ItemStack it : event.getDrops()){
				if(it != null){
					if(it.getType().equals(Material.BEACON)){
						it.setType(Material.AIR);
					}
				}
			}
		}
	}
}
