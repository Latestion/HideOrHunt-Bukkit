package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {

    private HideOrHunt plugin;

    public EntityExplode(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void explode(EntityExplodeEvent event) {
        if (plugin.game == null) return;
        if (plugin.game.gameState == GameState.ON) {
            for (Block block : event.blockList()) {
                event.blockList().removeIf(b -> b.getType() == Material.BEACON);
            }
        }
    }
}
