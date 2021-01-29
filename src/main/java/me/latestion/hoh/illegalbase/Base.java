package me.latestion.hoh.illegalbase;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.api.HOHBeaconBreakEvent;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;

public class Base {

    public HOHTeam team;
    public boolean isLegal = false;

    public Base(HOHTeam team) {
        this.team = team;
    }

    private void check() {
        new BaseHandler(this).isLegal(team.getBeacon(), false, null, false);
    }

    public void checkLegalBase() {
        if (!HideOrHunt.getInstance().getConfig().getBoolean("Legal-Base-Detector")) return;
        check();

        // TODO: USE A SCHEDULER

        if (isLegal) {
            // Legal base
            // Check back later
        }
        else {
            // Not legal base
            int timeToFixBase = HideOrHunt.getInstance().getConfig().getInt("Time-To-Fix-Base");
            for (HOHPlayer player : team.players) {
                if (player.getPlayer().isOnline()) {
                    player.getPlayer().sendMessage(ChatColor.RED + "Invalid Base! You have " + ((int) timeToFixBase / 60) + "mins to fix your base!");
                }
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HideOrHunt.getInstance(), () -> {
                if (team.hasBeacon()) {
                    try {
                        check();
                    }
                    catch (RuntimeException exception) {

                    }
                    if (!isLegal) {
                        boolean removeBeacon = HideOrHunt.getInstance().getConfig().getBoolean("Remove-Beacon-If-Not-Fixed");
                        if (removeBeacon) {
                            HOHBeaconBreakEvent e = new HOHBeaconBreakEvent(team.getBeacon(), team, null);
                            Bukkit.getPluginManager().callEvent(e);
                            if (e.isCancelled()) {
                                return;
                            }
                            team.getBeacon().breakNaturally();
                            HideOrHunt.getInstance().sbUtil.beaconBreakTeam(team.getName());
                            team.doAsthetic(null);
                            team.setBeacon(null);
                            return;
                        }
                    }
                }
            }, timeToFixBase * 20L);
        }
    }
}
