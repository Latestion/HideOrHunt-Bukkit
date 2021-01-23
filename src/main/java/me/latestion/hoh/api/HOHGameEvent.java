package me.latestion.hoh.api;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class HOHGameEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private GameState state;
    private Location loc;
    private int teamsize;

    public HOHGameEvent(GameState state, Location loc, int teamsize) {
        this.state = state;
        this.loc = loc;
        this.teamsize = teamsize;
    }

    public GameState getState() { return state; }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Location getLoc() { return loc; }

    public void setLoc(Location setLoc) {
        if (state != GameState.OFF) {
            HideOrHunt.getInstance().game.loc = setLoc;
        }
    }

    public int getTeamsize() { return teamsize; }

    public void setTeamsize(int newSize) {
        if (state == GameState.PREPARE) {
            HideOrHunt.getInstance().game.teamSize = newSize;
        }
    }

}
