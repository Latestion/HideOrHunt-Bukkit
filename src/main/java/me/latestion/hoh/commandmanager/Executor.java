package me.latestion.hoh.commandmanager;

import java.util.ArrayList;
import java.util.List;

import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.utils.Util;

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
        MessageManager messageManager = plugin.getMessageManager();

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
						if (GameState.getCurrentGameState() != GameState.OFF) {
							return false;
						}
						HOHGame game = new HOHGame(plugin, player.getLocation(), size);
						plugin.game = game;
					}	
				}
			}
			if (args[0].equalsIgnoreCase("freeze") && GameState.getCurrentGameState() == GameState.ON && player.hasPermission("hoh.freeze")) {
                if (plugin.game.freeze) {
                    Bukkit.broadcastMessage(messageManager.getMessage("unfreezed-game"));
                    plugin.game.freeze = false;
                }
                else {
                    Bukkit.broadcastMessage(messageManager.getMessage("freezed-game"));
                    plugin.game.freeze = true;
                }
			}
            if (args[0].equalsIgnoreCase("reload") && player.hasPermission("hoh.reload")) {
                this.plugin.reloadConfig();
                player.sendMessage(messageManager.getMessage("reloaded-config"));
            }
            if (args[0].equalsIgnoreCase("rules")) {
                List<String> rules = plugin.getConfig().getStringList("Rules-Messages");
                for (String rule : rules) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', rule));
                }
            }
            if (args[0].equalsIgnoreCase("stop") && player.hasPermission("hoh.stop")) {
            	if (GameState.getCurrentGameState() == GameState.ON)
            		plugin.game.stop();
            	else
            		player.sendMessage(messageManager.getMessage("game-not-started"));
            }
            if (args[0].equalsIgnoreCase("beacon") && player.hasPermission("hoh.beacon") 
            		&& GameState.getCurrentGameState() == GameState.ON) {
                if (plugin.hohPlayers.containsKey(player.getUniqueId())) {
                    player.sendMessage(messageManager.getMessage("possible-cheat-attempt"));
                }
                else if (args[1] != null) {
                    List<String> playerNames = new ArrayList<String>();
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        playerNames.add(p2.getName().toLowerCase());
                    }
                    if (playerNames.contains(args[1].toLowerCase())) {
                        Player teleport = Bukkit.getPlayerExact(args[1]);
                        player.teleport(plugin.hohPlayers.get(teleport.getUniqueId()).getTeam().getBeacon().getLocation());
                    }
                    else {
                        player.sendMessage(messageManager.getMessage("invalid-player"));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("chat")) {
                if (GameState.getCurrentGameState() == GameState.ON) {
                    if (plugin.hohPlayers.containsKey(player.getUniqueId())) {
                    	HOHPlayer p = plugin.hohPlayers.get(player.getUniqueId());
                        if (p.teamChat) {
                            p.teamChat = false;
                            player.sendMessage(messageManager.getMessage("team-chat-off"));
                        }
                        else {
                            p.teamChat = true;
                            player.sendMessage(messageManager.getMessage("team-chat-on"));
                        }
                    }
                }
                else {
                    player.sendMessage(messageManager.getMessage("game-not-started"));
                }
            }
			
		}
		
		return false;
	}

}
