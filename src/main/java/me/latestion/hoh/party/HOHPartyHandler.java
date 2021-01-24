package me.latestion.hoh.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HOHPartyHandler {

    private HOHParty p;
    private List<UUID> party = new ArrayList<>();
    private List<UUID> invited = new ArrayList<>();

    public HOHPartyHandler(HOHParty p, UUID id) {
        this.p = p;
        addPlayer(id);
    }

    public HOHPartyHandler(HOHParty p, UUID id, UUID tm) {
        this.p = p;
        addPlayer(id);
        addPlayer(tm);
    }


    public List<UUID> getParty() {
        return party;
    }

    public UUID getLeader() {
        return party.get(0);
    }

    public void invitePlayer(UUID player) {
        invited.add(player);
        //// TODO: Send Message And Make Command
    }

    public void removePlayer(UUID id) {
        party.remove(id);
        if (p.partyPlayer.containsKey(id)) {
            p.partyPlayer.get(id).inParty = false;
            return;
        }
        else {
            p.partyPlayer.put(id, new HOHPartyPlayer(id, false));
        }
    }

    public void addPlayer(UUID id) {
        party.add(id);
        if (p.partyPlayer.containsKey(id)) {
            p.partyPlayer.get(id).inParty = true;
            return;
        }
        else {
            p.partyPlayer.put(id, new HOHPartyPlayer(id, true));
        }
    }

}
