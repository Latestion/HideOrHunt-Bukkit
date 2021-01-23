package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.localization.MessageManager;
import me.latestion.hoh.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AsyncChat implements Listener {
    private HideOrHunt plugin;

    public AsyncChat(final HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void ce(AsyncPlayerChatEvent event) {
        HOHPlayer hohPlayer = plugin.game.getHohPlayer(event.getPlayer().getUniqueId());
        if (hohPlayer == null) return;
        if (hohPlayer.isNamingTeam()) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            String name = event.getMessage();
            MessageManager messageManager = plugin.getMessageManager();
            if (name.length() > 15) {
                player.sendMessage(messageManager.getMessage("too-many-characters"));
                return;
            }
            if (plugin.game.gameState != GameState.PREPARE) {
                return;
            }
            Util util = new Util(plugin);
            if (util.isTeamNameTaken(name)) {
                player.sendMessage(messageManager.getMessage("team-name-taken"));
                return;
            }
            player.sendMessage(messageManager.getMessage("team-name-set").replace("%name%", name));
            int id = hohPlayer.getTeam().getID();
            ItemStack item = plugin.game.inv.getItem(id);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
            plugin.game.inv.setItem(id, item);
            for (HumanEntity h : plugin.game.inv.getViewers()) {
                if (h instanceof Player)
                    ((Player) h).updateInventory();
            }

            hohPlayer.getTeam().setName(name);
            hohPlayer.setNamingTeam(false);

            if (plugin.game.allPlayersSelectedTeam() && plugin.game.areAllTeamsNamed()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.game.startGame(), 1L);
            }
            return;
        }
        if (plugin.game.gameState != GameState.ON) return;

        if (!plugin.game.hohPlayers.containsKey(event.getPlayer().getUniqueId())) return;

        HOHPlayer player = plugin.game.hohPlayers.get(event.getPlayer().getUniqueId());
        event.setCancelled(true);
        if (player.dead) return;


        String message = event.getMessage();
        String format = (player.teamChat) ? plugin.getConfig().getString("Team-Chat-Format") : plugin.getConfig().getString("Main-Chat-Format");
        String f = format.replace("%message%", message);
        String g = f.replace("%playername%", event.getPlayer().getName());
        String h = g.replace("%playerteam%", player.getTeam().getName());
        event.setFormat(ChatColor.translateAlternateColorCodes('&', h));

        if (!player.teamChat) return;

        event.getRecipients().clear();
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (plugin.game.isSpying(p)) event.getRecipients().add(p);
        });
        player.getTeam().players.forEach(p -> {
            if (p.getPlayer().isValid())
                event.getRecipients().add(p.getPlayer());
        });

    }


}
