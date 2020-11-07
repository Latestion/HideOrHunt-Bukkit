package me.Latestion.HOH.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.Latestion.HOH.HideOrHunt;
import me.Latestion.HOH.Game.GameState;
import me.Latestion.HOH.Game.HOHPlayer;

public class BlockPlace implements Listener {

	private HideOrHunt plugin;
	
	public BlockPlace(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		
		if (GameState.getCurrentGamestate() != GameState.ON) return;
		
		if (event.getBlockPlaced().getType() == Material.BEACON) {
			HOHPlayer player = plugin.hohPlayer.get(event.getPlayer().getUniqueId());
			Location loc = event.getBlockPlaced().getLocation();
			player.getTeam().hasBeacon = true;
			player.getTeam().setBeacon(event.getBlockPlaced());
			Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', player.getTeam().getName()) + ChatColor.AQUA + " has placed their beacon!");
			setSign(loc, player);
			plugin.sbUtil.beaconPlaceTeam(player.getTeam().getName());
			player.getTeam().hasBeacon = true;
		}
	}
	
	public void setSign(Location loc, HOHPlayer player) {
        loc.getWorld().getBlockAt(loc.clone().add(0, 1, 0)).setType(Material.OAK_SIGN);
        Sign sign =  (Sign) loc.getWorld().getBlockAt(loc.clone().add(0, 1, 0)).getState();
        sign.setLine(0, player.getTeam().getName());
        sign.update();
	}
	
}
