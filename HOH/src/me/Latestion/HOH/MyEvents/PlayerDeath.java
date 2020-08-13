// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.Latestion.HOH.Main;

public class PlayerDeath implements Listener
{
    private Main plugin;
    
    public PlayerDeath(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void death(final PlayerDeathEvent event) {
        if (this.plugin.gameOn) {
            final Player player = event.getEntity();
            final String whichTeam = this.plugin.voids.getPlayerTeam(player);
            if (this.plugin.noBeacon.contains(whichTeam) || !plugin.blockLocation.containsValue(whichTeam)) {
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " was eliminated from the game!");
                
                if (player.getInventory().containsAtLeast(plugin.voids.beacon(whichTeam), 1)) {
                	event.getDrops().remove(plugin.voids.beacon(whichTeam));
                }
                
                if (this.plugin.getConfig().getBoolean("Ban-Player-On-Death")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.banList.add(player);
                            plugin.deadTalk.add(player);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName());
                        }
                    }, 0L);
                }
                else {
                    player.setGameMode(GameMode.SPECTATOR);
                    this.plugin.deadTalk.add(event.getEntity());
                }
                if (this.plugin.voids.end()) {
                    String winnerTeam = this.plugin.voids.getPlayerTeam(player.getKiller());
                    Bukkit.broadcastMessage(String.valueOf(ChatColor.translateAlternateColorCodes('&', winnerTeam)) + ChatColor.AQUA + " has won the game!");
                    this.plugin.gameOn = false;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable)new Runnable() {
                        @Override
                        public void run() {
                            for (final Player p : plugin.banList) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pardon " + p.getName());
                            }
                        }
                    }, 0L);
                    plugin.cmdManager.stop(player);
                }
            }
            else {
                final Random rand = new Random();
                final int i = rand.nextInt(100);
                if (this.plugin.getConfig().getInt("Inventory-Keep") == 0) {
                    return;
                }
                if (i + 1 <= this.plugin.getConfig().getInt("Inventory-Keep")) {
                    event.setKeepInventory(true);
                    event.getDrops().clear();
                    player.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("You Kept Your Stuff!").toString());
                }
                else {
                    player.sendMessage(new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append("You Lost Your Stuff!").toString());
                }
                final int i2 = rand.nextInt(100);
                if (this.plugin.getConfig().getInt("Player-Coord-Chance") == 0) {
                    return;
                }
                if (i2 + 1 <= this.plugin.getConfig().getInt("Player-Coord-Chance")) {
                    final Player killer = event.getEntity().getKiller();
                    final int i3 = rand.nextInt(1);
                    for (final Location loc : this.plugin.blockLocation.keySet()) {
                        if (this.plugin.blockLocation.get(loc).equals(whichTeam)) {
                            if (i3 == 0) {
                                killer.sendMessage(String.valueOf(player.getName()) + " beacon X coord is: " + loc.getBlockX());
                            }
                            if (i3 == 1) {
                                killer.sendMessage(String.valueOf(player.getName()) + " beacon Z coord is: " + loc.getBlockZ());
                            }
                        }
                    }
                }
            }
        }
    }
}
