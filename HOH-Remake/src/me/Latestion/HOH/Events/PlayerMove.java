package me.Latestion.HOH.Events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Latestion.HOH.Main;
import me.Latestion.HOH.Game.GameState;

public class PlayerMove implements Listener {
	
    private Main plugin;
    
    public PlayerMove(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void pme(PlayerMoveEvent event) {
        if (GameState.getCurrentGamestate() == GameState.ON) {
        	if (!plugin.game.freeze) return;
            if (event.getPlayer().isOp() && !this.plugin.getConfig().getBoolean("Allow-Op")) {
                event.setCancelled(false);
            }
            else {
                Location to = event.getFrom();
                to.setPitch(event.getTo().getPitch());
                to.setYaw(event.getTo().getYaw());
                event.setTo(to);
            }
        }
    }
}
