package me.latestion.hoh;

import me.latestion.hoh.commandmanager.CommandInitializer;
import me.latestion.hoh.data.flat.FlatHOHGame;
import me.latestion.hoh.events.*;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.stats.Metrics;
import me.latestion.hoh.utils.ScoreBoardUtil;
import me.latestion.hoh.versioncheck.UpdateChecker;

import java.io.File;

public class HideOrHunt extends JavaPlugin {

	public HOHGame game;
	public ScoreBoardUtil sbUtil;
	private MessageManager msgManager;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		this.saveLanguagesFiles();
		String lang = getConfig().getString("language");
		this.msgManager = new MessageManager(this.getDataFolder(), lang);

		sbUtil = new ScoreBoardUtil(this);
		new Metrics(this, 8350);
		hoh();
		registerAll();
		game = FlatHOHGame.deserialize(new File(this.getDataFolder(), "hohGame.yml"), this);
		if(game != null){
			game.loadGame();
		}
		else{
			this.game = new HOHGame(this);
		}
	}

	@Override
	public void onDisable() {
		if (game.gameState == GameState.ON) game.serverStop();
	}

	private void hoh() {
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		console.sendMessage("        " + ChatColor.RED + " _______ ");
		console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |");
		console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |" + ChatColor.WHITE + "    Version: " + this.getDescription().getVersion());
		console.sendMessage(ChatColor.AQUA + "|------|" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|------|" + ChatColor.WHITE + "    By: Latestion and barpec12");
		console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |");
		console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|_______|" + ChatColor.AQUA + "|      |");
		hasUpdate();
	}

	private boolean hasUpdate() {
		UpdateChecker updater = new UpdateChecker(this, 79307);
		try {
			if (updater.checkForUpdates()) {
				getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "You are using an older version of Hide Or Hunt!");
				getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Download the newest version here:");
				getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "https://www.spigotmc.org/resources/hide-or-hunt-plugin.79307/");
				getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				return true;
			} else {
				getServer().getConsoleSender().sendMessage("[Hide Or Hunt] Plugin is up to date! - "
						+ getDescription().getVersion());
				return false;
			}
		} catch (Exception e) {
			getLogger().info("Hide Or Hunt Could not check for updates.");
			return false;
		}

	}

	private void registerAll() {
		this.getServer().getPluginManager().registerEvents(new AsyncChat(this), this);
		this.getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
		this.getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
		this.getServer().getPluginManager().registerEvents(new CraftItem(this), this);
		this.getServer().getPluginManager().registerEvents(new EntityDamage(this), this);
		this.getServer().getPluginManager().registerEvents(new GameModeChange(this), this);
		this.getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
		this.getServer().getPluginManager().registerEvents(new InventoryClose(this), this);
		this.getServer().getPluginManager().registerEvents(new InventoryOpen(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerRespawn(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerWorld(this), this);
		this.getServer().getPluginManager().registerEvents(new TrulyGrace(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLogin(this), this);
		this.getServer().getPluginManager().registerEvents(new EntityExplode(this), this);
		new CommandInitializer(this).initialize();
	}

	public MessageManager getMessageManager() {
		return this.msgManager;
	}

	public void saveLanguagesFiles() {
		this.saveResource("locales/en.yml", false);
		this.saveResource("locales/pl.yml", false);
	}
}
