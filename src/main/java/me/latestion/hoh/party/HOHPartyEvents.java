package me.latestion.hoh.party;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class HOHPartyEvents implements Listener {

    private HOHParty party;

    public HOHPartyEvents(HOHParty party) {
        this.party = party;
    }

    @EventHandler
    public void pQuit(PlayerQuitEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        if (party.inParty(player)) {
            HOHPartyHandler hand = party.partyPlayer.get(player).party;
            hand.removePlayer(player);
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (!party.partyPlayer.containsKey(event.getPlayer().getUniqueId())) {
            party.partyPlayer.put(event.getPlayer().getUniqueId(), new HOHPartyPlayer(event.getPlayer().getUniqueId()));
        }
    }
}
