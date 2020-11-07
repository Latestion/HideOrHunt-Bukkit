package me.Latestion.HOH.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import me.Latestion.HOH.HideOrHunt;
import me.Latestion.HOH.Game.GameState;

public class PlayerWorld implements Listener {
	
    private HideOrHunt plugin;
    
    public PlayerWorld(HideOrHunt plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void change(PlayerPortalEvent event) {
    	if (GameState.getCurrentGamestate() != GameState.ON) return;
        if (this.plugin.getConfig().getBoolean("Disable-Other-Worlds")) {
            event.setCancelled(true);
        }
    }
}
