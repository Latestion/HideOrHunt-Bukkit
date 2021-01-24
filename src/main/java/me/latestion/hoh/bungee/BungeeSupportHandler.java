package me.latestion.hoh.bungee;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class BungeeSupportHandler implements Listener {

    private BungeeSupport support;

    public BungeeSupportHandler(BungeeSupport support) {
        this.support = support;
    }

    @EventHandler
    public void pLeave(PlayerQuitEvent event) {
        for (int i : support.queue.keySet()) {
            if (support.queue.get(i).contains(event.getPlayer())) {
                support.queue.get(i).remove(event.getPlayer());
            }
        }
    }
}
