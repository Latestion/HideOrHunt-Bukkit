package me.latestion.hoh.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.myrunnables.Episodes;
import me.latestion.hoh.myrunnables.SupplyDrop;
import me.latestion.hoh.stats.Metrics;
import me.latestion.hoh.utils.Bar;
import me.latestion.hoh.utils.ScoreBoardUtil;
import me.latestion.hoh.utils.Util;

public class HOHGame {

	private HideOrHunt plugin;
	public int size;
	private Util util;
	public Location loc;
	
	private List<HOHPlayer> players = new ArrayList<HOHPlayer>();
	private List<HOHTeam> teams = new ArrayList<>();
	public List<Player> cache = new ArrayList<>();
	
	public Bar bar;
	
	public boolean freeze = false;
	public boolean grace = false;
	
	public int ep = 1;
	
	public HOHGame(HideOrHunt plugin, Location loc, int size) {
		this.plugin = plugin;
		this.loc = loc;
		this.util = new Util(plugin);
		this.size = size;
		GameState.setGamestate(GameState.PREPARE);
		prepareGame();
	}

	private void prepareGame() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.isOp() && !util.getAllowOp()) {
				continue;
			}
			HOHPlayer p = new HOHPlayer(plugin, player.getUniqueId());
			plugin.hohPlayer.put(player.getUniqueId(), p);
			players.add(p);
		}
		double neededTeams = Math.ceil(players.size() / (double) size);
        int totalTeams = (int) neededTeams;
        util.createInv(totalTeams);
        for (HOHPlayer player : players) {
        	player.prepareTeam(plugin.inv);
        	cache.add(player.getPlayer());
        }
	}
	
	public void startGame() {
		
		if (GameState.getCurrentGamestate() != GameState.PREPARE) return;		
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "Starting Your" + ChatColor.WHITE + " HOH Game!");
		setBorder();
		for (HOHTeam team : teams) {
			String name = team.getName();
			plugin.sbUtil.addTeam(name);
			for (HOHPlayer player : team.players) {
				Player p = player.getPlayer();
				util.givePlayerKit(p, team, name);
			}
		}
		plugin.sbUtil.addAllPlayers();
		this.bar = new Bar(plugin);
		GameState.setGamestate(GameState.ON);
		sendTitle();
		plugin.sbUtil.setAsthetic();
		if (plugin.getConfig().getBoolean("Grace-Period")) grace = true;
		if (plugin.getConfig().getBoolean("Enable-Effect-On-Start")) addStartPotEffects();
		if (plugin.getConfig().getBoolean("Enable-Effect-After-Start")) addAfterPotEffects();
		if (plugin.getConfig().getBoolean("Auto-Episodes")) new Episodes(plugin);
		if (plugin.getConfig().getBoolean("Auto-Supply-Drops")) new SupplyDrop(plugin);
	}

	public void addTeam(HOHTeam team) {
		teams.add(team);
	}

	public List<HOHTeam> getTeams() {
		return teams;
	}
	
	public List<HOHTeam> getAliveTeams() {
		List<HOHTeam> send = new ArrayList<>();
		for (HOHTeam team : teams) {
			if (!team.eliminated) {
				send.add(team);
			}
		}
		return send;
	}
	
	private void setBorder() {
		int wb = util.getWorldBorder();
		loc.getWorld().getWorldBorder().setCenter(loc);
		loc.getWorld().getWorldBorder().setSize(wb);
		loc.getWorld().setSpawnLocation(loc);
	}
	
	public boolean end() {
		List<HOHTeam> aliveTeams = new ArrayList<>();
		for (HOHPlayer player : players) {
			if (!player.dead) {
				if (!aliveTeams.contains(player.getTeam())) {
					aliveTeams.add(player.getTeam());
				}
			}
		}
		return aliveTeams.size() == 1 ? true : false;
	}
	
	public void stop() {
    	loc.getWorld().getWorldBorder().setSize(100000000);
    	for (HOHTeam team : teams) {
    		if (team.getBeacon() != null) team.getBeacon().setType(Material.AIR);
    		for (HOHPlayer player : team.players) {
    			player.getPlayer().getInventory().clear();
    			loc.getWorld().getSpawnLocation();
    			player.getPlayer().setScoreboard(plugin.sbUtil.manager.getNewScoreboard());
    			player.getPlayer().teleport(loc);
    		}
    	}
    	GameState.setGamestate(GameState.OFF);
    	plugin.cache.clear();
    	plugin.chat.clear();
    	plugin.hohPlayer.clear();
    	plugin.hohTeam.clear();
    	bar.stop();
    	Bukkit.getScheduler().cancelTasks(plugin);
    	new Metrics(plugin, 79307);
    	plugin.sbUtil = new ScoreBoardUtil(plugin);
    	Bukkit.broadcastMessage(ChatColor.RED + "Game is stopped!");
	}	
	
	private void sendTitle() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendTitle(ChatColor.BOLD + "" + ChatColor.GREEN + "Game Started"
					, ChatColor.BOLD + "" + ChatColor.WHITE + "Leaders have received the team beacons."
					, 10
					, 50
					, 10);
		}
	}
	
	private void addStartPotEffects() {
		for (String s : plugin.getConfig().getStringList("Effect-On-Start")) {
			String[] split = s.split(", ");
			for (HOHPlayer player : players) {
				if (player.getPlayer().isOnline()) player.getPlayer()
				.addPotionEffect(new PotionEffect(PotionEffectType.getByName(split[0]),
						(Integer.parseInt(split[1]) * 20),
						(Integer.parseInt(split[2]) - 1), false, false));
			}
		}
	}
	
	private void addAfterPotEffects() {
		for (String s : plugin.getConfig().getStringList("Effect-After-Start")) {
			String[] split = s.split(", ");
			PotionEffect effect = new PotionEffect(PotionEffectType.getByName(split[0].toString()), (Integer.parseInt(split[1]) * 20), (Integer.parseInt(split[2]) - 1), false, false);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	            	for (HOHPlayer player : players) {
	    				player.getPlayer().addPotionEffect(effect);
	    			}
	            }
	        }, Integer.parseInt(split[3]) * 20L);
		}
	}

	public void graceOff() {
		grace = false;
		Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "Grace period has ended.");
		Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.RED + "PVP is enabled and you will take damage now!");
	}
	
	public HOHTeam getWinnerTeam() {
		for (HOHTeam team : teams) {
			if (!team.eliminated) {
				return team;
			}
		}
		return null;
	}
}
