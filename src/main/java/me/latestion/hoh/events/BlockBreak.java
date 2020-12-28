package me.latestion.hoh.events;

import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.utils.Util;

public class BlockBreak implements Listener {

	private HideOrHunt plugin;

	public BlockBreak(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		MessageManager messageManager = plugin.getMessageManager();
		if (GameState.getCurrentGameState() != GameState.ON) return;
		if (event.getBlock().getType() != Material.BEACON) return;
		Player player = event.getPlayer();
		HOHPlayer hohPlayer = plugin.getHohPlayer(player.getUniqueId());
		Util util = new Util(plugin);
		HOHTeam team = util.getTeamFromBlock(event.getBlock());
		if (team == null)
			return;
		if (team.equals(hohPlayer.getTeam())) {
			event.setCancelled(true);
			player.sendMessage(messageManager.getMessage("cannot-break-own-beacon"));
			return;
		}
		event.setDropItems(false);
		team.hasBeacon = false;
		plugin.sbUtil.beaconBreakTeam(team.getName());
		doAsthetic(team, event.getPlayer());
	}

	private void doAsthetic(HOHTeam team, Player player) {
		MessageManager messageManager = plugin.getMessageManager();
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
		String eliminatedMsg = messageManager.getMessage("eliminated-broadcast").replace("%eliminated-team%", team.getName());
		eliminatedMsg = eliminatedMsg.replace("%eliminating-team%", plugin.game.hohPlayers.get(player.getUniqueId()).getTeam().getName());
		Bukkit.broadcastMessage(eliminatedMsg);
		for (HOHPlayer p : team.players) {
			p.getPlayer().sendTitle(messageManager.getMessage("beacon-destroyed-title"), "", 10, 40, 10);
			p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
		}
	}

}
