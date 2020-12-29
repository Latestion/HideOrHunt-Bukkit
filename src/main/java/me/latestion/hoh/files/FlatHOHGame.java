package me.latestion.hoh.files;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.game.HOHTeam;
import me.latestion.hoh.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author barpec12 on 28.12.2020
 */
public class FlatHOHGame {

	private final HOHGame game;

	public FlatHOHGame(HOHGame game) {
		this.game = game;
	}

	public static HOHGame deserialize(File file, HideOrHunt plugin) {
		if(!file.exists())
			return null;
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
		int teamSize = yc.getInt("teamSize");
		Location loc = Util.deserializeLocation(yc.getString("location"));

		boolean freeze = yc.getBoolean("freeze");
		boolean grace = yc.getBoolean("grace");
		int episode = yc.getInt("episode");
		GameState gameState = GameState.valueOf(yc.getString("gameState"));
		HOHGame game = new HOHGame(plugin, loc, teamSize);

		List<HOHPlayer> players = FlatHOHPlayer.deserialize(game,
				new File(plugin.getDataFolder(), "players.yml"));
		game.setHohPlayers(players.stream().collect(Collectors.toMap(p -> p.getUUID(), p -> p)));

		List<HOHTeam> teams = FlatHOHTeam.deserialize(game, new File(plugin.getDataFolder(), "teams.yml"));
		game.setTeams(teams.stream().collect(Collectors.toMap(t -> t.getID(), t -> t)));
		Bukkit.getLogger().info("Loaded teams: " + game.getTeams().size());
		for (HOHTeam team : game.getTeams().values()) {
			team.players.forEach(p -> p.setTeam(team));
			Bukkit.getLogger().info("Players in team: "+team.players.size());
		}

		game.freeze = freeze;
		game.grace = grace;
		game.ep = episode;
		game.gameState = gameState;
		game.setSpawnLocation(loc);

		return game;
	}

	public static void save(HOHGame game, HideOrHunt plugin, File file) {
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
		yc.set("teamSize", game.teamSize);

		FlatHOHPlayer.save(game.hohPlayers.values(), new File(plugin.getDataFolder(), "players.yml"));
		FlatHOHTeam.save(game.getTeams().values(), new File(plugin.getDataFolder(), "teams.yml"));

		yc.set("location", Util.serializeLocation(game.loc));
		yc.set("freeze", game.freeze);
		yc.set("grace", game.grace);
		yc.set("episode", game.ep);
		yc.set("gameState", game.gameState.toString());
		try {
			yc.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
