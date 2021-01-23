package me.latestion.hoh.utils;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Util {

    private HideOrHunt plugin;

    public Util(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    public static String serializeLocation(Location loc) {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(loc.getWorld().getName()).add(Double.toString(loc.getX()))
                .add(Double.toString(loc.getY())).add(Double.toString(loc.getZ()));
        return joiner.toString();
    }

    public static Location deserializeLocation(String locString) {
        String[] splitted = locString.split(",");
        World w = Bukkit.getWorld(splitted[0]);
        double x = Double.valueOf(splitted[1]);
        double y = Double.valueOf(splitted[2]);
        double z = Double.valueOf(splitted[3]);
        return new Location(w, x, y, z);
    }

    public int getInt(String s) {
        try {
            int i = Integer.parseInt(s);
            return i;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean getAllowOp() {
        return plugin.getConfig().getBoolean("Allow-Op");
    }

    public int getWorldBorder() {
        return plugin.getConfig().getInt("WorldBorder");
    }

    public boolean isTeamNameTaken(final String teamName) {
        return plugin.game.getTeams().values().stream().anyMatch(t -> (t.getName() != null
                && t.getName().toLowerCase().equals(teamName.toLowerCase())));
    }

    public Inventory createInv(int i) {
        MessageManager messageManager = plugin.getMessageManager();
        Inventory inv = Bukkit.createInventory(null, 54, messageManager.getMessage("team-inventory-title"));
        for (int i2 = 0; i2 < i; ++i2) {
            final ItemStack item = new ItemStack(Material.BEACON);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(messageManager.getMessage("team-inventory-beacon-name").replace("%team%", Integer.toString(i2)));
            final List<String> lore = new ArrayList<>();
            lore.add(messageManager.getMessage("team-inventory-beacon-lore"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(i2, item);
        }
        return inv;
    }

    public ItemStack beacon(String name) {
        final ItemStack item = new ItemStack(Material.BEACON);
        final ItemMeta meta = item.getItemMeta();
        name = ChatColor.translateAlternateColorCodes('&', name);
        meta.setDisplayName(plugin.getMessageManager().getMessage("beacon-name").replace("%name%", name));
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable) new Runnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + p.getName() + " " + potion);
                }
            }, 0L);
        }
    }

    public HOHTeam getTeamFromBlock(Block block) {
        return plugin.game.getTeams().values().stream().filter(t -> t.hasBeacon()
                && t.getBeacon().equals(block)).findAny().orElse(null);
    }
}
