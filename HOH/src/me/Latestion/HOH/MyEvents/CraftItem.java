// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.inventory.CraftItemEvent;
import me.Latestion.HOH.Main;
import org.bukkit.event.Listener;

public class CraftItem implements Listener
{
    private Main plugin;
    
    public CraftItem(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void cie(final CraftItemEvent event) {
        if (this.plugin.gameOn) {
            if (event.getRecipe().getResult().getType() == Material.CRAFTING_TABLE) {
                event.setCancelled(true);
                final Player player = (Player)event.getWhoClicked();
                player.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("You cannot craft that!").toString());
            }
            if (event.getRecipe().getResult().getType() != Material.BEACON) {
                return;
            }
            event.setCancelled(true);
            final Player player = (Player)event.getWhoClicked();
            player.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("You cannot craft that!").toString());
        }
    }
}
