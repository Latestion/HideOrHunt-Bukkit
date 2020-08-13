// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.scoreboard.Score;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import me.Latestion.HOH.Main;
import org.bukkit.event.Listener;

public class BlockBreak implements Listener
{
    private Main plugin;
    
    public BlockBreak(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void bbe(final BlockBreakEvent event) {
        if (this.plugin.gameOn) {
            final Player player = event.getPlayer();
            if (event.getBlock().getType().equals((Object)Material.BEACON)) {
                 String name = this.plugin.blockLocation.get(event.getBlock().getLocation());
                if (this.plugin.data.getConfig().getStringList(String.valueOf(this.plugin.time) + "." + name).contains(player.getUniqueId().toString())) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot break your own beacon!");
                    return;
                }
                event.setDropItems(false);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
                this.plugin.blockLocation.remove(event.getBlock().getLocation());
                Bukkit.broadcastMessage(String.valueOf(ChatColor.translateAlternateColorCodes('&', name)) + ChatColor.BOLD + ChatColor.RED + " lost their beacon!");
                this.plugin.noBeacon.add(name);
                this.plugin.obj.getScoreboard().resetScores(String.valueOf(ChatColor.translateAlternateColorCodes('&', name))
                		+ ": " + ChatColor.BOLD + ChatColor.GREEN + "\u2713");
                int i = this.plugin.scoreboardHolder.get(name);
                Score score = this.plugin.obj.getScore(String.valueOf(ChatColor.translateAlternateColorCodes('&', name)) 
                		+ ": " + ChatColor.BOLD + ChatColor.RED + "\u2718");
                score.setScore(i);
            }
        }
    }
}
