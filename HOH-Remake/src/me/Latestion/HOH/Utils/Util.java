package me.Latestion.HOH.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.HOH.HideOrHunt;
import me.Latestion.HOH.Game.HOHTeam;

public class Util {

	private HideOrHunt plugin;
	
	public Util(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	public int getInt(String  s) {
		try {
			int i = Integer.parseInt(s);
			return i;
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public boolean getAllowOp() {
		return plugin.getConfig().getBoolean("Allow-Op");
	}
	
	public int getWorldBorder() {
		return plugin.getConfig().getInt("WorldBorder");
	}
	
    public boolean isPlayerNaming(Player player) {
    	return plugin.chat.contains(player);
    }
  
    public boolean isTeamTaken(final String teamName) {
        final List<String> allTeams = new ArrayList<String>();
        for (HOHTeam team : plugin.game.getTeams()) {
        	allTeams.add(team.getName().toLowerCase());
        }
        return allTeams.contains(teamName.toLowerCase());
    }
    
    public void createInv(int i) {
        plugin.inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Teams");
        for (int i2 = 0; i2 < i; ++i2) {
            final ItemStack item = new ItemStack(Material.BEACON);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Team: " + i2);
            final List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.RED + "Players:");
            meta.setLore(lore);
            item.setItemMeta(meta);
            plugin.inv.setItem(i2, item);
        }
    }
    
    public ItemStack beacon(String name) {
        final ItemStack item = new ItemStack(Material.BEACON);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.valueOf(ChatColor.translateAlternateColorCodes('&', name)) + ChatColor.BLUE + "'s beacon");
        /*
         * Make Better With Lore!
         */
        item.setItemMeta(meta);
        return item;
    }
    
	public void givePlayerKit(Player p, HOHTeam team, String name) {
		if (team.getLeader().getPlayer().equals(p)) {
			p.getInventory().addItem(beacon(name));
		}
		for (String items : this.plugin.getConfig().getStringList("Kits")) {
            String[] item = items.split(", ");
            if (!item[0].equalsIgnoreCase("BEACON")) {
                ItemStack itemstack = new ItemStack(Material.matchMaterial(item[0]), Integer.parseInt(item[1]));
                p.getInventory().addItem(itemstack);
            }
        }
        for (String potion : this.plugin.getConfig().getStringList("Kits-Potion")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable)new Runnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + p.getName() + " " + potion);
                }
            }, 0L);
        }
	}

	public HOHTeam getTeamFromBlock(Block block) {
		for (HOHTeam team : plugin.game.getTeams()) {
			if (team.getBeacon().equals(block)) return team;
		}
		return null;
	}
}
