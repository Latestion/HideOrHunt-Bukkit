package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerWorld implements Listener {

    private final HideOrHunt plugin;

    public PlayerWorld(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void changeWorld(PlayerPortalEvent event) {
        if (plugin.game.gameState != GameState.ON) return;
        if (this.plugin.getConfig().getBoolean("Disable-Other-Worlds")) {
            event.setCancelled(true);
        }
    }
}
