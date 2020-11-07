package me.Latestion.HOH.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import me.Latestion.HOH.Main;
import me.Latestion.HOH.Game.GameState;

public class PlayerWorld implements Listener {
	
    private Main plugin;
    
    public PlayerWorld(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void chage(PlayerPortalEvent event) {
    	if (GameState.getCurrentGamestate() != GameState.ON) return;
        if (this.plugin.getConfig().getBoolean("Disable-Other-Worlds")) {
            event.setCancelled(true);
        }
    }
}
