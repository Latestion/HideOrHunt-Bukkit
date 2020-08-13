// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Latestion.HOH.Main;

public class InventoryClose implements Listener
{
    private Main plugin;
    
    public InventoryClose(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void close(final InventoryCloseEvent event) {
        if (this.plugin.cache2.contains(event.getPlayer())) {
            final Player player = (Player)event.getPlayer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable)new Runnable() {
                @Override
                public void run() {
                    player.openInventory(InventoryClose.this.plugin.inv);
                }
            }, 10L);
        }
    }
}
