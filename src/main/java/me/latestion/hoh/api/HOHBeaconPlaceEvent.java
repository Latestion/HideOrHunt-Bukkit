package me.latestion.hoh.api;

import me.latestion.hoh.game.HOHTeam;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class HOHBeaconPlaceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private String message;
    private boolean cancelled;
    private final Block beacon;
    private final HOHTeam team;
    private final UUID id;
    private final boolean success;

    public HOHBeaconPlaceEvent(Block beacon, HOHTeam team, UUID id, boolean success) {
        this.beacon = beacon;
        this.team = team;
        this.id = id;
        this.success = success;
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

    public boolean isSuccessful() {
        return success;
    }

}
