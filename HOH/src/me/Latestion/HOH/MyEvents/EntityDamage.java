// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Latestion.HOH.Main;

public class EntityDamage implements Listener
{
	
	public List<UUID> antilog = new ArrayList<UUID>();

    private Main plugin;
    
    public EntityDamage(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void damage(final EntityDamageByEntityEvent event) {
        if (this.plugin.gameOn && this.plugin.graceOn && event.getDamager() instanceof Player && event.getEntity() instanceof Player && this.plugin.getConfig().getBoolean("Grace-Period")) {
            event.setCancelled(true);
            event.getDamager().sendMessage(ChatColor.RED + "Grace Period!");
            return;
        }
        if (plugin.gameOn && event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
        	
        	Player player = (Player) event.getEntity();
        	Player target = (Player) event.getDamager();
        	
        	if (!plugin.getConfig().getBoolean("Team-Damage")) {
            	if (plugin.voids.getPlayerTeam(player).equalsIgnoreCase(plugin.voids.getPlayerTeam(target))) {
            		event.setCancelled(true);
            		target.sendMessage(ChatColor.RED + "You cannot hurt your own teammate!");
            		return;
            	}
        	}
        	
        	int secs = plugin.getConfig().getInt("Pvp-Log");
        	if (secs == 0) return;
        	plugin.voids.tabListName();

        	
        	if (!antilog.contains(player.getUniqueId()) && !antilog.contains(target.getUniqueId())) {
    	        player.sendMessage(ChatColor.GOLD + "You're now in Combat for " + secs + " second !");
    	        target.sendMessage(ChatColor.GOLD + "You're now in Combat for " + secs + " second !");
    	        antilog.add(player.getUniqueId());
    	        antilog.add(target.getUniqueId());
    	        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
    	        	@Override
    		        public void run() {
    		            if ((antilog.contains(player.getUniqueId())) && (antilog.contains(target.getUniqueId()))) {
				            antilog.remove(player.getUniqueId());
				            antilog.remove(target.getUniqueId());
				            target.sendMessage(ChatColor.GREEN + "You can now log out safely.");
				            player.sendMessage(ChatColor.GREEN + "You can now log out safely.");
    		            }
    		        }
    		    } , secs * 20L);
        	}
        }
    }
    
	@EventHandler
	public void onAntiLogQuit(PlayerQuitEvent event) {
	    Player p = event.getPlayer();
	    if (plugin.gameOn) {
		    if (this.antilog.contains(p.getUniqueId())) {
		    	Bukkit.getServer().broadcastMessage(event.getPlayer().getName() + " has combat logged!");
			    p.damage(20.0);
		    }
	    }
	}
    
}
