package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
                plugin.game.endGame(winnerTeam.getName());
            }
        }
    }
}
