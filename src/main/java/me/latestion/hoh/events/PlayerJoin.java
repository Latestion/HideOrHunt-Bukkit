package me.latestion.hoh.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;

public class PlayerJoin implements Listener {
	
    private HideOrHunt plugin;
    
    public PlayerJoin(HideOrHunt plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void pje(final PlayerJoinEvent event) {
        if (GameState.getCurrentGameState() == GameState.ON) {
            plugin.sbUtil.addPlayer(event.getPlayer());
            if (!plugin.game.bar.getBar().getPlayers().contains(event.getPlayer())) plugin.game.bar.addPlayer(event.getPlayer());
            return;
        }
        event.getPlayer().teleport(event.getPlayer().getLocation().getWorld().getSpawnLocation());
        
    }
}
