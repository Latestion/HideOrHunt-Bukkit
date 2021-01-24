package me.latestion.hoh.bungee;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.events.EntityExplode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.*;

public class BungeeSupport {

    public PlugMessage pm;
    public boolean isHub = false;

    public Map<String, ServerState> state = new HashMap<>();
    public Map<Integer, Map<Integer, List<UUID>>> queue = new HashMap<>();
    //        TeamSize,  Max Teams, Players In Queue

    public String thisServer;
    public String hub;

    public Map<UUID, String> rejoinServer = new HashMap<>();

    public BungeeSupport(HideOrHunt plugin) {
        this.pm = new PlugMessage(plugin);
        loadServers();
        this.hub = plugin.getConfig().getString("Main-Lobby");
    }

    public void queuePlayer(Player player, int tSize, int pMax) {
       if (!isHub) return;
       if (isPlayerInQueue(player)) return;;
       if (queue.containsKey(tSize)) {
           List<UUID> players = queue.get(tSize).get(pMax);
           players.add(player.getUniqueId());
           for (ServerState ss : state.values()) {
               if (ss.maxPlayers == players.size() && ss.teamsize == tSize && !ss.game) {
                   String server = ss.name;
                   for (UUID p : players) {
                       pm.connect(Bukkit.getPlayer(p), server);
                       rejoinServer.put(p, server);
                   }
               }
           }
       }
    }

    public void removeQueuePlayer(Player player) {
        for (int i : queue.keySet()) {
            if (queue.get(i).containsValue(player)) {
                queue.get(i).remove(player.getUniqueId());
                return;
            }
        }
    }

    public boolean isPlayerInQueue(Player player) {
        UUID id = player.getUniqueId();
        for (Map<Integer, List<UUID>> maps : queue.values()) {
            if (maps.containsValue(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean rejoin(Player player) {
        if (rejoinServer.containsKey(player.getUniqueId())) {
            pm.connect(player, rejoinServer.get(player.getUniqueId()));
            return true;
        }
        return false;
    }

    private void loadServers() {
        for (String s : HideOrHunt.getInstance().getConfig().getStringList("HOH-Servers")) {
            String[] split = s.split(", ");
            state.put(split[0], (new ServerState(split[0], parseInt(split[1]), parseInt(split[2]))));
        }
    }

    private ServerState getCurrentServerState() {
        return state.get(thisServer);
    }

    private int parseInt(String s) {
        try {
            return  Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public void deleteWorlds(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorlds(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        path.delete();
    }

}
