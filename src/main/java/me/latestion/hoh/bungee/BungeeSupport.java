package me.latestion.hoh.bungee;

import me.latestion.hoh.HideOrHunt;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BungeeSupport {

    private HideOrHunt plugin;
    public PlugMessage pm;
    public boolean isHub = false;

    public List<UUID> inQueue = new ArrayList<>();

    public BungeeSupport(HideOrHunt plugin) {
        this.plugin = plugin;
        this.pm = new PlugMessage(plugin);
    }

    public void queuePlayer(Player player) {
        inQueue.add(player.getUniqueId());
        pm.sendPlayerToServer();
    }
}
