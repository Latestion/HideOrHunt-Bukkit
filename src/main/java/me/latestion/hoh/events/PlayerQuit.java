package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author barpec12 on 06.01.2021
 */
public class PlayerQuit implements Listener {
	private HideOrHunt plugin;

	public PlayerQuit(HideOrHunt plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		HOHGame game = plugin.getGame();
		Player p = e.getPlayer();
		if(game.getGameState().equals(GameState.ON) && !game.freeze){
			if(game.getHohPlayer(p.getUniqueId()) != null){
				p.damage(p.getHealth() * 4); //*4 just in case
			}
		}
	}

}