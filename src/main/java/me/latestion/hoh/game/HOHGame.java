package me.latestion.hoh.game;

import java.util.*;

import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
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

	private List<HOHPlayer> playersDuringGame = new ArrayList<>();
	private Map<Integer, HOHTeam> teams = new HashMap<>();

	public Bar bar;

	public boolean freeze = false;
	public boolean grace = false;

	public int ep = 1;

	public HOHGame(HideOrHunt plugin, Location loc, int size) {
		this.plugin = plugin;
		this.loc = loc;
		this.util = new Util(plugin);
		this.size = size;
		GameState.setGameState(GameState.PREPARE);
		prepareGame();
	}

	private void prepareGame() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.isOp() && !util.getAllowOp()) {
				continue;
			}
			HOHPlayer p = new HOHPlayer(plugin, player.getUniqueId());
			plugin.hohPlayers.put(player.getUniqueId(), p);
			playersDuringGame.add(p);
		}
		double neededTeams = Math.ceil(playersDuringGame.size() / (double) size);
		int totalTeams = (int) neededTeams;
		util.createInv(totalTeams);
		for (HOHPlayer player : playersDuringGame) {
			player.prepareTeam(plugin.inv);
		}
	}

	public void startGame() {

		if (GameState.getCurrentGameState() != GameState.PREPARE) return;
		Bukkit.getServer().broadcastMessage(plugin.getMessageManager().getMessage("starting-game"));
		setBorder();
		for (HOHTeam team : teams.values()) {
			String name = team.getName();
			plugin.sbUtil.addTeam(name);
			for (HOHPlayer player : team.players) {
				Player p = player.getPlayer();
				util.givePlayerKit(p, team, name);
			}
		}
		plugin.sbUtil.addAllPlayers();
		this.bar = new Bar(plugin);
		GameState.setGameState(GameState.ON);
		sendStartTitle();
		plugin.sbUtil.setAsthetic();
		if (plugin.getConfig().getBoolean("Grace-Period")) grace = true;
		if (plugin.getConfig().getBoolean("Enable-Effect-On-Start")) addStartPotEffects();
		if (plugin.getConfig().getBoolean("Enable-Effect-After-Start")) addAfterPotEffects();
		if (plugin.getConfig().getBoolean("Auto-Episodes")) new Episodes(plugin);
		if (plugin.getConfig().getBoolean("Auto-Supply-Drops")) new SupplyDrop(plugin);
	}

	public void addTeam(HOHTeam team) {
		teams.put(team.getID(), team);
	}

	public Collection<HOHTeam> getTeams() {
		return teams.values();
	}

	public HOHTeam getTeam(Integer id) {
		return teams.get(id);
	}

	public List<HOHTeam> getAliveTeams() {
		List<HOHTeam> send = new ArrayList<>();
		for (HOHTeam team : teams.values()) {
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
		for (HOHPlayer player : playersDuringGame) {
			if (!player.dead) {
				if (!aliveTeams.contains(player.getTeam())) {
					aliveTeams.add(player.getTeam());
				}
			}
		}
		return aliveTeams.size() == 1 ? true : false;
	}

	public void stop() {
		loc.getWorld().getWorldBorder().reset();
		for (HOHTeam team : teams.values()) {
			if (team.getBeacon() != null) team.getBeacon().setType(Material.AIR);
			for (HOHPlayer player : team.players) {
				player.getPlayer().getInventory().clear();
				loc.getWorld().getSpawnLocation();
				player.getPlayer().setScoreboard(plugin.sbUtil.manager.getNewScoreboard());
				player.getPlayer().teleport(loc);
			}
		}
		GameState.setGameState(GameState.OFF);
		plugin.hohPlayers.clear();
		playersDuringGame.clear();
		teams.clear();
		bar.stop();
		Bukkit.getScheduler().cancelTasks(plugin);
		new Metrics(plugin, 79307);
		plugin.sbUtil = new ScoreBoardUtil(plugin);
		Bukkit.broadcastMessage(plugin.getMessageManager().getMessage("stopping-game"));
	}

	private void sendStartTitle() {
		MessageManager messageManager = plugin.getMessageManager();
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendTitle(messageManager.getMessage("start-title-first-line")
					, messageManager.getMessage("start-title-second-line")
					, 10
					, 50
					, 10);
		}
	}

	private void addStartPotEffects() {
		for (String s : plugin.getConfig().getStringList("Effect-On-Start")) {
			String[] split = s.split(", ");
			for (HOHPlayer player : playersDuringGame) {
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
					for (HOHPlayer player : playersDuringGame) {
						player.getPlayer().addPotionEffect(effect);
					}
				}
			}, Integer.parseInt(split[3]) * 20L);
		}
	}

	public void graceOff() {
		MessageManager messageManager = plugin.getMessageManager();
		grace = false;
		Bukkit.broadcastMessage(messageManager.getMessage("grace-period-ended-1"));
		Bukkit.broadcastMessage(messageManager.getMessage("grace-period-ended-2"));
	}

	public HOHTeam getWinnerTeam() {
		return teams.values().stream().filter(t -> !t.eliminated).findAny().orElseGet(null);
	}

	public boolean areAllTeamsNamed() {
		return !plugin.getHohPlayers().values().stream().anyMatch(p -> p.isNamingTeam());
	}

	public boolean allPlayersSelectedTeam() {
		return plugin.getHohPlayers().values().stream().allMatch(p -> p.hasTeam());
	}
}
