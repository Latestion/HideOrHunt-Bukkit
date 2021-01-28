package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final HideOrHunt plugin;

    public PlayerJoin(HideOrHunt plugin) {
        this.plugin = plugin;
    }

	@EventHandler
	public void pje(final PlayerJoinEvent event) {
		Player p = event.getPlayer();
		HOHGame game = plugin.getGame();
		if (plugin.support != null) {
			if (!plugin.support.isHub) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.support.pm.isHub(), 20);
			}
		}
		if (game.gameState == GameState.ON) {
			plugin.sbUtil.addPlayer(p);
			if (!plugin.game.bar.getBar().getPlayers().contains(p))
				plugin.game.bar.addPlayer(p);
			if(plugin.game.getHohPlayer(p.getUniqueId()) == null){
				p.setGameMode(GameMode.SPECTATOR);
			}
		}
		else if(game.getGameState() == GameState.PREPARE){
			p.setGameMode(GameMode.SPECTATOR);
		}
		else{
			if (plugin.getConfig().getBoolean("Teleport-To-Spawn")) p.teleport(p.getWorld().getSpawnLocation());
		}
	}
}
