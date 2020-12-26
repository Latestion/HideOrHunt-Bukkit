package me.latestion.hoh.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import me.latestion.hoh.game.GameState;

public class CraftItem implements Listener {
	
    public CraftItem() {
    	
    }
    
    @EventHandler
    public void cie(CraftItemEvent event) {
        if (GameState.getCurrentGamestate() == GameState.ON) {
            if (event.getRecipe().getResult().getType() == Material.CRAFTING_TABLE || 
            		event.getRecipe().getResult().getType() == Material.BEACON) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "You cannot craft that!");
                return;
            }
        }
    }
}
