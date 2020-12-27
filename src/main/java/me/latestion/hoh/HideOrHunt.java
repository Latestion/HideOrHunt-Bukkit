package me.latestion.hoh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import me.latestion.hoh.commandmanager.Executor;
import me.latestion.hoh.events.AsyncChat;
import me.latestion.hoh.events.BlockBreak;
import me.latestion.hoh.events.BlockPlace;
import me.latestion.hoh.events.CraftItem;
import me.latestion.hoh.events.EntityDamage;
import me.latestion.hoh.events.GameModeChange;
import me.latestion.hoh.events.InventoryClick;
import me.latestion.hoh.events.InventoryClose;
import me.latestion.hoh.events.InventoryOpen;
import me.latestion.hoh.events.PlayerDeath;
import me.latestion.hoh.events.PlayerJoin;
import me.latestion.hoh.events.PlayerMove;
import me.latestion.hoh.events.PlayerRespawn;
import me.latestion.hoh.events.PlayerWorld;
import me.latestion.hoh.events.TrulyGrace;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.stats.Metrics;
import me.latestion.hoh.utils.ScoreBoardUtil;
import me.latestion.hoh.versioncheck.UpdateChecker;

public class HideOrHunt extends JavaPlugin {

	public HOHGame game;
	public ScoreBoardUtil sbUtil;
	
	public Map<UUID, HOHPlayer> hohPlayer = new HashMap<>();
	public Map<String, HOHTeam> hohTeam = new HashMap<>();
	
	public List<Integer> cache = new ArrayList<>();
	public List<Player> chat = new ArrayList<>();
	
	public Inventory inv;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		GameState.setGamestate(GameState.OFF);
		sbUtil = new ScoreBoardUtil(this);
		new Metrics(this, 8350);
		hoh();
		registerAll();
	}
	
	@Override
	public void onDisable() {
		if (GameState.getCurrentGamestate() == GameState.ON) game.stop();
	}

    private void hoh() {
    	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    	console.sendMessage("        " +                  ChatColor.RED + " _______ ");
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |");
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |" + ChatColor.WHITE + "    Version: " + this.getDescription().getVersion()); 
    	console.sendMessage(ChatColor.AQUA + "|------|" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|------|" + ChatColor.WHITE + "    By: Latestion"); 
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
		this.getServer().getPluginManager().registerEvents(new CraftItem(), this);
		this.getServer().getPluginManager().registerEvents(new EntityDamage(this), this);
		this.getServer().getPluginManager().registerEvents(new GameModeChange(this), this);	
        this.getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClose(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryOpen(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerRespawn(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerWorld(this), this);
        this.getServer().getPluginManager().registerEvents(new TrulyGrace(this), this);
        this.getCommand("hoh").setExecutor(new Executor(this));
	}
}