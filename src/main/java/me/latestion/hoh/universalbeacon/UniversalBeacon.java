package me.latestion.hoh.universalbeacon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class UniversalBeacon {

    private UniversalBeaconInventory inv;

    public Map<UUID, List<Material>> unlocked = new HashMap<>();

    public UniversalBeacon() {
        inv = new UniversalBeaconInventory();
    }

    public Inventory getInv() {
        return inv.getInv();
    }
}
