package me.Latestion.HOH;

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

import me.Latestion.HOH.CommandManager.Executor;
import me.Latestion.HOH.Events.AsyncChat;
import me.Latestion.HOH.Events.BlockBreak;
import me.Latestion.HOH.Events.BlockPlace;
import me.Latestion.HOH.Events.CraftItem;
import me.Latestion.HOH.Events.EntityDamage;
import me.Latestion.HOH.Events.GameModeChange;
import me.Latestion.HOH.Events.InventoryClick;
import me.Latestion.HOH.Events.InventoryClose;
import me.Latestion.HOH.Events.InventoryOpen;
import me.Latestion.HOH.Events.PlayerDeath;
import me.Latestion.HOH.Events.PlayerJoin;
import me.Latestion.HOH.Events.PlayerMove;
import me.Latestion.HOH.Events.PlayerRespawn;
import me.Latestion.HOH.Events.PlayerWorld;
import me.Latestion.HOH.Events.TrulyGrace;
import me.Latestion.HOH.Game.GameState;
import me.Latestion.HOH.Game.HOHGame;
import me.Latestion.HOH.Game.HOHPlayer;
import me.Latestion.HOH.Game.HOHTeam;
import me.Latestion.HOH.Stats.Metrics;
import me.Latestion.HOH.Utils.ScoreBoardUtil;
import me.Latestion.HOH.VersionCheck.UpdateChecker;

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
