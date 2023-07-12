package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftItem implements Listener {

	private HideOrHunt plugin;

	public CraftItem(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void cie(CraftItemEvent event) {
		if (plugin.game.getGameState() == GameState.ON) {
			if (event.getRecipe().getResult().getType() == Material.CRAFTING_TABLE ||
					event.getRecipe().getResult().getType() == Material.BEACON) {
				event.setCancelled(true);
				Player player = (Player) event.getWhoClicked();
				player.sendMessage(plugin.getMessageManager().getMessage("cannot-craft"));
			}
		}
	}
}
