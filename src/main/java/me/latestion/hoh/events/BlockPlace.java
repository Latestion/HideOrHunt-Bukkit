package me.latestion.hoh.events;

import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;

public class BlockPlace implements Listener {

	private HideOrHunt plugin;
	
	public BlockPlace(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		
		if (plugin.game.gameState != GameState.ON || plugin.game.frozen){
			event.setCancelled(true);
			return;
		}
		
		if (event.getBlockPlaced().getType() == Material.BEACON) {
			HOHPlayer player = plugin.game.hohPlayers.get(event.getPlayer().getUniqueId());
			Location loc = event.getBlockPlaced().getLocation();
			player.getTeam().setBeacon(event.getBlockPlaced());
			MessageManager messageManager = plugin.getMessageManager();
			Bukkit.broadcastMessage(messageManager.getMessage("beacon-placed-broadcast").replace("%team%", player.getTeam().getName()));
			setSign(loc, player);
			plugin.sbUtil.beaconPlaceTeam(player.getTeam().getName());
		}
	}
	
	public void setSign(Location loc, HOHPlayer player) {
        loc.getWorld().getBlockAt(loc.clone().add(0, 1, 0)).setType(Material.OAK_SIGN);
        Sign sign =  (Sign) loc.getWorld().getBlockAt(loc.clone().add(0, 1, 0)).getState();
        sign.setLine(0, player.getTeam().getName());
        sign.update();
	}
	
}
