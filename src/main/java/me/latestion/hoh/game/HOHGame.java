package me.latestion.hoh.game;

import java.io.File;
import java.util.*;

import me.latestion.hoh.files.FlatHOHGame;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
	public Location loc;
	public int teamSize;
	private Util util;

	public Map<UUID, HOHPlayer> hohPlayers = new HashMap<>();
	private Map<Integer, HOHTeam> teams = new HashMap<>();

	public Bar bar;

	public boolean freeze = false;
	public boolean grace = false;

	public GameState gameState = GameState.OFF;

	public int ep = 1;

	public Inventory inv;

	public HOHGame(HideOrHunt plugin){
		this.plugin = plugin;
		this.util = new Util(plugin);
	}

	public HOHGame(HideOrHunt plugin, Location loc, int teamSize) {
		this.plugin = plugin;
		this.loc = loc;
		this.util = new Util(plugin);
		this.teamSize = teamSize;
	}

	public void loadGame(){
		double neededTeams = Math.ceil(hohPlayers.size() / (double) teamSize);
		int totalTeams = (int) neededTeams;
		this.inv = util.createInv(totalTeams);
		teams.values().stream().forEach(t -> plugin.sbUtil.addTeam(t.getName()));
		this.bar = new Bar(plugin);
		if (plugin.getConfig().getBoolean("Auto-Episodes")) new Episodes(plugin);
		if (plugin.getConfig().getBoolean("Auto-Supply-Drops")) new SupplyDrop(plugin);
	}
	public void prepareGame() {
		this.gameState = GameState.PREPARE;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.isOp() && !util.getAllowOp()) {
				continue;
			}
			HOHPlayer p = new HOHPlayer(this, player.getUniqueId());
			hohPlayers.put(player.getUniqueId(), p);
		}
		double neededTeams = Math.ceil(hohPlayers.size() / (double) teamSize);
		int totalTeams = (int) neededTeams;
		this.inv = util.createInv(totalTeams);
		for (HOHPlayer player : hohPlayers.values()) {
			player.prepareTeam(inv);
		}
	}

	public void startGame() {
		if (gameState != GameState.PREPARE) return;
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
		gameState = GameState.ON;
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

	public Map<Integer, HOHTeam> getTeams() {
		return teams;
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

	public void setSpawnLocation(Location loc){
		this.loc = loc;
	}

	public void setHohPlayers(Map<UUID, HOHPlayer> hohPlayers) {
		this.hohPlayers = hohPlayers;
	}

	public void setTeams(Map<Integer, HOHTeam> teams) {
		this.teams = teams;
	}

	public boolean checkEndConditions() {
		List<HOHTeam> aliveTeams = new ArrayList<>();
		for (HOHPlayer player : hohPlayers.values()) {
			if (!player.dead) {
				if (!aliveTeams.contains(player.getTeam())) {
					aliveTeams.add(player.getTeam());
				}
			}
		}
		return aliveTeams.size() == 1 ? true : false;
	}

	public void endGame(){
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
		gameState = GameState.OFF;
		hohPlayers.clear();
		teams.clear();
		bar.stop();
		plugin.sbUtil = new ScoreBoardUtil(plugin);
		Bukkit.getScheduler().cancelTasks(plugin);

		File gameFile = new File(plugin.getDataFolder(), "hohGame.yml");
		gameFile.delete();
		File teamFile = new File(plugin.getDataFolder(), "teams.yml");
		teamFile.delete();
		File playersFile = new File(plugin.getDataFolder(), "players.yml");
		playersFile.delete();

		Bukkit.broadcastMessage(plugin.getMessageManager().getMessage("ending-game"));
	}

	public void serverStop() {
		FlatHOHGame.save(this, plugin, new File(plugin.getDataFolder(), "hohGame.yml"));
		Bukkit.getScheduler().cancelTasks(plugin);
		plugin.game = null;
		new Metrics(plugin, 79307);
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
			for (HOHPlayer player : hohPlayers.values()) {
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
					for (HOHPlayer player : hohPlayers.values()) {
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
		return teams.values().stream().filter(t -> !t.eliminated).findAny().orElse(null);
	}

	public boolean areAllTeamsNamed() {
		return !plugin.game.getHohPlayers().values().stream().anyMatch(p -> p.isNamingTeam());
	}

	public boolean allPlayersSelectedTeam() {
		return plugin.game.getHohPlayers().values().stream().allMatch(p -> p.hasTeam());
	}

	public Map<UUID, HOHPlayer> getHohPlayers() {
		return hohPlayers;
	}

	public HOHPlayer getHohPlayer(UUID uuid) {
		return hohPlayers.get(uuid);
	}
}
