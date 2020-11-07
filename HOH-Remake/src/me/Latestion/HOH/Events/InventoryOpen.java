package me.Latestion.HOH.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import me.Latestion.HOH.Game.GameState;

public class InventoryOpen implements Listener {
	
    public InventoryOpen() {
     
    }
    
    @EventHandler
    public void ioe(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (GameState.getCurrentGamestate() != GameState.ON) return;
        if (event.getInventory().getType().equals(InventoryType.BEACON)) {
            event.setCancelled(true);
            player.openWorkbench(null, true);
        }
    }
}
