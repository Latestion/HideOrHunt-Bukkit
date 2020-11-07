package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;
import me.Latestion.HOH.Main;
import org.bukkit.event.Listener;

public class PlayerWorld implements Listener
{
    private Main plugin;
    
    public PlayerWorld(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void change(PlayerPortalEvent event) {
        if (this.plugin.gameOn && this.plugin.getConfig().getBoolean("Disable-Other-Worlds")) {
            event.setCancelled(true);
        }
    }
}
