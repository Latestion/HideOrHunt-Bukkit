// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
            if (!plugin.bar.getBar().getPlayers().contains(event.getPlayer())) plugin.bar.addPlayer(event.getPlayer());
        }
        if (plugin.version && plugin.getConfig().getBoolean("Custom-Tab-List"))
        	plugin.voids.prepareTab();
        event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Join-Title")), "", 10, 50, 10);
        
        if (!plugin.gameOn) {
        	try {
        		if (plugin.lolloc != null) {
        			event.getPlayer().teleport(plugin.lolloc);
        		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        Player console = event.getPlayer();
        console.sendMessage("       " +                  ChatColor.RED + "_____");
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |");
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |" + ChatColor.WHITE + "    Version: " + plugin.getDescription().getVersion()); 
    	console.sendMessage(ChatColor.AQUA + "|----|" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|----|" + ChatColor.WHITE + "    By: Latestion"); 
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |"); 
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|____|" + ChatColor.AQUA + "|      |");
        console.sendMessage("https://www.spigotmc.org/resources/hide-or-hunt-plugin.79307/");
    }
}
