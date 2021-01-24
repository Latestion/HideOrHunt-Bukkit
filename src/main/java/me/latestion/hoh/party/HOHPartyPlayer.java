package me.latestion.hoh.party;

import java.util.UUID;

public class HOHPartyPlayer {

    public boolean inParty = false;
    public UUID id;

    public HOHPartyPlayer(UUID id, boolean bol) {
        this.id = id;
        this.inParty = bol;
    }

}
