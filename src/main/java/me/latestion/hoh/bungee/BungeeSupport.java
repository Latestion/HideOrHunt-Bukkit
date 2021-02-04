package me.latestion.hoh.bungee;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.events.EntityExplode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import scala.concurrent.impl.FutureConvertersImpl;

import java.io.File;
import java.util.*;

public class BungeeSupport {

    public PlugMessage pm;
    public boolean isHub = false;

    public Map<String, ServerState> state = new HashMap<>();

    public String thisServer;
    public String hub;

    public Map<UUID, String> rejoinServer = new HashMap<>();

    public BungeeSupport(HideOrHunt plugin) {
        this.pm = new PlugMessage(plugin);
        loadServers();
        this.hub = plugin.getConfig().getString("Main-Lobby");
    }

    public void queueTeam(UUID player, int tSize, int pMax) {
       if (!isHub) return;
       if (isPlayerInQueue(player)) return;;

       List<ServerState> serverStates = getAvailableServers(tSize, pMax);

       for (ServerState ss : serverStates) {
           if (ss.queue.size() < ss.maxPlayers) {
               ss.queue.add(player);
               if (ss.maxPlayers == ss.queue.size()) {
                   String server = ss.name;
                   for (UUID p : ss.queue) {
                       // TODO: Send party information to the plugin
                       pm.connect(Bukkit.getPlayer(p), server);
                       rejoinServer.put(p, server);
                   }
                   return;
               }
           }
       }
    }

    public void queueTeam(List<UUID> partyIDs, int tSize, int pMax) {
        if (!isHub) return;
        if (isPlayerInQueue(partyIDs.get(0))) return;;

        List<ServerState> serverStates = getAvailableServers(tSize, pMax);

        if (tSize == partyIDs.size()) {
            for (ServerState ss : serverStates) {
                if ((ss.queue.size() + partyIDs.size()) <= ss.maxPlayers) {
                    ss.queue.addAll(partyIDs);
                    if (ss.maxPlayers == ss.queue.size()) {
                        String server = ss.name;
                        for (UUID p : ss.queue) {
                            // TODO: Send party information to the plugin
                            pm.sendTeam(partyIDs);
                            pm.connect(Bukkit.getPlayer(p), server);
                            rejoinServer.put(p, server);
                        }
                        return;
                    }
                }
            }
            return;
        }
    }

    public List<ServerState> getAvailableServers(int teamsize, int maxplayers) {
        List<ServerState> out = new ArrayList<>();
        parentLoop:
        for (ServerState ss : state.values()) {
            if (ss.teamsize == teamsize && ss.maxPlayers == maxplayers && !ss.game) {
                int queue = ss.queue.size();
                if (queue == 0) {
                    out.add(out.size(), ss);
                    continue parentLoop;
                }
                if (out.size() == 1) {
                    if (out.get(0).queue.size() <= queue) {
                        out.add(0, ss);
                        continue parentLoop;
                    }
                    else if (out.get(0).queue.size() > queue) {
                        out.add(1, ss);
                        continue parentLoop;
                    }
                }
                else {
                    for (int i = 0; i < out.size(); i++) {
                        int sQueue = out.get(i).queue.size();
                        if (sQueue <= queue) {
                            out.add(i, ss);
                            continue parentLoop;
                        }
                    }
                }
            }
        }
        return out;
    }

    public List<List<UUID>> getAllQueues() {
        List<List<UUID>> out = new ArrayList<>();
        for (ServerState ss : state.values()) {
            out.add(ss.queue);
        }
        return out;
    }

    public void removeQueuePlayer(UUID player) {
        for (ServerState ss : state.values()) {
           if (ss.queue.contains(player)) {
               if (HideOrHunt.getInstance().party.inParty(player)) {
                   if (HideOrHunt.getInstance().party.ownsParty(player)) {
                       /*
                       Remove Full Team
                        */
                       for (UUID id : HideOrHunt.getInstance().party.getParty(player).getParty()) {
                           ss.queue.remove(id);
                           return;
                       }
                   }
                   else {
                       /*
                       Don't allow as he is not leader
                        */
                       return;
                   }
               }
               ss.queue.remove(player);
               return;
           }
        }
    }

    public boolean isPlayerInQueue(UUID player) {
        return getAllQueues().contains(player);
    }

    public boolean rejoin(UUID player) {
        if (rejoinServer.containsKey(player)) {
            pm.connect(Bukkit.getPlayer(player), rejoinServer.get(player));
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

    public ServerState getCurrentServerState() {
        return state.get(thisServer);
    }

    public int parseInt(String s) {
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
