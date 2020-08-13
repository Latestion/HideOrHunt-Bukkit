// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.Latestion.HOH.Main;

public class BlockPlace implements Listener
{
    private Main plugin;
    
    public BlockPlace(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void bpe(final BlockPlaceEvent event) {
        if (event.getBlock().getType().equals(Material.BEACON) && this.plugin.gameOn) {
            final Player player = event.getPlayer();
            final Location loc = event.getBlock().getLocation();
            final String whichTeam = this.plugin.voids.getPlayerTeam(player);
            this.plugin.blockLocation.put(loc, whichTeam);
            Bukkit.getServer().broadcastMessage(String.valueOf(ChatColor.translateAlternateColorCodes('&', whichTeam)) + ChatColor.AQUA + " has placed their beacon!");
            loc.getWorld().getBlockAt(loc.clone().add(0, 1, 0)).setType(Material.OAK_SIGN);
            Sign sign =  (Sign) loc.getWorld().getBlockAt(loc.clone().add(0, 1, 0)).getState();
            sign.setLine(0, whichTeam);
            sign.update();
        }
    }
}
