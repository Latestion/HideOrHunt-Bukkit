package me.Latestion.HOH.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import me.Latestion.HOH.Main;
import me.Latestion.HOH.Game.GameState;

public class TrulyGrace implements Listener {

	private Main plugin;
	
	public TrulyGrace(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (GameState.getCurrentGamestate() == GameState.ON)  {
			if (plugin.game.grace) {
				if (plugin.getConfig().getBoolean("Grace-Period-Peaceful")) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void target(EntityTargetEvent event) {
		if (GameState.getCurrentGamestate() == GameState.ON)  {
			if (plugin.game.grace) {
				if (plugin.getConfig().getBoolean("Grace-Period-Peaceful")) {
					event.setCancelled(true);
				}
			}
		}
	}
}
