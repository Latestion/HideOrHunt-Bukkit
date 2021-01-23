package me.latestion.hoh.bungee;

import me.latestion.hoh.HideOrHunt;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.*;

public class BungeeSupport {

    private HideOrHunt plugin;
    public PlugMessage pm;
    public boolean isHub = false;

    public List<UUID> inQueue = new ArrayList<>();
    public Map<String, ServerState> state = new HashMap<>();

    public BungeeSupport(HideOrHunt plugin) {
        this.plugin = plugin;
        this.pm = new PlugMessage(plugin);
        loadServers();
    }

    public void queuePlayer(Player player) {
        inQueue.add(player.getUniqueId());
        for (ServerState ss : state.values()) {
            if (!ss.game) {
                if (ss.totalOnlinePlayers < ss.maxPlayers) {
                    pm.sendPlayerToServer(ss, player);
                    break;
                }
            }
        }
        player.sendMessage(ChatColor.RED + "No Servers Available, Try again later!");
    }

    private void loadServers() {
        for (String s : plugin.getConfig().getStringList("HOH-Servers")) {
            String[] split = s.split(", ");
            state.put(split[0], (new ServerState(split[0], parseInt(split[1]), parseInt(split[2]))));
        }
    }

    private int parseInt(String s) {
        return Integer.parseInt(s);
    }
}
