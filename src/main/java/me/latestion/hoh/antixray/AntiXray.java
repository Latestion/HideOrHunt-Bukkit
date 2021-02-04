package me.latestion.hoh.antixray;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Predicate;

public class AntiXray extends BukkitRunnable {

    private HideOrHunt plugin;
    private HOHGame game;

    private int range;
    private int tick;

    private Block air;

    public AntiXray(HideOrHunt plugin) {
        this.plugin = plugin;
        this.game = plugin.getGame();
        this.range = plugin.getConfig().getInt("Show-Beacon-Range");
        this.tick = plugin.getConfig().getInt("Check-Delay");
        if (tick == 0) tick = 1;
        block();
    }

    @Override
    public void run() {
        if (game.getGameState() != GameState.ON) cancel();
        for (HOHTeam team : game.getTeams().values()) {
            if (!team.hasBeacon()) continue;
            Block beacon = team.getBeacon();
            Location beaconLoc = beacon.getLocation();
            World world = beaconLoc.getWorld();
            Collection<Entity> entities = world.getNearbyEntities(beaconLoc, range, range, range);
            List<Player> players = new ArrayList<>();
            entities.forEach(en -> { if (en instanceof Player) players.add((Player) en); });
            Location highestLocation = world.getHighestBlockAt(beaconLoc).getLocation();
            double distance = highestLocation.distance(beaconLoc);
            if (distance <= 5) {
                continue;
            }
            for (Player player : game.getWorld().getPlayers()) {
                if (!players.contains(player)) {
                    player.sendBlockChange(beaconLoc, air.getBlockData());
                    player.sendBlockChange(beaconLoc.add(0, 1, 0), air.getBlockData());
                    continue;
                }
                player.sendBlockChange(beaconLoc, beacon.getBlockData());
                player.sendBlockChange(beaconLoc.add(0, 1, 0), team.getSign().getBlockData());
                team.getSign().getState().update();
            }
        }
    }

    public void start() {
        if (game.getGameState() == GameState.ON) {
            this.range = plugin.getConfig().getInt("Show-Beacon-Range");
            this.tick = plugin.getConfig().getInt("Check-Delay");
            runTaskTimer(plugin, 0, tick * 20);
        }
    }

    public void stop() {
        cancel();
    }

    public void block() {
        air = Bukkit.getWorlds().get(0).getBlockAt(100000, 11, 100000);
        air.setType(Material.STONE);
    }
}
