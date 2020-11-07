package me.Latestion.HOH.Events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.HOH.Main;
import me.Latestion.HOH.Game.GameState;
import me.Latestion.HOH.Game.HOHTeam;
import me.Latestion.HOH.Utils.Util;

public class InventoryClick implements Listener {
    private Main plugin;
    
    public InventoryClick(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void click(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (event.getCurrentItem() == null) {
            return;
        }
        if (!event.getCurrentItem().hasItemMeta()) {
            return;
        }
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }
        if (event.getView().getTitle().contains("Teams")) {
        	if (GameState.getCurrentGamestate() != GameState.PREPARE) {
        		return;
        	}
        	event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory().equals(plugin.inv) && event.getCurrentItem().getItemMeta().hasLore()) {
                List<String> lore = event.getCurrentItem().getItemMeta().getLore();
                if (lore.size() == 1) {
                    lore.add(player.getName());
                    ItemStack item = event.getCurrentItem();
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    event.getClickedInventory().setItem(event.getSlot(), item);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.updateInventory();
                    }
                    this.plugin.chat.add(player);
                    plugin.cache.add(event.getSlot());
                    player.sendMessage(ChatColor.GOLD + "Enter the name for your team!");
                    plugin.game.cache.remove(player);
                    player.closeInventory();
                    return;
                }
                else if (lore.size() - 1 < plugin.game.size) {
                	
                	Util util = new Util(plugin);
                	if (util.isPlayerNaming(Bukkit.getPlayerExact(ChatColor.stripColor(lore.get(1))))) {
                		player.sendMessage(ChatColor.RED + "Player is still naming the team!");
                		return;
                	}
                    lore.add(player.getName());
                    event.getCurrentItem().getItemMeta().setLore(lore);
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        p2.updateInventory();
                    }
                    
                    String name = event.getCurrentItem().getItemMeta().getDisplayName();
                    HOHTeam team = plugin.hohTeam.get(name);
                    team.addPlayer(plugin.hohPlayer.get(player.getUniqueId()));
                    plugin.hohPlayer.get(player.getUniqueId()).setTeam(team);
                    
                    plugin.game.cache.remove(plugin.game.cache.indexOf(player));
                    player.closeInventory();
                    if (plugin.game.cache.isEmpty() && plugin.chat.isEmpty() && plugin.cache.isEmpty()) {
                        plugin.game.startGame();
                    }
                    return;
                }
                else {
                    player.sendMessage(ChatColor.RED + "The Team is full!");
                    return;
                }
            }
        }
    }
}
