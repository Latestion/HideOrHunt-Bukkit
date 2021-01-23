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

    private final HideOrHunt plugin;
    private BungeeSupport support;

    public PlugMessage(HideOrHunt plugin) {
        this.plugin = plugin;
        this.support = plugin.support;
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
                support.thisServer = servername;
                ServerState ss = support.state.get(servername);
                if (ss.maxPlayers == Bukkit.getOnlinePlayers().size()) {
                    if (!support.isHub) {

                    }
                }
            }

            if (subChannel.equals("PlayerCount")) {
                String server = in.readUTF();
                int i = in.readInt();
                ServerState ss = support.state.get(server);
                ss.totalOnlinePlayers = i;
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

    public void sendPlayerToServer(ServerState serverState, Player player) {
        connect(player, serverState.name);
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerCount");
        output.writeUTF(serverState.name);
        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

}
