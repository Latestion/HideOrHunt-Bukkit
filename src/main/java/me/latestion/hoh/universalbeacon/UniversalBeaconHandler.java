package me.latestion.hoh.universalbeacon;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class UniversalBeaconHandler implements Listener {

    private UniversalBeacon ub;

    public UniversalBeaconHandler(UniversalBeacon universalBeacon) {
        this.ub = universalBeacon;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory) return;
        if (!event.getClickedInventory().equals(ub.getInv())) return;
        event.setCancelled(true);
        if (HideOrHunt.getInstance().game.getGameState() != GameState.ON) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        UUID player = event.getWhoClicked().getUniqueId();
        if (!ub.unlocked.containsKey(player)) return;
        ItemStack item = event.getCurrentItem();
        Material type = item.getType();


    }

}
