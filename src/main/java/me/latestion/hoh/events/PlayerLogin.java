package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * @author barpec12 on 30.12.2020
 */
public class PlayerLogin implements Listener {
	private HideOrHunt plugin;

	public PlayerLogin(HideOrHunt plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e){
		Player p = e.getPlayer();
		if(!e.getPlayer().hasPermission("hoh.spectate")){
			e.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getMessageManager().getMessage("no-spectate-permissions"));
		}
	}

}
