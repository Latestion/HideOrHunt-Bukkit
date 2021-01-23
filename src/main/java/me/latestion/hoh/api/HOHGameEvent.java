package me.latestion.hoh.api;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class HOHGameEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final GameState state;
    private final Location loc;
    private final int teamSize;
    private final HideOrHunt plugin;
    private boolean cancelled;

    public HOHGameEvent(GameState state, Location loc, int teamSize) {
        this.state = state;
        this.loc = loc;
        this.teamSize = teamSize;
        this.plugin = HideOrHunt.getInstance();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GameState getState() {
        return state;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location setLoc) {
        if (state != GameState.OFF) {
            plugin.game.loc = setLoc;
        }
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int newSize) {
        if (state == GameState.PREPARE) {
            plugin.game.teamSize = newSize;
        }
    }

}
