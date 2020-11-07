package me.Latestion.HOH.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Latestion.HOH.Main;

public class InventoryClose implements Listener{
    private Main plugin;
    
    public InventoryClose(final Main plugin) {
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
