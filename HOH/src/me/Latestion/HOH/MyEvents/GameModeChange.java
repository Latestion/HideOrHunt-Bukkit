// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import me.Latestion.HOH.Main;
import org.bukkit.event.Listener;

public class GameModeChange implements Listener
{
    private Main plugin;
    
    public GameModeChange(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void c(final PlayerGameModeChangeEvent event) {
        if (this.plugin.gameOn && this.plugin.played.contains(event.getPlayer()) && event.getPlayer().isOp() && this.plugin.getConfig().getBoolean("Allow-Op") && !event.getNewGameMode().equals((Object)GameMode.SPECTATOR)) {
            Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append(event.getPlayer().getName()).append(" Just went in ").append(event.getNewGameMode().toString().toUpperCase()).append(".").toString());
        }
    }
}
