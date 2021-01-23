package me.latestion.hoh.events;

import me.latestion.hoh.game.HOHGame;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;

public class PlayerJoin implements Listener {

	private HideOrHunt plugin;

	public PlayerJoin(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void pje(final PlayerJoinEvent event) {
		Player p = event.getPlayer();
		HOHGame game = plugin.getGame();
		if (plugin.support != null) plugin.support.pm.isHub();
		if (game.gameState == GameState.ON) {
			plugin.sbUtil.addPlayer(p);
			if (!plugin.game.bar.getBar().getPlayers().contains(p))
				plugin.game.bar.addPlayer(p);
			if(plugin.game.getHohPlayer(p.getUniqueId()) == null){
				p.setGameMode(GameMode.SPECTATOR);
			}
			return;
		}else if(game.getGameState().equals(GameState.PREPARE)){
			p.setGameMode(GameMode.SPECTATOR);
		}else{
			p.teleport(p.getWorld().getSpawnLocation());
		}
	}
}
