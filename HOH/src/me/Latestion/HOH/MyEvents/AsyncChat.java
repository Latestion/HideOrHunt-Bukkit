// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyEvents;

import org.bukkit.event.EventHandler;
import java.util.List;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import java.util.UUID;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.Latestion.HOH.Main;
import org.bukkit.event.Listener;

public class AsyncChat implements Listener
{
    private Main plugin;
    
    public AsyncChat(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void ce(final AsyncPlayerChatEvent event) {
        if (this.plugin.chat.contains(event.getPlayer())) {
            event.setCancelled(true);
            final Player player = event.getPlayer();
            final String name = event.getMessage();
            if (name.length() > 15) {
                player.sendMessage("Too many characters!");
                return;
            }
            if (this.plugin.voids.isTeamTaken(name)) {
                player.sendMessage("Teamname already exists!");
            }
            else {
                player.sendMessage("Teamname set to: " + name);
                final ItemStack item = this.plugin.inv.getItem((int)this.plugin.cache.get(this.plugin.chat.indexOf(player)));
                final ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                item.setItemMeta(meta);
                this.plugin.inv.setItem((int)this.plugin.cache.get(this.plugin.chat.indexOf(player)), item);
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    p.updateInventory();
                }
                final List<String> s = new ArrayList<String>();
                s.add(player.getUniqueId().toString());
                this.plugin.data.getConfig().set(String.valueOf(this.plugin.time) + "." + name, (Object)s);
                this.plugin.data.saveConfig();
                this.plugin.cache.remove(this.plugin.chat.indexOf(player));
                this.plugin.chat.remove(player);
                if (this.plugin.cache2.isEmpty() && this.plugin.cache.isEmpty()) {
                    this.plugin.voids.doIt(this.plugin.started);
                }
            }
        }
        else if (this.plugin.gameOn) {
            final Player player = event.getPlayer();
            final String teamname = this.plugin.voids.getPlayerTeam(player);
            if (teamname == null) {
                return;
            }
            if (teamname != null) {
            	if (!plugin.teamChat.contains(event.getPlayer())) {
                    String message = event.getMessage();
                    String format = plugin.getConfig().getString("Main-Chat-Format");
                    String f = format.replace("%message%", message);
                    String g = f.replace("%playername%", event.getPlayer().getName());
                    String h = g.replace("%playerteam%", plugin.voids.getPlayerTeam(event.getPlayer()));
                    event.setCancelled(true);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', h));	
            	}
            }
        }
        if (this.plugin.gameOn) {
            if (this.plugin.deadTalk.contains(event.getPlayer())) {
                event.setCancelled(true);
                for (final Player send : this.plugin.deadTalk) {
                    send.sendMessage(ChatColor.GRAY + "[DeadTalk]" + event.getPlayer().getName() + ": " + ChatColor.RESET + event.getMessage());
                }
            }
            if (this.plugin.teamChat.contains(event.getPlayer())) {
                event.setCancelled(true);
                final Player player = event.getPlayer();
                final String teamname = this.plugin.voids.getPlayerTeam(player);
                final List<String> sendMessagePlayers = (List<String>)this.plugin.data.getConfig().getStringList(String.valueOf(this.plugin.time) + "." + teamname);
                for (final String sendingMessage : sendMessagePlayers) {
                    Player sendingMessagePlayers = Bukkit.getPlayer(UUID.fromString(sendingMessage));
                    String message = event.getMessage();
                    String format = plugin.getConfig().getString("Team-Chat-Format");
                    String f = format.replace("%message%", message);
                    String g = f.replace("%playername%", event.getPlayer().getName());
                    String h = g.replace("%playerteam%", plugin.voids.getPlayerTeam(event.getPlayer()));
                    sendingMessagePlayers.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', h));
                }
                for (final Player staffSpying : this.plugin.staffSpy) {
                    staffSpying.sendMessage(ChatColor.AQUA + "[StaffSpy]" + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', "[" + teamname + "]") + player.getName() + ": " + event.getMessage());
                }
            }
        }
    }
}
