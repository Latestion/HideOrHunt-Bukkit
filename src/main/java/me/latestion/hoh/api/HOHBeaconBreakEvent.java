package me.latestion.hoh.api;

import me.latestion.hoh.game.HOHTeam;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public final class HOHBeaconBreakEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private String message;
    private boolean cancelled;
    private final Block beacon;
    private final HOHTeam team;
    private final UUID id;

    public HOHBeaconBreakEvent(Block beacon, HOHTeam team, UUID id) {
        this.beacon = beacon;
        this.team = team;
        this.id = id;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Block getBeacon() {
        return beacon;
    }

    public HOHTeam getTeam() {
        return team;
    }

    public UUID getPlayerID() {
        return id;
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
}
