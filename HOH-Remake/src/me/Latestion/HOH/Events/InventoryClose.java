package me.Latestion.HOH.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Latestion.HOH.HideOrHunt;

public class InventoryClose implements Listener{
    private HideOrHunt plugin;
    
    public InventoryClose(final HideOrHunt plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (plugin.game.cache.contains(event.getPlayer())) {
            Player player = (Player) event.getPlayer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable)new Runnable() {
                @Override
                public void run() {
                    player.openInventory(plugin.inv);
                }
            }, 1L);
        }
    }
}
