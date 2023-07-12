package me.latestion.hoh.illegalbase;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.api.HOHBeaconBreakEvent;
import me.latestion.hoh.game.HOHTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitScheduler;

public class Base {

    public HOHTeam team;
    public boolean isLegal = false;

    public Base(HOHTeam team) {
        this.team = team;
    }

    private void check() {
        try {
            new BaseHandler(this).isLegal(team.getBeacon(), false, null, true);
        }
        catch (RuntimeException exception) {
            return;
        }
    }

    public void checkLegalBase() {
        if (!HideOrHunt.getInstance().getConfig().getBoolean("Legal-Base-Detector")) return;
        check();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(HideOrHunt.getInstance(), () -> {
            if (isLegal) return;
            int timeToFixBase = HideOrHunt.getInstance().getConfig().getInt("Time-To-Fix-Base");
            team.players.forEach(player -> { if (player.getPlayer().isOnline())
                player.getPlayer().sendMessage(ChatColor.RED + "Invalid Base! You have " + ((int) timeToFixBase / 60) + "mins to fix your base!");});
            scheduler.scheduleSyncDelayedTask(HideOrHunt.getInstance(), () -> {
                if (!team.hasBeacon()) return;
                check();
                scheduler.scheduleSyncDelayedTask(HideOrHunt.getInstance(), () -> {
                    if (isLegal) return;
                    boolean removeBeacon = HideOrHunt.getInstance().getConfig().getBoolean("Remove-Beacon-If-Not-Fixed");
                    if (!removeBeacon) return;
                    HOHBeaconBreakEvent e = new HOHBeaconBreakEvent(team.getBeacon(), team, null);
                    Bukkit.getPluginManager().callEvent(e);
                    if (e.isCancelled()) return;
                    team.getBeacon().breakNaturally();
                    HideOrHunt.getInstance().sbUtil.beaconBreakTeam(team.getName());
                    team.doAsthetic(null);
                    team.setBeacon(null);
                    return;
                }, 20L);
            }, timeToFixBase * 20L);
        }, 20L);
    }
}
