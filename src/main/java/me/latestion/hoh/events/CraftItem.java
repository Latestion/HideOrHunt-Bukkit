package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import me.latestion.hoh.game.GameState;

public class CraftItem implements Listener {

	private HideOrHunt plugin;

	public CraftItem(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void cie(CraftItemEvent event) {
		if (plugin.game.gameState == GameState.ON) {
			if (event.getRecipe().getResult().getType() == Material.CRAFTING_TABLE ||
					event.getRecipe().getResult().getType() == Material.BEACON) {
				if (plugin.game.allowCrafting) {
					return;
				}
				event.setCancelled(true);
				Player player = (Player) event.getWhoClicked();
				player.sendMessage(plugin.getMessageManager().getMessage("cannot-craft"));
			}
		}
	}
}
