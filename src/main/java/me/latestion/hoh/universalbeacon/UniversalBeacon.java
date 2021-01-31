package me.latestion.hoh.universalbeacon;

import com.sun.istack.internal.NotNull;
import me.latestion.hoh.game.HOHTeam;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class UniversalBeacon {

    private UniversalBeaconInventory inv;

    public Map<HOHTeam, List<Inventory>> unlocked = new HashMap<>();

    public UniversalBeacon() {
        inv = new UniversalBeaconInventory();
    }

    @NotNull
    public Inventory getInv() {
        return inv.getInv();
    }

    public void openInv(Player player) {
        player.openInventory(getInv());
    }

    public Inventory getPlayerInv() {

        return null;
    }

}
