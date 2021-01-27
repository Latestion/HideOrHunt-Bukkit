package me.latestion.hoh.illegalbase;

import me.latestion.hoh.game.HOHTeam;
import org.bukkit.block.BlockFace;

public class Base {

    private HOHTeam team;
    public boolean isLegal = true;

    public BaseHandler baseHandler;

    public Base(HOHTeam team) {
        this.team = team;
        this.baseHandler = new BaseHandler(this);
    }

    public void check() {
        baseHandler.isLegal(team.getBeacon(), false, null);
    }
}
