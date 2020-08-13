// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;
import me.Latestion.HOH.Main;
import org.bukkit.event.Listener;

public class PlayerJoin implements Listener
{
    private Main plugin;
    
    public PlayerJoin(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void pje(final PlayerJoinEvent event) {
        if (this.plugin.gameOn) {
            event.getPlayer().setScoreboard(this.plugin.board);
            if (plugin.version && plugin.getConfig().getBoolean("Custom-Tab-List")) {
            	event.getPlayer().setPlayerListName("[" + plugin.voids.format(plugin.voids.getPlayerTeam(event.getPlayer())) + ChatColor.RESET + "]" + event.getPlayer().getName());
            }
        }
        if (plugin.version && plugin.getConfig().getBoolean("Custom-Tab-List"))
        	plugin.voids.prepareTab();
        event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Join-Title")), "", 10, 50, 10);
    }
}
