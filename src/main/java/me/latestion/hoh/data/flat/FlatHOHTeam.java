package me.latestion.hoh.data.flat;

import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.utils.Util;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author barpec12 on 28.12.2020
 */
public class FlatHOHTeam {

	private final HOHTeam team;

	public FlatHOHTeam(HOHTeam team) {
		this.team = team;
	}

	public static List<HOHTeam> deserialize(HOHGame game, File file) {
		List<HOHTeam> teams = new ArrayList<>();
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
		List<ConfigurationSection> sections = yc.getValues(false)
				.keySet().stream().map(s -> yc.getConfigurationSection(s)).collect(Collectors.toList());
		for(ConfigurationSection sc : sections){
			String name = sc.getString("name");
			int id = sc.getInt("id");
			List<String> players = sc.getStringList("players");
			List<String> allivePlayers = sc.getStringList("allive-players");
			boolean eliminated = sc.getBoolean("eliminated");
			Location beaconLoc = Util.deserializeLocation(sc.getString("beacon-loc"));

			HOHTeam team = new HOHTeam(id);
			team.setName(name);

			team.setPlayers(game.getHohPlayers().values().stream()
					.filter(p -> players.contains(p.getUUID().toString())).collect(Collectors.toList()));

			team.setAlivePlayers(game.getHohPlayers().values().stream()
					.filter(p -> allivePlayers.contains(p.getUUID().toString())).collect(Collectors.toList()));

			team.setEliminated(eliminated);
			team.setBeacon(beaconLoc.getBlock());
			teams.add(team);
		}
		return teams;
	}

	public static void save(Collection<HOHTeam> teams, File file) {
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
		for(HOHTeam team : teams) {
			ConfigurationSection sc = yc.createSection(Integer.toString(team.getID()));
			sc.set("name", team.getName());
			sc.set("id", team.getID());
			sc.set("players", team.players.stream().map(p -> p.getUUID().toString())
					.collect(Collectors.toList()));
			sc.set("allive-players", team.alivePlayers.stream().map(p -> p.getUUID().toString())
					.collect(Collectors.toList()));
			sc.set("eliminated", team.eliminated);
			sc.set("beacon-loc", Util.serializeLocation(team.getBeacon().getLocation()));
		}
		try {
			yc.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
