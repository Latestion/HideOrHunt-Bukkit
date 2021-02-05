package me.latestion.hoh.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class HOHPlayer {

    private final HOHGame game;
    private final UUID uuid;
    private final String name;
    public boolean banned = false; // Banned Player
    public boolean dead = false;   // Dead Player (Eliminated Player)
    public boolean teamChat = false; // Is Using Team Chat
    private HOHTeam team = null; // Player Team
    private boolean namingTeam = false;

    public HOHPlayer(HOHGame game, UUID uuid, String name) {
        this.game = game;
        this.uuid = uuid;
        this.name = name;
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

    public String getName(){
        return this.name;
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
        if (game.getGameState() == GameState.PREPARE) {
            getPlayer().openInventory(inv);
        }
    }

    public HOHGame getGame() {
        return game;
    }

}
