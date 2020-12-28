package me.latestion.hoh.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.latestion.hoh.HideOrHunt;

public class Bar extends BukkitRunnable {
	
	private HideOrHunt plugin;
	public BossBar bar;
	
	public Bar(HideOrHunt plugin) {
		this.plugin = plugin;
		createBar();
		runTaskTimer(plugin, 0L, 50L);
	}
	
	public void addAllPlayer() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			bar.addPlayer(player);
		}
	}
	
	public void addPlayer(Player player) {
		bar.addPlayer(player);
	}
	
	public BossBar getBar() {
		return bar;
	}
	
	public void createBar() {
		String title = plugin.getMessageManager().getMessage("alive-teams-bar").replace("%number%", Integer.toString(plugin.game.getAliveTeams().size()));
		bar = Bukkit.createBossBar(title, BarColor.GREEN, BarStyle.SOLID);
		bar.setVisible(true);
		addAllPlayer();
		
	}
	
	@Override
	public void run() {
		String title = plugin.getMessageManager().getMessage("alive-teams-bar").replace("%number%", Integer.toString(plugin.game.getAliveTeams().size()));
		bar.setTitle(format(title));
		double progress = (plugin.game.getAliveTeams().size() / plugin.game.getTeams().size());
		bar.setProgress(progress);
	}

	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public void stop() {
		this.cancel();
		bar.removeAll();
	}
	
}
