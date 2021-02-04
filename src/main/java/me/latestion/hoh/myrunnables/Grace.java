package me.latestion.hoh.myrunnables;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHTeam;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Grace extends BukkitRunnable {

    private HideOrHunt plugin;

    public Grace(HideOrHunt plugin) {
        this.plugin = plugin;
        plugin.game.grace = true;
        long delay = plugin.getConfig().getInt("Grace-Duration") * 20 * 60;
        runTaskLater(plugin, delay);
    }

    @Override
    public void run() {
        // Grace Ended
        if (plugin.game.getGameState() != GameState.ON) cancel();
        if (plugin.game.grace) {
            plugin.game.graceOff();
            checkLegalBase();
        }
    }

    private void checkLegalBase() {
        if (plugin.getConfig().getBoolean("Check-When-Grace-End") && plugin.getConfig().getBoolean("Legal-Base-Detector")) {
            for (HOHTeam teams : plugin.game.getTeams().values()) teams.getBase().checkLegalBase();
        }
    }
}
