package me.Latestion.HOH.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Latestion.HOH.Main;
import me.Latestion.HOH.Game.GameState;

public class PlayerJoin implements Listener {
	
    private Main plugin;
    
    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void pje(final PlayerJoinEvent event) {
        if (GameState.getCurrentGamestate() == GameState.ON) {
            plugin.sbUtil.addPlayer(event.getPlayer());
            if (!plugin.game.bar.getBar().getPlayers().contains(event.getPlayer())) plugin.game.bar.addPlayer(event.getPlayer());
            return;
        }
        event.getPlayer().teleport(event.getPlayer().getLocation().getWorld().getSpawnLocation());
        
    }
}
