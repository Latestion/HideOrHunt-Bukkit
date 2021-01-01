package me.latestion.hoh.events;

import java.util.Random;
import java.util.UUID;

import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.utils.Util;

public class PlayerDeath implements Listener {

	private HideOrHunt plugin;
	private Util util;

	public PlayerDeath(HideOrHunt plugin) {
		this.plugin = plugin;
		this.util = new Util(plugin);
	}

	@EventHandler
	public void death(PlayerDeathEvent event) {
		if (plugin.game.gameState == GameState.ON) {
			HOHPlayer player = plugin.game.hohPlayers.get(event.getEntity().getUniqueId());
			MessageManager messageManager = plugin.getMessageManager();

			if (!player.getTeam().hasBeacon()) {
				String msg;
				msg = messageManager.getMessage("player-eliminated").replace("%player%", player.getPlayer().getDisplayName());
				Bukkit.broadcastMessage(msg);

				if (player.getPlayer().getInventory().containsAtLeast(util.beacon(player.getTeam().getName()), 1)) {
					event.getDrops().remove(util.beacon(player.getTeam().getName()));
				}

				player.banned = true;
				player.dead = true;
				player.getTeam().diedPlayer(player);

				if (player.getTeam().eliminated) {
					msg = messageManager.getMessage("team-eliminated").replace("%team%", player.getTeam().getName());
					Bukkit.broadcastMessage(msg);
					plugin.sbUtil.eliminateTeam(player.getTeam().getName());
				}

				if (this.plugin.getConfig().getBoolean("Ban-Player-On-Death")) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getPlayer().getName() + " Eliminated");
						}
					}, 0L);
				} else {
					player.getPlayer().setGameMode(GameMode.SPECTATOR);
				}

				if (plugin.game.checkEndConditions()) {
					HOHTeam winnerTeam = plugin.game.getWinnerTeam();
					if (winnerTeam == null) return;
					Bukkit.broadcastMessage(messageManager.getMessage("win-message").replace("%winner-team%", winnerTeam.getName()));
					plugin.game.gameState = GameState.OFF;
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							for (UUID p : plugin.game.hohPlayers.keySet()) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pardon " + Bukkit.getPlayer(p).getName());
							}
						}
					}, 0L);
					plugin.game.endGame();
				}

			} else {
				Random rand = new Random();
				int i = rand.nextInt(100);
				if (this.plugin.getConfig().getInt("Inventory-Keep") == 0) {
					return;
				}
				if (i + 1 <= this.plugin.getConfig().getInt("Inventory-Keep")) {
					event.setKeepInventory(true);
					event.getDrops().clear();
					player.getPlayer().sendMessage(messageManager.getMessage("kept-inventory"));
				} else {
					player.getPlayer().sendMessage(messageManager.getMessage("lost-inventory"));
				}
			}
		}
	}
}
