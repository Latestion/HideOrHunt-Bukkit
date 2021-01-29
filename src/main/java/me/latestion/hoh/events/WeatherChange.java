package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChange implements Listener {

    @EventHandler
    public void change(WeatherChangeEvent event) {
        HideOrHunt plugin = HideOrHunt.getInstance();
        if (plugin.getConfig().getBoolean("Cancel-Weather-Change")) if (plugin.game.getGameState() == GameState.ON) if (plugin.game.getWorld().equals(event.getWorld()))
            event.setCancelled(true);
    }

}
