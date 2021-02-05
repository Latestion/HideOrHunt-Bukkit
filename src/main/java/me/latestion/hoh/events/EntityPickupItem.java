package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.customitems.TrackingItem;
import me.latestion.hoh.game.HOHGame;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author barpec12 on 13.01.2021
 */
public class EntityPickupItem implements Listener {

	public EntityPickupItem(){

	}

	@EventHandler
	public void onEntityPickupItem(EntityPickupItemEvent e){
		ItemStack item = e.getItem().getItemStack();
		if(TrackingItem.isTrackingItem(item)) {
			if(!e.getEntityType().equals(EntityType.PLAYER)){
				e.setCancelled(true);
				return;
			}
			Player p = (Player) e.getEntity();
			TrackingItem trackingItem = TrackingItem.getTrackingItem(p);
			trackingItem.hideTracking();
			item.setType(Material.CLOCK);
		}
	}
}
