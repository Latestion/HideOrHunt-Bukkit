package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.localization.MessageManager;
import me.latestion.hoh.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuit implements Listener {

    private HideOrHunt plugin;

    public PlayerQuit(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.game.getGameState() == GameState.ON) {
            if (plugin.game.checkEndConditions()) {
                HOHTeam winnerTeam = plugin.game.getWinnerTeam();
                if (winnerTeam == null) return;
                plugin.game.endGame(winnerTeam);
            }
            return;
        }
        if (plugin.game.getGameState() == GameState.PREPARE) {
            Player player = event.getPlayer();
            int secs = plugin.getConfig().getInt("Remove-Player-After");
            if (secs <= 0) return;
            if (plugin.getConfig().getBoolean("Auto-Team-Join")) return;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (player.isOnline()) {
                    return;
                }
                HOHPlayer p = plugin.game.getHohPlayer(player.getUniqueId());
                if (p.getTeam() != null) {
                    p.getTeam().removePlayer(p);
                }
                plugin.game.hohPlayers.remove(player.getUniqueId());

            }, secs * 20L);
        }
    }
}
