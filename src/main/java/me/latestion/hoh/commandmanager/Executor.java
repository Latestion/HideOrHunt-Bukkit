package me.latestion.hoh.commandmanager;

import java.io.File;
import java.util.List;
import java.util.Optional;

import me.latestion.hoh.data.flat.FlatHOHGame;
import me.latestion.hoh.game.HOHTeam;
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
			HOHGame game = plugin.game;

			if (args[0].equalsIgnoreCase("start")) {
				if (player.hasPermission("hoh.start")) {
					if (args.length == 2) {
						Util util = new Util(plugin);
						int size = util.getInt(args[1]);
						if (size == 0) {
							return false;
						}
						if (game.gameState != GameState.OFF) {
							return false;
						}
						game.setSpawnLocation(player.getLocation());
						game.teamSize = size;
						game.prepareGame();
					}
				}
			}
			if (args[0].equalsIgnoreCase("freeze") && plugin.game.gameState == GameState.ON && player.hasPermission("hoh.freeze")) {
				if (plugin.game.frozen) {
					Bukkit.broadcastMessage(messageManager.getMessage("unfreezed-game"));
					plugin.getGame().unFreeze();
				} else {
					Bukkit.broadcastMessage(messageManager.getMessage("freezed-game"));
					plugin.getGame().freeze();
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
				if (plugin.game.gameState == GameState.ON)
					plugin.game.endGame();
				else
					player.sendMessage(messageManager.getMessage("game-not-started"));
			}
			if (args[0].equalsIgnoreCase("beacon") && player.hasPermission("hoh.beacon")
					&& plugin.game.gameState == GameState.ON) {
				 if (args[1] != null) {
					Optional<HOHPlayer> op = game.getHohPlayers().values().stream().filter(p -> p.getName().equalsIgnoreCase(args[1])).findAny();
					if(op.isPresent()){
						HOHPlayer p = op.get();
						HOHTeam t = p.getTeam();
						if(t.hasBeacon()){
							player.teleport(t.getBeacon().getLocation());
						}else{
							//TODO send message
						}
					} else {
						player.sendMessage(messageManager.getMessage("invalid-player"));
					}
				}
			}
			if (args[0].equalsIgnoreCase("chat")) {
				if (plugin.game.gameState == GameState.ON) {
					if (plugin.game.hohPlayers.containsKey(player.getUniqueId())) {
						HOHPlayer p = plugin.game.hohPlayers.get(player.getUniqueId());
						if (p.teamChat) {
							p.teamChat = false;
							player.sendMessage(messageManager.getMessage("team-chat-off"));
						} else {
							p.teamChat = true;
							player.sendMessage(messageManager.getMessage("team-chat-on"));
						}
					}
				} else {
					player.sendMessage(messageManager.getMessage("game-not-started"));
				}
			}
			if (args[0].equalsIgnoreCase("save")) {
				if (player.hasPermission("hoh.save")) {
					FlatHOHGame.save(game, plugin, new File(plugin.getDataFolder(), "hohGame.yml"));
				}
			}
			if (args[0].equalsIgnoreCase("reaload")) {
				if (player.hasPermission("hoh.reload")) {
					plugin.reloadConfig();
				}
			}
		}

		return false;
	}

}
