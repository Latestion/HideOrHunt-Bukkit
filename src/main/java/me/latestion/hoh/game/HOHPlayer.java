package me.latestion.hoh.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class HOHPlayer {

    private final HOHGame game;
    private final UUID uuid;
    public boolean banned = false; // Banned Player
    public boolean dead = false;   // Dead Player (Eliminated Player)
    public boolean teamChat = false; // Is Using Team Chat
    private HOHTeam team = null; // Player Team
    private boolean namingTeam = false;

    public HOHPlayer(HOHGame game, UUID uuid) {
        this.game = game;
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public HOHTeam getTeam() {
        return team;
    }

    public void setTeam(HOHTeam t) {
        this.team = t;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean hasTeam() {
        return this.team != null;
    }

    public boolean isNamingTeam() {
        return this.namingTeam;
    }

    public void setNamingTeam(boolean namingTeam) {
        this.namingTeam = namingTeam;
    }

    public void prepareTeam(Inventory inv) {
        if (game.gameState == GameState.PREPARE) {
            getPlayer().openInventory(inv);
        }
    }

    public HOHGame getGame() {
        return game;
    }

}
