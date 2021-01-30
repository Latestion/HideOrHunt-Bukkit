package me.latestion.hoh.universalbeacon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UniversalBeaconInventory {

    private Inventory inv;

    public UniversalBeaconInventory() {
        setInv();
    }

    public Inventory getInv() {
        return inv;
    }

    private void setInv() {
        inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.AQUA + "Universal Beacon");
        inv.setItem(2, getItemStack(Material.FURNACE, format("#2d2d2d", "Furnace")));
        inv.setItem(3, getItemStack(Material.ANVIL, format("#CACCCE", "Anvil")));
        inv.setItem(4, getItemStack(Material.CRAFTING_TABLE, format("#964B00", "Crafting Table")));
        inv.setItem(5, getItemStack(Material.ENCHANTING_TABLE, format("#b9f2ff", "Enchanting Table")));
        inv.setItem(6, getItemStack(Material.CHEST, format("#c95a49", "Chest")));
    }


    private ItemStack getItemStack(Material mat, String displayName) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    private String format(String hex, String s) {
        return net.md_5.bungee.api.ChatColor.of(hex) + s;
    }

}
