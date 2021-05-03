package me.latestion.hoh.universalbeacon;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHTeam;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.EnchantingTable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UniversalBeaconHandler implements Listener {

    private UniversalBeacon ub;

    public UniversalBeaconHandler(UniversalBeacon universalBeacon) {
        this.ub = universalBeacon;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory) return;
        if (!event.getClickedInventory().equals(ub.getInv())) return;
        event.setCancelled(true);
        if (HideOrHunt.getInstance().game.getGameState() != GameState.ON) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        HOHTeam team = HideOrHunt.getInstance().getGame().getHohPlayer(event.getWhoClicked().getUniqueId()).getTeam();
        if (!ub.unlocked.containsKey(team)) return;
        ItemStack item = event.getCurrentItem();
        Material type = item.getType();
        Player p = (Player) event.getWhoClicked();
        List<Inventory> invs = ub.unlocked.get(team);
        if (type == Material.ENCHANTING_TABLE) {
            invs.removeIf(inv -> (!(inv.getType().equals(InventoryType.ENCHANTING))));
            if (invs.size() == 0) return;
            p.openInventory(invs.get(0));
            EnchantingInventory inv = (EnchantingInventory) invs.get(0);
            return;
        }
        if (type == Material.ANVIL) {
            p.openInventory(Bukkit.createInventory(null, InventoryType.ANVIL));
            return;
        }
        if (type == Material.CRAFTING_TABLE) {
            p.openWorkbench(null, true);
            return;
        }
        if (type == Material.CHEST) {
            invs.removeIf(inv -> (!(inv.getType().equals(InventoryType.CHEST))));
            if (invs.size() == 0) return;
            return;
        }
        if (type == Material.FURNACE) {
            invs.removeIf(inv -> (!(inv.getType().equals(InventoryType.FURNACE))));
            if (invs.size() == 0) return;
            return;
        }
    }

    @EventHandler
    public void craftBlock(CraftItemEvent event) {
        if (HideOrHunt.getInstance().game.getGameState() != GameState.ON) return;
        HOHTeam team = HideOrHunt.getInstance().getGame().getHohPlayer(event.getWhoClicked().getUniqueId()).getTeam();
        Material type = event.getRecipe().getResult().getType();
        if (type == Material.FURNACE || type == Material.ENCHANTING_TABLE || type == Material.ANVIL || type == Material.CHEST) {
            List<Inventory> invs = new ArrayList<>();
            if (ub.unlocked.containsKey(team)) invs.addAll(ub.unlocked.get(team));
            String s = type.toString(); if (type == Material.ENCHANTING_TABLE) s = "ENCHANTING";
            invs.add(Bukkit.createInventory(null, InventoryType.valueOf(type.toString())));
            ub.unlocked.put(team, invs);

        }
    }
}
