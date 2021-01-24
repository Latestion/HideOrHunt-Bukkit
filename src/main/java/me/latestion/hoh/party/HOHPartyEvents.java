package me.latestion.hoh.party;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class HOHPartyEvents implements Listener {

    @EventHandler
    public void pQuit(PlayerQuitEvent event) {
        /*
            Remove from Party
            Or IF OWNER
            Disband Party
         */
    }

}
