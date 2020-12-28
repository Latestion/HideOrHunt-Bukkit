package me.latestion.hoh.myrunnables;

import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import me.latestion.hoh.HideOrHunt;

public class Episodes extends BukkitRunnable {

	private HideOrHunt plugin;

	public Episodes(HideOrHunt plugin) {
		this.plugin = plugin;
		runTaskTimer(plugin, (plugin.getConfig().getInt("Episode-Time") * 60 * 20),
				(plugin.getConfig().getInt("Episode-Time") * 60 * 20L));
		sendReminders();
	}

	@Override
	public void run() {
		MessageManager messageManager = plugin.getMessageManager();
		Bukkit.broadcastMessage(messageManager.getMessage("episode-end").replace("%episode%", Integer.toString(plugin.game.ep)));
		if (plugin.game.ep == 1) {
			if (plugin.game.grace)
				plugin.game.graceOff();
		}
		if (plugin.getConfig().getInt("Episode-End-Break-Time") != 0) {
			plugin.game.freeze = true;
			Bukkit.broadcastMessage(messageManager.getMessage("game-freezed"));
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.game.freeze = false;
					Bukkit.broadcastMessage(messageManager.getMessage("game-unfreezed"));
					sendReminders();
				}
			}, (plugin.getConfig().getInt("Episode-End-Break-Time") * 20));
		} else {
			sendReminders();
		}
		plugin.game.ep++;
	}

	private void sendReminders() {
		for (String s : plugin.getConfig().getStringList("Episode-Reminders")) {
			String[] split = s.split(", ");
			int interval = Integer.parseInt(split[0]);
			String message = format(split[1].replace("%ep%", Integer.toString(plugin.game.ep)));
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					Bukkit.broadcastMessage(message);
				}
			}, interval * 20L);
		}
	}

	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
