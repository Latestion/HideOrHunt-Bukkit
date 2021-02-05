package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHPlayer;
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
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        HOHGame game = plugin.getGame();
        switch (game.getGameState()) {
            case ON:
                HOHPlayer hp = game.getHohPlayers().get(p.getUniqueId());
                if (hp != null) {
                    if (hp.banned) {
                        String msg = plugin.getMessageManager().getMessage("player-eliminated").replace("%player%", p.getDisplayName());
                        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, msg);
                    }
                    return;
                }
                if (!e.getPlayer().hasPermission("hoh.spectate")) {
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getMessageManager().getMessage("no-spectate-permissions"));
                    return;
                }
                break;
            case PREPARE:
                if (!e.getPlayer().hasPermission("hoh.spectate")) {
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getMessageManager().getMessage("no-spectate-permissions"));
                    return;
                }
                break;
            default:
        }

    }

}
