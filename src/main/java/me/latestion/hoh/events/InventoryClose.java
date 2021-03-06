package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {
    private HideOrHunt plugin;

    public InventoryClose(final HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (plugin.game.getGameState() == GameState.OFF)
            return;
        HOHPlayer hohPlayer = plugin.game.getHohPlayer(event.getPlayer().getUniqueId());
        if (!hohPlayer.hasTeam()) {
            Player player = (Player) event.getPlayer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(plugin.game.inv), 1L);
        }
    }
}
