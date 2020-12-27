package me.latestion.hoh.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
		if (GameState.getCurrentGamestate() != GameState.ON) return;
		if (event.getBlock().getType() != Material.BEACON) return;
		Player player = event.getPlayer();
		Util util = new Util(plugin);
		HOHTeam team = util.getTeamFromBlock(event.getBlock());
		if (team.players.contains(plugin.hohPlayer.get(player.getUniqueId()))) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot break your own beacon!");
			return;
		}
		event.setDropItems(false);
		team.hasBeacon = false;
		plugin.sbUtil.beaconBreakTeam(team.getName());
		doAsthetic(team, event.getPlayer());
	}
	
	private void doAsthetic(HOHTeam team, Player player) {
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
		Bukkit.broadcastMessage(ChatColor.GREEN + team.getName() + ChatColor.BOLD + "" + ChatColor.RED + " was eliminated by " + 
	        	ChatColor.GREEN	+ plugin.hohPlayer.get(player.getUniqueId()).getTeam().getName() + ".");
        for (HOHPlayer p : team.players) {
        	p.getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.RED + "Beacon Destroyed", "", 10, 40, 10);
        	p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
        }
	}
	
}
