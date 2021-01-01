package me.latestion.hoh.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;

public class PlayerMove implements Listener {

	private HideOrHunt plugin;

	public PlayerMove(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void pme(PlayerMoveEvent event) {
		if (plugin.game.gameState == GameState.ON) {
			if (!plugin.game.freeze) return;
			if (event.getPlayer().isOp() && !this.plugin.getConfig().getBoolean("Allow-Op")) {
				event.setCancelled(false);
			} else {
				Location to = event.getFrom();
				to.setPitch(event.getTo().getPitch());
				to.setYaw(event.getTo().getYaw());
				event.setTo(to);
			}
		}
	}
}
