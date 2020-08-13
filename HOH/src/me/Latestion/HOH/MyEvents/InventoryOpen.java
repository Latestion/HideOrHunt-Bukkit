// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import me.Latestion.HOH.Main;
import org.bukkit.event.Listener;

public class InventoryOpen implements Listener
{
    private Main plugin;
    
    public InventoryOpen(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void ioe(final InventoryOpenEvent event) {
        final Player player = (Player)event.getPlayer();
        if (this.plugin.gameOn && event.getInventory().getType().equals((Object)InventoryType.BEACON)) {
            event.setCancelled(true);
            player.openWorkbench((Location)null, true);
        }
    }
}
