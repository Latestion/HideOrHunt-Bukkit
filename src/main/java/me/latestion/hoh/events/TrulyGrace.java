package me.latestion.hoh.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;

public class TrulyGrace implements Listener {

	private HideOrHunt plugin;

	public TrulyGrace(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (plugin.game.gameState == GameState.ON) {
			if (plugin.game.grace) {
				if (plugin.getConfig().getBoolean("Grace-Period-Peaceful")) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void target(EntityTargetEvent event) {
		if (plugin.game.gameState == GameState.ON) {
			if (plugin.game.grace) {
				if (plugin.getConfig().getBoolean("Grace-Period-Peaceful")) {
					event.setCancelled(true);
				}
			}
		}
	}
}
