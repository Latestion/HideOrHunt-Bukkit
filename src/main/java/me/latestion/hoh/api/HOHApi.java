package me.latestion.hoh.api;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HOHApi {

    public static HOHPlayer getHOHPlayer(Player player) {
        return HideOrHunt.getInstance().game.getHohPlayer(player.getUniqueId());
    }

    public static HOHPlayer getHOHPlayer(UUID id) {
        return HideOrHunt.getInstance().game.getHohPlayer(id);
    }

    public static HOHGame getGame() {
        return HideOrHunt.getInstance().getGame();
    }

}
