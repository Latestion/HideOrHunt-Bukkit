// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.Commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.HOH.Main;
import me.Latestion.HOH.Stats.Metrics;

public class CommandManager implements CommandExecutor
{
    private Main plugin;
    
    public CommandManager(final Main plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Console Cannot Do This!");
        }
        else {
            final Player player = (Player)sender;
            if (label.equalsIgnoreCase("hoh")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "/hoh start: " + ChatColor.WHITE + "Starts the game!");
                    player.sendMessage(ChatColor.RED + "/hoh freeze: " + ChatColor.WHITE + "Freezes the game!");
                    player.sendMessage(ChatColor.RED + "/hoh reload: " + ChatColor.WHITE + "Reloads the config file!");
                    player.sendMessage(ChatColor.RED + "/hoh beacon: " + ChatColor.WHITE + "Lets an admin teleport to a players/teams beacon!");
                    player.sendMessage(ChatColor.RED + "/hoh rules: " + ChatColor.WHITE + "Sends all the rules!");
                    player.sendMessage(ChatColor.RED + "/hoh spy: " + ChatColor.WHITE + "Allows the admin to see team chat!");
                    player.sendMessage(ChatColor.RED + "/hoh chat: " + ChatColor.WHITE + "Enables team chat!");
                }
                else {
                	if (args[0].equalsIgnoreCase("start")) {
                        if (this.plugin.gameOn) {
                            return false;
                        }
                        if (!player.hasPermission("hoh.start")) {
                            return false;
                        }
                        if (args.length > 1) {
                            this.plugin.started = player;
                            final int i = Integer.parseInt(args[1]);
                            if (i > 0) {
                                final double neededTeams = Math.ceil(Bukkit.getOnlinePlayers().size() / (double)i);
                                final int totalTeams = (int)neededTeams;
                                this.plugin.voids.createInv(totalTeams);
                                this.plugin.gmType = i;
                                plugin.lolloc = player.getLocation();
                                final LocalDateTime now = LocalDateTime.now();
                                final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH/mm/ss");
                                final String formatDateTime = now.format(format);
                                this.plugin.time = formatDateTime;
                                for (final Player p : Bukkit.getOnlinePlayers()) {
                                    if (!p.isOp() || this.plugin.getConfig().getBoolean("Allow-Op")) {
                                        this.plugin.played.add(p);
                                        this.plugin.cache2.add(p);
                                        p.openInventory(this.plugin.inv);
                                    }
                                }
                                if (!plugin.latest) {
                                	player.sendMessage(ChatColor.RED + "You are not using the latest version of Hide Or Hunt!");
                                    player.sendMessage(ChatColor.AQUA + "Download the newest version here:");
                                    player.sendMessage("https://www.spigotmc.org/resources/hide-or-hunt-plugin.79307/");
                                }
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.RED + args[1] + " Is not a number!");
                        }
                    }
                    if (args[0].equalsIgnoreCase("freeze") && this.plugin.gameOn && player.hasPermission("hoh.freeze")) {
                        if (this.plugin.freeze) {
                            Bukkit.broadcastMessage(ChatColor.RED + "UnFreezed the game!");
                            this.plugin.freeze = false;
                        }
                        else {
                            Bukkit.broadcastMessage(ChatColor.RED + "Freezed the game!");
                            this.plugin.freeze = true;
                        }
                    }
                    if (args[0].equalsIgnoreCase("reload") && player.hasPermission("hoh.reload")) {
                        this.plugin.reloadConfig();
                        player.sendMessage(ChatColor.RED + "Config Has Been Reloaded!");
                    }
                    if (args[0].equalsIgnoreCase("beacon") && player.hasPermission("hoh.beacon")) {
                        if (this.plugin.played.contains(player)) {
                            player.sendMessage("You cannot do this!" + ChatColor.DARK_RED + "Error: Possible Cheat Attempt!");
                        }
                        else if (args[1] != null) {
                            final List<String> playerNames = new ArrayList<String>();
                            for (final Player p2 : Bukkit.getOnlinePlayers()) {
                                playerNames.add(p2.getName().toLowerCase());
                            }
                            if (playerNames.contains(args[1].toLowerCase())) {
                                final Player teleport = Bukkit.getPlayer(args[1]);
                                final String whichTeam = this.plugin.voids.getPlayerTeam(teleport);
                                for (final Location loc : this.plugin.blockLocation.keySet()) {
                                    if (this.plugin.blockLocation.get(loc).equals(whichTeam)) {
                                        player.teleport(loc);
                                    }
                                }
                            }
                            else {
                                player.sendMessage(ChatColor.RED + "Invalid Player!");
                            }
                        }
                    }
                    if (args[0].equalsIgnoreCase("chat")) {
                        if (this.plugin.gameOn) {
                            if (this.plugin.played.contains(player)) {
                                if (this.plugin.teamChat.contains(player)) {
                                    this.plugin.teamChat.remove(player);
                                    player.sendMessage(ChatColor.AQUA + "Team chat disabled");
                                }
                                else {
                                    this.plugin.teamChat.add(player);
                                    player.sendMessage(ChatColor.AQUA + "Team chat enabled");
                                }
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "Game is not on!");
                        }
                    }
                    if (args[0].equalsIgnoreCase("rules")) {
                        final List<String> rules = (List<String>)this.plugin.getConfig().getStringList("Rules-Messages");
                        for (final String rule : rules) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', rule));
                        }
                    }
                    if (args[0].equalsIgnoreCase("spy") && player.hasPermission("hoh.spy")) {
                        if (player.isOp()) {
                            if (this.plugin.played.contains(player)) {
                                if (!this.plugin.getConfig().getBoolean("Allow-Op")) {
                                    if (this.plugin.staffSpy.contains(player)) {
                                        this.plugin.staffSpy.remove(player);
                                        player.sendMessage(ChatColor.LIGHT_PURPLE + "Staff Spy Disabled!");
                                    }
                                    else {
                                        this.plugin.staffSpy.add(player);
                                        player.sendMessage(ChatColor.LIGHT_PURPLE + "Staff Spy Enabled!");
                                    }
                                }
                                else {
                                    Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " just went in staff spy mode, Possible Cheat Attempt!");
                                }
                            }
                            else {
                                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " just went in staff spy mode, Possible Cheat Attempt!");
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "You are not op!");
                        }
                    }
                    if (args[0].equalsIgnoreCase("stats") && player.hasPermission("hoh.stats")) {
                    	plugin.voids.createMyInv();
                    	player.openInventory(plugin.stats);
                    }
                    if (args[0].equalsIgnoreCase("stop") && player.hasPermission("hoh.stop")) {
                    	if (plugin.gameOn)
                    		stop(player);
                    	else
                    		player.sendMessage("There is no ongoing game!");
                    }
                    if (args[0].equalsIgnoreCase("list")) {
                    	if (plugin.gameOn) {
                    		Inventory iii = Bukkit.createInventory(null, 54, ChatColor.AQUA + "TEAMS");
                    		int[] i  = {0};
                    		plugin.data.getConfig().getConfigurationSection(plugin.time).getKeys(false).forEach(key -> {
                    			List<String> ourLore = new ArrayList<String>();
                    			for (String s : plugin.data.getConfig().getStringList(plugin.time + "." + key)) {
                    				ourLore.add(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName());
                    			}
                    			ItemStack item = new ItemStack(Material.BEACON);
                    			ItemMeta meta = item.getItemMeta();
                    			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', key));
                    			meta.setLore(ourLore);
                    			item.setItemMeta(meta);
                    			iii.setItem(i[0], item);
                    			i[0]++;
                    		});
                       	}
                    }
                }
            }	
        }
        return false;
    }
    
    public void stop(Player player) {
    	
    	plugin.worldGame.getWorldBorder().setSize(100000000);
    	
    	for (Location loc : plugin.blockLocation.keySet()) {
    		plugin.worldGame.getBlockAt(loc).setType(Material.AIR);
    	}
    	for (Player p : plugin.played) {
    		p.getInventory().clear();
    		p.teleport(plugin.lolloc);
    		p.setScoreboard(plugin.manager.getNewScoreboard());
    	}
    	plugin.gameOn = false;
      	plugin.ep = 1;
    	plugin.graceOn = false;
    	plugin.blockLocation.clear();
    	plugin.scoreboardHolder.clear();
    	
    	this.plugin.getServer().getScheduler().cancelTasks(plugin);
    	new Metrics(plugin, 8350);
    	
    	plugin.played.clear();
    	plugin.chat.clear();
    	plugin.deadTalk.clear();
    	plugin.staffSpy.clear();
    	plugin.teamChat.clear();
    	plugin.noBeacon.clear();
    	plugin.banList.clear();
    	plugin.cache.clear();
    	plugin.cache2.clear();
    	plugin.scoreBoardRegister();
    	player.sendMessage(ChatColor.RED + "Game is stopped!");
    }
    
}
