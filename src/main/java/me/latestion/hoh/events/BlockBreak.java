package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.api.HOHBeaconBreakEvent;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.localization.MessageManager;
import me.latestion.hoh.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private HideOrHunt plugin;

    public BlockBreak(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        MessageManager messageManager = plugin.getMessageManager();
        if (plugin.game.getGameState() != GameState.ON) return;
        if (event.getBlock().getType() == Material.BEACON) {
            Player player = event.getPlayer();
            HOHPlayer hohPlayer = plugin.game.getHohPlayer(player.getUniqueId());
            Util util = new Util(plugin);
            HOHTeam team = util.getTeamFromBlock(event.getBlock());
            if (team == null)
                return;
            HOHBeaconBreakEvent e = new HOHBeaconBreakEvent(event.getBlock(), team, player.getUniqueId());
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            if (team.equals(hohPlayer.getTeam())) {
                event.setCancelled(true);
                player.sendMessage(messageManager.getMessage("cannot-break-own-beacon"));
                return;
            }
            event.setDropItems(false);
            team.setBeacon(null);
            plugin.sbUtil.beaconBreakTeam(team.getName());
            team.doAsthetic(event.getPlayer());
            if (plugin.getConfig().getBoolean("End-Game-If-Only-One-Beacon")) {
                int total = 0;
                HOHTeam winner = null;
                for (HOHTeam t : plugin.game.getTeams().values()) {
                    if (t.hasBeacon()) {
                        total++;
                        winner = t;
                    }
                }
                if (total == 1) {
                    plugin.game.endGame(winner.getName());
                }
            }
            return;
        }
        if (event.getBlock().getType() == Material.OAK_SIGN) {
            if (event.getBlock().getLocation().getWorld().getBlockAt(event.getBlock().getLocation().subtract(0, 1, 0)).getType() == Material.BEACON) {
                event.setCancelled(true);
                return;
            }
            return;
        }
        return;
    }
}
