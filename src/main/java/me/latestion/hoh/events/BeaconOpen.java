package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.utils.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BeaconOpen implements Listener {

    private HideOrHunt plugin;

    public BeaconOpen(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (plugin.game.getGameState() != GameState.ON) return;
        if (event.getClickedBlock().getType() != Material.BEACON) return;
        event.setCancelled(true);
        Block block = event.getClickedBlock();
        HOHTeam team = new Util(plugin).getTeamFromBlock(block);
        HOHTeam playerTeam = plugin.game.getHohPlayer(event.getPlayer().getUniqueId()).getTeam();
        if (playerTeam == null || team == null) return;
        if (plugin.getConfig().getBoolean("Allow-Other-Team-Beacon-Opening")) {
            event.getPlayer().openWorkbench(null, true);
            return;
        }
        if (team != playerTeam) {
            return;
        }
        event.getPlayer().openWorkbench(null, true);
    }

}
