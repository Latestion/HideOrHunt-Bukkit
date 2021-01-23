package me.latestion.hoh.data.flat;

import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author barpec12 on 28.12.2020
 */
public class FlatHOHPlayer {

    private final HOHPlayer player;

    public FlatHOHPlayer(HOHPlayer player) {
        this.player = player;
    }

    public static List<HOHPlayer> deserialize(HOHGame game, File file) {
        List<HOHPlayer> players = new ArrayList<>();
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        List<ConfigurationSection> sections = yc.getValues(false)
                .keySet().stream().map(s -> yc.getConfigurationSection(s)).collect(Collectors.toList());

        for (ConfigurationSection se : sections) {
            UUID uuid = UUID.fromString(se.getString("uuid"));
            boolean banned = se.getBoolean("banned");
            boolean dead = se.getBoolean("dead");
            boolean teamChat = se.getBoolean("teamChat");
            boolean namingTeam = se.getBoolean("namingTeam");

            HOHPlayer player = new HOHPlayer(game, uuid);
            player.banned = banned;
            player.dead = dead;
            player.teamChat = teamChat;
            player.setNamingTeam(namingTeam);

            players.add(player);
        }
        return players;
    }

    public static void save(Collection<HOHPlayer> players, File file) {
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        for (HOHPlayer player : players) {
            ConfigurationSection section = yc.createSection(player.getUUID().toString());
            section.set("uuid", player.getUUID().toString());
            section.set("banned", player.banned);
            section.set("dead", player.dead);
            section.set("teamChat", player.teamChat);
            section.set("namingTeam", player.isNamingTeam());
        }
        try {
            yc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
