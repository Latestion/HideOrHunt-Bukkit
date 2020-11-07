package me.Latestion.HOH.Events;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.Latestion.HOH.HideOrHunt;
import me.Latestion.HOH.Game.GameState;
import me.Latestion.HOH.Game.HOHPlayer;
import me.Latestion.HOH.Game.HOHTeam;
import me.Latestion.HOH.Utils.Util;

public class PlayerDeath implements Listener {
	
    private HideOrHunt plugin;
    private Util util;
    
    public PlayerDeath(HideOrHunt plugin) {
        this.plugin = plugin;
        this.util = new Util(plugin);
    }
    
    @EventHandler
    public void death(PlayerDeathEvent event) {
        if (GameState.getCurrentGamestate() == GameState.ON) {
            HOHPlayer player = plugin.hohPlayer.get(event.getEntity().getUniqueId());
            
            if (!player.getTeam().hasBeacon) {
            	Bukkit.broadcastMessage(ChatColor.RED + player.getPlayer().getName() + " was eliminated from the game!");
            	
            	if (player.getPlayer().getInventory().containsAtLeast(util.beacon(player.getTeam().getName()), 1)) {
                	event.getDrops().remove(util.beacon(player.getTeam().getName()));
                }
             
            	player.banned = true;
            	player.dead = true;
            	player.getTeam().diedPlayer(player);     
            	
            	if (player.getTeam().eliminated) {
            		Bukkit.broadcastMessage(ChatColor.GREEN + player.getTeam().getName() + " was eliminated!");
            		plugin.sbUtil.eliminateTeam(player.getTeam().getName());
            	}
     
                if (this.plugin.getConfig().getBoolean("Ban-Player-On-Death")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {	
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getPlayer().getName() + " Eliminated");
                        }
                    }, 0L);
                }
                else {
                    player.getPlayer().setGameMode(GameMode.SPECTATOR);
                }
                
                if (plugin.game.end()) {
                    HOHTeam winnerTeam = plugin.game.getWinnerTeam();
                    if (winnerTeam == null) return;
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', winnerTeam.getName()) + ChatColor.AQUA + " has won the game!");
                    GameState.setGamestate(GameState.OFF);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            for (UUID p : plugin.hohPlayer.keySet()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pardon " + Bukkit.getPlayer(p).getName());
                            }
                        }
                    }, 0L);
                    plugin.game.stop();
                }
                
            }
            else {
                Random rand = new Random();
                int i = rand.nextInt(100);
                if (this.plugin.getConfig().getInt("Inventory-Keep") == 0) {
                    return;
                }
                if (i + 1 <= this.plugin.getConfig().getInt("Inventory-Keep")) {
                    event.setKeepInventory(true);
                    event.getDrops().clear();
                    player.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "You Kept Your Stuff!");
                }
                else {
                    player.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "You Lost Your Stuff!");
                }
            }
        }
    }
}
