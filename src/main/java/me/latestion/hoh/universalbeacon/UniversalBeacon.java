package me.latestion.hoh.universalbeacon;

import me.latestion.hoh.game.HOHTeam;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniversalBeacon {

    private UniversalBeaconInventory inv;

    public Map<HOHTeam, List<Inventory>> unlocked = new HashMap<>();
    public Map<HOHTeam, Integer> bruh;

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
