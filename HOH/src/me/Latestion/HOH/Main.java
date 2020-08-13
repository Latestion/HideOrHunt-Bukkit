// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.Latestion.HOH.Commands.CommandManager;
import me.Latestion.HOH.Commands.TabC;
import me.Latestion.HOH.Files.DataManager;
import me.Latestion.HOH.MyEvents.AsyncChat;
import me.Latestion.HOH.MyEvents.BlockBreak;
import me.Latestion.HOH.MyEvents.BlockPlace;
import me.Latestion.HOH.MyEvents.CraftItem;
import me.Latestion.HOH.MyEvents.EntityDamage;
import me.Latestion.HOH.MyEvents.GameModeChange;
import me.Latestion.HOH.MyEvents.InventoryClick;
import me.Latestion.HOH.MyEvents.InventoryClose;
import me.Latestion.HOH.MyEvents.InventoryOpen;
import me.Latestion.HOH.MyEvents.PlayerDeath;
import me.Latestion.HOH.MyEvents.PlayerJoin;
import me.Latestion.HOH.MyEvents.PlayerMove;
import me.Latestion.HOH.MyEvents.PlayerWorld;
import me.Latestion.HOH.MyEvents.TabListEvents;
import me.Latestion.HOH.MyVoids.CreateVoids;
import me.Latestion.HOH.Stats.Metrics;
import me.Latestion.HOH.Tabs.TabManager16;
import me.Latestion.HOH.VersionCheck.UpdateChecker;

public class Main extends JavaPlugin
{
    public boolean gameOn;
    public Player started;
    public Inventory inv;
    public CreateVoids voids;
    public int gmType;
    public String time;
    public DataManager data;
    public boolean freeze;
    public World worldGame;
    public int ep;
    public boolean graceOn;
    public Map<Location, String> blockLocation;
    public Map<String, Integer> scoreboardHolder;
    public List<Player> played;
    public List<Player> chat;
    public List<Player> deadTalk;
    public List<Player> staffSpy;
    public List<Player> teamChat;
    public List<String> noBeacon;
    public List<Player> banList;
    public List<Integer> cache;
    public List<Player> cache2;
    public ScoreboardManager manager;
    public Scoreboard board;
    public Objective obj;
    public Inventory stats;
    public TabManager16 tab;
    public boolean version = false;
    public boolean latest = false;
    public CommandManager cmdManager;
    public Location lolloc;
    
    @Override
    public void onEnable() {
    	if (Bukkit.getVersion().contains("1.16.1")) {
    		this.version = true;
        	this.tab = new TabManager16(this);
    	}
    	man();
        this.saveDefaultConfig();
        this.data = new DataManager(this);
        this.voids = new CreateVoids(this);
        this.cmdManager = new CommandManager(this);
        this.scoreBoardRegister();
        this.getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
        this.getServer().getPluginManager().registerEvents(new AsyncChat(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClose(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryOpen(this), this);
        this.getServer().getPluginManager().registerEvents(new CraftItem(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        this.getServer().getPluginManager().registerEvents(new GameModeChange(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamage(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerWorld(this), this);
        this.getServer().getPluginManager().registerEvents(new TabListEvents(this), this);
        this.getCommand("hoh").setExecutor(new CommandManager(this));
        this.getCommand("hoh").setTabCompleter(new TabC());
        new Metrics(this, 8350);
    }
    
    @Override
    public void onDisable() {
    }
    
    private void man() {
        hoh();
        hasUpdate();
        this.gameOn = false;
        this.freeze = false;
        this.ep = 1;
        this.graceOn = false;
        this.blockLocation = new HashMap<Location, String>();
        this.scoreboardHolder = new HashMap<String, Integer>();
        this.played = new ArrayList<Player>();
        this.chat = new ArrayList<Player>();
        this.deadTalk = new ArrayList<Player>();
        this.staffSpy = new ArrayList<Player>();
        this.teamChat = new ArrayList<Player>();
        this.noBeacon = new ArrayList<String>();
        this.banList = new ArrayList<Player>();
        this.cache = new ArrayList<Integer>();
        this.cache2 = new ArrayList<Player>();
   
    }
    
    public void scoreBoardRegister() {
        this.manager = Bukkit.getScoreboardManager();
        this.board = this.manager.getNewScoreboard();
        this.obj = this.board.registerNewObjective("HideOrHunt", "dummy", ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("SB-Title")));
    }
    
    private void hoh() {
    	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    	console.sendMessage("        " + ChatColor.RED + " _______ ");
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |");
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |" + ChatColor.WHITE + "    Version: " 
    	+ this.getDescription().getVersion()); 
    	console.sendMessage(ChatColor.AQUA + "|------|" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|------|" + ChatColor.WHITE + "    By: Latestion"); 
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|       |" + ChatColor.AQUA + "|      |"); 
    	console.sendMessage(ChatColor.AQUA + "|      |" + ChatColor.RED + "|_______|" + ChatColor.AQUA + "|      |");
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
            	latest = true;
                getServer().getConsoleSender().sendMessage("[Hide Or Hunt] Plugin is up to date! - "
                				+ getDescription().getVersion());
                return false;
            }
        } catch (Exception e) {
            getLogger().info("Hide Or Hunt Could not check for updates!");
            return false;
        } 
	}
    
}
