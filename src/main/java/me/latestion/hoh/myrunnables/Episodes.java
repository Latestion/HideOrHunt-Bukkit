package me.latestion.hoh.myrunnables;

import me.latestion.hoh.data.flat.FlatHOHGame;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import me.latestion.hoh.HideOrHunt;

import java.io.File;
import java.util.Map;

public class Episodes extends BukkitRunnable {

	private HideOrHunt plugin;
	long time;

	public Episodes(HideOrHunt plugin){
		this(plugin, 0);
	}
	public Episodes(HideOrHunt plugin, long time) {
		this.plugin = plugin;
		this.time = time;
		runTaskTimer(plugin, 0L, 20L);
	}

	@Override
	public void run() {
		HOHGame game = plugin.getGame();
		MessageManager messageManager = plugin.getMessageManager();
		long breakTime = game.getBreakTime();
		if(game.isDuringBreak() && breakTime != -1 && time >= breakTime){
			game.unFreeze();
		}
		if(game.isFrozen()){
			return;
		}
		sendReminders(game.getEpisodeTime() - time);
		if(time != 0 && time % plugin.getGame().getEpisodeTime() == 0){
			Bukkit.broadcastMessage(messageManager.getMessage("episode-end").replace("%episode%", Integer.toString(game.ep)));
			if (game.getEpisodeNumber() == 1) {
				if (game.grace)
					game.graceOff();
			}
			if (breakTime != 0) {
				game.freeze();
				game.setDuringBreak(true);
			}
			time = 0;
			game.ep++;
			FlatHOHGame.save(game, plugin, new File(plugin.getDataFolder(), "hohGame.yml"));
			return;
		}
		time++;
	}

	private void sendReminders(long remainingTime) {
		Map<Long, String> reminders = plugin.getGame().getEpisodeReminders();
		if(reminders.containsKey(remainingTime)){
			String message = format(reminders.get(remainingTime).replace("%ep%", Integer.toString(plugin.game.ep)));
			Bukkit.broadcastMessage(message);
		}
	}

	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
