// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import me.Latestion.HOH.Main;
import org.bukkit.event.Listener;

public class PlayerMove implements Listener
{
    private Main plugin;
    
    public PlayerMove(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void pme(final PlayerMoveEvent event) {
        if (this.plugin.freeze) {
            if (event.getPlayer().isOp() && !this.plugin.getConfig().getBoolean("Allow-Op")) {
                event.setCancelled(false);
            }
            else {
                event.setCancelled(true);
            }
        }
    }
}
