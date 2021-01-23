package me.latestion.hoh.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.latestion.hoh.HideOrHunt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;

public class PlugMessage implements PluginMessageListener {

    private HideOrHunt plugin;

    public PlugMessage(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subChannel = in.readUTF();

            if (subChannel.equals("GetServer")) {
                String servername = in.readUTF();
                if (plugin.getConfig().getString("Main-Lobby").equals(servername)) {
                    plugin.support.isHub = true;
                }
            }

            if (subChannel.equals("PlayerCount")) {
                String server = in.readUTF();
                int i = in.readInt();
                if (i == 0) {
                    if (plugin.support.inQueue.size() == 2) {
                        Player hunter = Bukkit.getPlayer(plugin.support.inQueue.get(0));
                        plugin.support.inQueue.remove(0);
                        Player runner = Bukkit.getPlayer(plugin.support.inQueue.get(0));
                        plugin.support.inQueue.remove(0);
                        hunter.sendMessage(ChatColor.RED + "[ManHunt]" + ChatColor.GOLD + "Found your opponent!");
                        runner.sendMessage(ChatColor.RED + "[ManHunt]" + ChatColor.GOLD + "Found your opponent!");
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                connect(hunter, server);
                            }
                        }, 3 * 20);
                    }
                }
            }
        }
    }

    public void connect(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void isHub() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServer");
        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void sendPlayerToServer() {
        List<String> servers = plugin.getConfig().getStringList("HOH-Servers");
        for (String s : servers) {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("PlayerCount");
            output.writeUTF(s);
            Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
        }
    }

}
