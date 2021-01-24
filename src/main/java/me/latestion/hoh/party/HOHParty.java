package me.latestion.hoh.party;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HOHParty {

    public Map<UUID, HOHPartyHandler> parties = new HashMap<>();
    public Map<UUID, HOHPartyPlayer> partyPlayer = new HashMap<>();

    public List<UUID> getParty(UUID id) {
        return parties.get(id).getParty();
    }

    public boolean ownsParty(UUID id) {
        return parties.containsKey(id);
    }

    public boolean isParty(UUID id) {
        return partyPlayer.get(id).inParty;
    }

    public HOHPartyHandler createParty(UUID owner) {
        HOHPartyHandler hand = new HOHPartyHandler(this, owner);
        parties.put(owner, hand);
        return hand;
    }

    public void deleteParty(UUID id) {
        HOHPartyHandler hand = parties.get(id);
        for (UUID p : hand.getParty()) {
            partyPlayer.get(p).inParty = false;
        }
        parties.remove(id);
    }

    public int getPartySize(UUID id) {
        return parties.get(id).getParty().size();
    }

}
