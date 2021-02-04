package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class TrulyGrace implements Listener {

    private final HideOrHunt plugin;

    public TrulyGrace(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (plugin.game.getGameState() != GameState.ON) return;
        if (plugin.game.grace) {
            if (plugin.getConfig().getBoolean("Grace-Period-Peaceful")) if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void target(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) return;
        if (plugin.game.getGameState() != GameState.ON) return;
        if (plugin.game.grace && plugin.getConfig().getBoolean("Grace-Period-Peaceful"))
            event.setCancelled(true);
    }
}
