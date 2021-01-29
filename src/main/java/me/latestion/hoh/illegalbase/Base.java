package me.latestion.hoh.illegalbase;

import me.latestion.hoh.game.HOHTeam;
import org.bukkit.block.BlockFace;

public class Base {

    public HOHTeam team;
    public boolean isLegal = false;


    public Base(HOHTeam team) {
        this.team = team;
    }

    public void check() {
        new BaseHandler(this).isLegal(team.getBeacon(), false, null, false);
    }
}
