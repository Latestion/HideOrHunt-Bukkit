package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.api.HOHBeaconPlaceEvent;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    private HideOrHunt plugin;

    public BlockPlace(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if (plugin.game.gameState != GameState.ON) return;

        if (event.getBlockPlaced().getType() == Material.BEACON) {

            HOHPlayer player = plugin.game.hohPlayers.get(event.getPlayer().getUniqueId());
            HOHTeam team = player.getTeam();
            Location loc = event.getBlockPlaced().getLocation();
            boolean success = true;

            if (plugin.game.ep != 1) {
				/*
				Failed To Place Beacon
				 */
                event.getPlayer().sendMessage(ChatColor.RED + "You have failed to place the beacon!");
                event.setCancelled(true);
                event.getItemInHand().setType(Material.AIR);
                success = false;
            }

            HOHBeaconPlaceEvent e = new HOHBeaconPlaceEvent(event.getBlock(), team, player.getUUID(), success);
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            if (success) {
                player.getTeam().setBeacon(event.getBlockPlaced());
                MessageManager messageManager = plugin.getMessageManager();
                Bukkit.broadcastMessage(messageManager.getMessage("beacon-placed-broadcast").replace("%team%", player.getTeam().getName()));
                player.getTeam().setSign(setSign(loc, player));
                plugin.sbUtil.beaconPlaceTeam(player.getTeam().getName());
                if (loc.getBlockY() == 256) {
                    event.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "You have found an easter egg! Contact Latestion#0529 on discord with a screenshot!");
                }
                try {
                    team.checkLegalBase();
                }
                catch (RuntimeException runtimeException) {

                }
            }
        }
    }

    private Block setSign(Location loc, HOHPlayer player) {
        loc.getWorld().getBlockAt(loc.clone().add(0, 1, 0)).setType(Material.OAK_SIGN);
        Block block = loc.getWorld().getBlockAt(loc.clone().add(0, 1, 0));
        Sign sign = (Sign) block.getState();
        sign.setLine(0, player.getTeam().getName());
        sign.update();
        return block;
    }

}
