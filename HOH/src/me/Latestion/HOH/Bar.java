package me.Latestion.HOH;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;


public class Bar {
	
	private final Main plugin;
	public BossBar bar;
	
	public Bar(Main plugin) {
		this.plugin = plugin;
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
		bar = Bukkit.createBossBar(format(ChatColor.GREEN + "Alive Teams: " + plugin.voids.getAliveTeam().size()), BarColor.GREEN, BarStyle.SOLID);
		bar.setVisible(true);
		addAllPlayer();
		cast();
	}
	
	public void cast() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				bar.setTitle(format(ChatColor.GREEN + "Alive Teams: " + plugin.voids.getAliveTeam().size()));
				double progress = (plugin.voids.getAliveTeam().size() / plugin.voids.getAllTeam().size());
				bar.setProgress(progress);
			}
		}, 0, 40L);
	}
	
	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
}
