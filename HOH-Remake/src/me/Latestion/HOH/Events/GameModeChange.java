package me.Latestion.HOH.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import me.Latestion.HOH.HideOrHunt;
import me.Latestion.HOH.Game.GameState;

public class GameModeChange implements Listener {
	
    private HideOrHunt plugin;
    
    public GameModeChange(final HideOrHunt plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void c(PlayerGameModeChangeEvent event) {
    	if (GameState.getCurrentGamestate() != GameState.ON) return;
    	if (!plugin.hohPlayer.containsKey(event.getPlayer().getUniqueId())) return;
    	if (!this.plugin.getConfig().getBoolean("Allow-Op")) return;
        if (!event.getNewGameMode().equals(GameMode.SPECTATOR)) {
            Bukkit.broadcastMessage(event.getPlayer().getName() + ChatColor.BOLD + "" + ChatColor.RED + " Just went in " + event.getNewGameMode().toString().toUpperCase());
        }
    }
}
