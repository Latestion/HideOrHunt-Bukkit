package me.latestion.hoh.bungee;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.api.HOHGameEvent;
import me.latestion.hoh.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class BungeeSupportHandler implements Listener {

    private BungeeSupport support;

    public BungeeSupportHandler(BungeeSupport support) {
        this.support = support;
    }

    @EventHandler
    public void pLeave(PlayerQuitEvent event) {
        if (!support.isHub) return;
        support.removeQueuePlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void pJoin(PlayerJoinEvent event) {
        if (support.isHub) return;
        ServerState ss = support.state.get(support.thisServer);
        if (ss.maxPlayers == Bukkit.getOnlinePlayers().size()) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HideOrHunt.getInstance(), () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hoh start" + ss.teamsize);
            }, 2 * 20L);
        }
    }

    @EventHandler
    public void gameEnd(HOHGameEvent event) {
        if (event.getState() == GameState.OFF) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HideOrHunt.getInstance(), () -> {

                support.pm.customHOH(false, support.thisServer);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    support.pm.connect(player, support.hub);
                }

                for (World w : Bukkit.getWorlds()) {
                    support.deleteWorlds(w.getWorldFolder());
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
            }, 10 * 20L);
        }
        if (event.getState() == GameState.ON) {
            support.pm.customHOH(true, support.thisServer);
        }
    }
}
