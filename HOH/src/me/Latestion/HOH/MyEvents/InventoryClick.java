package me.Latestion.HOH.MyEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.HOH.Main;

public class InventoryClick implements Listener
{
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
        if (event.getView().getTitle().contains("Teams") || event.getView().getTitle().contains("Game History") || event.getView().getTitle().contains("About Game")) {
        	event.setCancelled(true);
            Player player = (Player)event.getWhoClicked();
            if (event.getInventory().equals(this.plugin.inv) && event.getCurrentItem().getItemMeta().hasLore()) {
                List<String> lore = new ArrayList<String>();
                lore = (List<String>)event.getCurrentItem().getItemMeta().getLore();
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
                    this.plugin.cache.add(event.getSlot());
                    player.sendMessage(ChatColor.GOLD + "Enter the name for your team!");
                    this.plugin.cache2.remove(this.plugin.cache2.indexOf(player));
                    player.closeInventory();
                    return;
                }
                else if (lore.size() - 1 < this.plugin.gmType) {
                	
                	if (plugin.voids.isPlayerNaming(Bukkit.getPlayerExact(ChatColor.stripColor(lore.get(1))))) {
                		player.sendMessage(ChatColor.RED + "Player is still naming the team!");
                		return;
                	}
                	
                    lore.add(player.getName());
                    event.getCurrentItem().getItemMeta().setLore(lore);
                    for (final Player p2 : Bukkit.getOnlinePlayers()) {
                        p2.updateInventory();
                    }
                    String name = event.getCurrentItem().getItemMeta().getDisplayName();
                    List<String> newPlayer = new ArrayList<String>();
                    newPlayer.addAll(this.plugin.data.getConfig().getStringList(String.valueOf(this.plugin.time) + "." + name));
                    newPlayer.add(player.getUniqueId().toString());
                    this.plugin.data.getConfig().set(String.valueOf(this.plugin.time) + "." + name, (Object)newPlayer);
                    this.plugin.cache2.remove(this.plugin.cache2.indexOf(player));
                    player.closeInventory();
                    if (this.plugin.cache2.isEmpty() && plugin.chat.isEmpty() && plugin.cache.isEmpty()) {
                        this.plugin.voids.doIt(this.plugin.started);
                    }
                    return;
                }
                else {
                    player.sendMessage(ChatColor.RED + "The Team is full!");
                    return;
                }
            }
            if (event.getInventory().equals(plugin.stats)) {
            	event.setCancelled(true);
            	String time = event.getCurrentItem().getItemMeta().getDisplayName();
            	Inventory noo = Bukkit.createInventory(null, 54, "About Game");
            	int[] i = {0};
            	plugin.data.getConfig().getConfigurationSection(time).getKeys(false).forEach(key -> {
            		ItemStack item = plugin.voids.beacon(key);
            		ItemMeta meta = item.getItemMeta();
            		List<String> names = new ArrayList<>();
            		for (String s : plugin.data.getConfig().getStringList(time + "." + key)) {
            			names.add(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName());
            		}
            		meta.setLore(names);
            		item.setItemMeta(meta);
            		noo.setItem(i[0], item);
            		i[0]++;
            	});
            	player.openInventory(noo);
            	return;
            }
            if (event.getView().getTitle().contains("About Game")) {
            	event.setCancelled(true);
            }  
        }
    }
}
