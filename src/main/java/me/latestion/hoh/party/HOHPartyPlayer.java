package me.latestion.hoh.party;

import java.util.UUID;

public class HOHPartyPlayer {

    public HOHPartyHandler party;
    public UUID id;

    public HOHPartyPlayer(UUID id, HOHPartyHandler hand) {
        this.id = id;
        this.party = hand;
    }

    public HOHPartyPlayer(UUID id) {
        this.id = id;
    }

    public boolean inParty() {
        return party != null;
    }

}
