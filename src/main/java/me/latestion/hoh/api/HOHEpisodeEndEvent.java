package me.latestion.hoh.api;

import com.sun.istack.internal.NotNull;
import me.latestion.hoh.game.HOHTeam;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class HOHEpisodeEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private int ep;

    public HOHEpisodeEndEvent(int ep) {
       this.ep = ep;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getEp() { return ep; }

}
