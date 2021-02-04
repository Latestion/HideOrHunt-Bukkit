package me.latestion.hoh.party;

import me.latestion.hoh.HideOrHunt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HOHParty {

    public Map<UUID, HOHPartyHandler> parties = new HashMap<>();
    public Map<UUID, HOHPartyPlayer> partyPlayer = new HashMap<>();

    public boolean ownsParty(UUID id) {
        return parties.containsKey(id);
    }

    public HOHPartyHandler createParty(UUID owner, UUID invited) {
        if (parties.containsKey(owner)) {
            return null;
        }
        HOHPartyHandler hand = new HOHPartyHandler(this, owner);
        parties.put(owner, hand);
        hand.invitePlayer(invited);
        return hand;
    }

    public void deleteParty(UUID id) {
        HOHPartyHandler hand = parties.get(id);
        for (UUID p : hand.getParty()) {
            partyPlayer.get(p).party = null;
            HideOrHunt.getInstance().support.removeQueuePlayer(p);
        }
        parties.remove(id);
    }

    public int getPartySize(UUID id) {
        return parties.get(id).getParty().size();
    }

    public HOHPartyHandler getParty(UUID id) {
        return parties.get(id);
    }

    public void joinParty(UUID player, UUID p) {
        if (parties.containsKey(p)) {
            HOHPartyHandler hand = parties.get(p);
            if (hand.isInvited(player)) {
                hand.addPlayer(player);
            }
            else {
                // Not Invited OR Invite Expired
            }
        }
        else {
            // No such team
        }
    }

    public boolean inParty(UUID id) {
        return partyPlayer.get(id).inParty();
    }

}
