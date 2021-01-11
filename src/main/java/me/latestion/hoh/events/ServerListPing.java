package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * @author barpec12 on 06.01.2021
 */
public class ServerListPing implements Listener {
	private HideOrHunt plugin;

	public ServerListPing(HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void serverListPing(ServerListPingEvent e) {
		GameState state = plugin.getGame().getGameState();
		switch (state){
			case OFF:
				e.setMotd("WAITING");
			case ON:
			case PREPARE:
				e.setMotd("INGAME_ALLJOIN");
		}
	}
}
