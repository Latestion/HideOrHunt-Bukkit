package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.customitems.TrackingItem;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author barpec12 on 13.01.2021
 */
public class PlayerInteract implements Listener {

	public PlayerInteract(){

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(e.hasItem()){
			ItemStack item = e.getItem();
			if(TrackingItem.isTrackingItem(item)){
				TrackingItem trackingItem = TrackingItem.getTrackingItem(p);
				trackingItem.processRightClick();
			}
		}
	}
}