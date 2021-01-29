package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDrop implements Listener {

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        HideOrHunt plugin = HideOrHunt.getInstance();
        if (plugin.game.gameState != GameState.ON) return;
        if (event.getItemDrop().getItemStack().getType() == Material.BEACON) {
            event.setCancelled(true);
        }
    }
}
