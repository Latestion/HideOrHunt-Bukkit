package me.latestion.hoh.illegalbase;

import me.latestion.hoh.game.HOHTeam;
import org.bukkit.block.BlockFace;

public class Base {

    private HOHTeam team;
    public boolean isLegal = true;

    public Base(HOHTeam team) {
        this.team = team;

    }

    public void check() {
        try {
            BaseHandler.isLegal(team.getBeacon(), false, this, null);
        }
        catch (Exception e) {
            // Do Nothing
        }
    }
}
