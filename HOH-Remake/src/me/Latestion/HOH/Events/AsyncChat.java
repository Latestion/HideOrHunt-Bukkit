package me.Latestion.HOH.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.HOH.Main;
import me.Latestion.HOH.Game.GameState;
import me.Latestion.HOH.Game.HOHPlayer;
import me.Latestion.HOH.Game.HOHTeam;
import me.Latestion.HOH.Utils.Util;

public class AsyncChat implements Listener {
    private Main plugin;
    
    public AsyncChat(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void ce(AsyncPlayerChatEvent event) {
        if (plugin.chat.contains(event.getPlayer())) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            String name = event.getMessage();
            if (name.length() > 15) {
                player.sendMessage(ChatColor.RED + "Too many characters!");
                return;
            }
        	if (GameState.getCurrentGamestate() != GameState.PREPARE) {
        		return;
        	}
            Util util = new Util(plugin);
            if (util.isTeamTaken(name)) {
                player.sendMessage(ChatColor.RED + "Teamname already exists!");
            }
            else {
                player.sendMessage("Teamname set to: " + name);
                ItemStack item = plugin.inv.getItem(plugin.cache.get(plugin.chat.indexOf(player)));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                item.setItemMeta(meta);
                plugin.inv.setItem(plugin.cache.get(plugin.chat.indexOf(player)), item);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.updateInventory();
                }
                
                HOHTeam team = new HOHTeam(plugin, name);
                plugin.hohPlayer.get(player.getUniqueId()).setTeam(team);
                plugin.hohTeam.put(name, team);
                team.addPlayer(plugin.hohPlayer.get(player.getUniqueId()));
                plugin.game.addTeam(team);
                plugin.cache.remove(plugin.chat.indexOf(player));
                plugin.chat.remove(player);
                
                if (plugin.game.cache.isEmpty() && this.plugin.cache.isEmpty()) {
        			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
        	            public void run() {
        	            	plugin.game.startGame();
        	            }
        	        }, 1L);
                }
            }
        }
        else {
        	if (GameState.getCurrentGamestate() != GameState.ON) {
        		return;
        	}
        	if (plugin.hohPlayer.containsKey(event.getPlayer().getUniqueId())) {
        		HOHPlayer player = plugin.hohPlayer.get(event.getPlayer().getUniqueId());
        		if (player.dead) {        		
        			event.setCancelled(true);
        			return;
        		}
        		if (!player.teamChat) {
        			String message = event.getMessage();
                    String format = plugin.getConfig().getString("Main-Chat-Format");
                    String f = format.replace("%message%", message);
                    String g = f.replace("%playername%", event.getPlayer().getName());
                    String h = g.replace("%playerteam%", player.getTeam().getName());
                    event.setCancelled(true);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', h));	
        		}
        		else {
        			String message = event.getMessage();
                    String format = plugin.getConfig().getString("Team-Chat-Format");
                    String f = format.replace("%message%", message);
                    String g = f.replace("%playername%", event.getPlayer().getName());
                    String h = g.replace("%playerteam%", player.getTeam().getName());
        			for (HOHPlayer p : player.getTeam().players) {
        				if (p.getPlayer().isOnline()) {
                            p.getPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', h));
        				}
        			}
        		}
        	}
        }
    }
}
