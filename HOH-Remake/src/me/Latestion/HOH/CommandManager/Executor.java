package me.Latestion.HOH.CommandManager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Latestion.HOH.HideOrHunt;
import me.Latestion.HOH.Game.GameState;
import me.Latestion.HOH.Game.HOHGame;
import me.Latestion.HOH.Game.HOHPlayer;
import me.Latestion.HOH.Utils.Util;

public class Executor implements CommandExecutor {

	private HideOrHunt plugin;
	
	public Executor(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (label.equalsIgnoreCase("hoh")) {
			
			if (args.length == 0) {
				/*
				 * Print All Commands Here
				 */
				return false;
			}
			
			if (args[0].equalsIgnoreCase("start")) {
				if (player.hasPermission("hoh.start")) {
					if (args.length == 2) {
						Util util = new Util(plugin);
						int size = util.getInt(args[1]);
						if (size == 0) {
							return false;
						}
						if (GameState.getCurrentGamestate() != GameState.OFF) {
							return false;
						}
						HOHGame game = new HOHGame(plugin, player.getLocation(), size);
						plugin.game = game;
					}	
				}
			}
			if (args[0].equalsIgnoreCase("freeze") && GameState.getCurrentGamestate() == GameState.ON && player.hasPermission("hoh.freeze")) {
                if (plugin.game.freeze) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Unfreezed the game!");
                    plugin.game.freeze = false;
                }
                else {
                    Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Freezed the game!");
                    plugin.game.freeze = true;
                }
			}
            if (args[0].equalsIgnoreCase("reload") && player.hasPermission("hoh.reload")) {
                this.plugin.reloadConfig();
                player.sendMessage(ChatColor.RED + "Config Has Been Reloaded!");
            }
            if (args[0].equalsIgnoreCase("rules")) {
                List<String> rules = plugin.getConfig().getStringList("Rules-Messages");
                for (String rule : rules) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', rule));
                }
            }
            if (args[0].equalsIgnoreCase("stop") && player.hasPermission("hoh.stop")) {
            	if (GameState.getCurrentGamestate() == GameState.ON)
            		plugin.game.stop();
            	else
            		player.sendMessage("There is no ongoing game!");
            }
            if (args[0].equalsIgnoreCase("beacon") && player.hasPermission("hoh.beacon") 
            		&& GameState.getCurrentGamestate() == GameState.ON) {
                if (plugin.hohPlayer.containsKey(player.getUniqueId())) {
                    player.sendMessage("You cannot do this!" + ChatColor.DARK_RED + "Error: Possible Cheat Attempt!");
                }
                else if (args[1] != null) {
                    List<String> playerNames = new ArrayList<String>();
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        playerNames.add(p2.getName().toLowerCase());
                    }
                    if (playerNames.contains(args[1].toLowerCase())) {
                        Player teleport = Bukkit.getPlayerExact(args[1]);
                        player.teleport(plugin.hohPlayer.get(teleport.getUniqueId()).getTeam().getBeacon().getLocation());
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Invalid Player!");
                    }
                }
            }
            if (args[0].equalsIgnoreCase("chat")) {
                if (GameState.getCurrentGamestate() == GameState.ON) {
                    if (plugin.hohPlayer.containsKey(player.getUniqueId())) {
                    	HOHPlayer p = plugin.hohPlayer.get(player.getUniqueId());
                        if (p.teamChat) {
                            p.teamChat = false;
                            player.sendMessage(ChatColor.AQUA + "Team chat disabled");
                        }
                        else {
                            p.teamChat = true;
                            player.sendMessage(ChatColor.AQUA + "Team chat enabled");
                        }
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Game is not on!");
                }
            }
			
		}
		
		return false;
	}

}
