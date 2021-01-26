package me.latestion.hoh;

import me.latestion.hoh.bungee.BungeeSupport;
import me.latestion.hoh.bungee.BungeeSupportHandler;
import me.latestion.hoh.bungee.PlugMessage;
import me.latestion.hoh.commandmanager.CommandInitializer;
import me.latestion.hoh.events.*;
import me.latestion.hoh.localization.MessageManager;
import me.latestion.hoh.party.HOHParty;
import me.latestion.hoh.party.HOHPartyEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.stats.Metrics;
import me.latestion.hoh.utils.ScoreBoardUtil;
import me.latestion.hoh.versioncheck.UpdateChecker;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.file.Files;
import java.nio.file.Paths;

public class HideOrHunt extends JavaPlugin {

	public HOHGame game;
	public ScoreBoardUtil sbUtil;
	private MessageManager msgManager;

	public BungeeSupport support;
	public HOHParty party;

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
		this.game = new HOHGame(this);
		loadSchems();
		loadBungee();
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
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new AsyncChat(this), this);
		pm.registerEvents(new BlockBreak(this), this);
		pm.registerEvents(new BlockPlace(this), this);
		pm.registerEvents(new CraftItem(this), this);
		pm.registerEvents(new EntityDamage(this), this);
		pm.registerEvents(new EntityExplode(this), this);
		pm.registerEvents(new GameModeChange(this), this);
		pm.registerEvents(new InventoryClick(this), this);
		pm.registerEvents(new InventoryClose(this), this);
		pm.registerEvents(new InventoryOpen(this), this);
		pm.registerEvents(new PlayerJoin(this), this);
		pm.registerEvents(new PlayerLogin(this), this);
		pm.registerEvents(new PlayerMove(this), this);
		pm.registerEvents(new PlayerQuit(this), this);
		pm.registerEvents(new PlayerWorld(this), this);
		pm.registerEvents(new RespawnScreen(this),this);
		pm.registerEvents(new TrulyGrace(this), this);
		new CommandInitializer(this).initialize();
	}

	public MessageManager getMessageManager() {
		return this.msgManager;
	}

	public void saveLanguagesFiles() {
		this.saveResource("locales/en.yml", false);
		this.saveResource("locales/pl.yml", false);
	}

	public void loadSchems()  {
		try {
			Files.createDirectories(Paths.get(this.getDataFolder().getAbsolutePath() + "/schems"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HOHGame getGame() {
		return game;
	}

	public static HideOrHunt getInstance() {
		return JavaPlugin.getPlugin(HideOrHunt.class);
	}

	private void loadBungee() {
		if (!getConfig().getBoolean("Bungee-Cord")) {
			return;
		}
		support = new BungeeSupport(this);
		this.getServer().getPluginManager().registerEvents(new BungeeSupportHandler(support), this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PlugMessage(this));

		if (!getConfig().getBoolean("Party-System")) {
			return;
		}

		/*
		TODO: Fix Tons Of Null Errors
		 */

		this.party = new HOHParty();
		this.getServer().getPluginManager().registerEvents(new HOHPartyEvents(party), this);

	}
}
