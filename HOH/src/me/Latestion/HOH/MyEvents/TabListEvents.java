package me.Latestion.HOH.MyEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import me.Latestion.HOH.Main;

public class TabListEvents implements Listener {

	private Main plugin;
	public TabListEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void no(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			if (plugin.gameOn) {
				if (plugin.getConfig().getBoolean("Custom-Tab-List")) {
					plugin.voids.tabListName();
				}
			}
		}
	}
}
