package me.latestion.hoh.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HOHEpisodeEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private int ep;

    public HOHEpisodeEndEvent(int ep) {
        this.ep = ep;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public int getEp() {
        return ep;
    }

}
