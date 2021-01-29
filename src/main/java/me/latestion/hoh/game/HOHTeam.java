package me.latestion.hoh.game;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.api.HOHBeaconBreakEvent;
import me.latestion.hoh.illegalbase.Base;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HOHTeam {

    public List<HOHPlayer> players = new ArrayList<>();
    public List<HOHPlayer> alivePlayers = new ArrayList<>();
    public boolean eliminated = false;
    private String name;
    private Integer id;
    private Block beacon;
    private Block sign;
    private Base base;

    public HOHTeam(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(List<HOHPlayer> players) {
        this.players = players;
    }

    public void setAlivePlayers(List<HOHPlayer> alivePlayers) {
        this.alivePlayers = alivePlayers;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public boolean addPlayer(HOHPlayer hohPlayer) {
        if (players.size() == HideOrHunt.getInstance().game.teamSize) {
            return false;
        }
        players.add(hohPlayer);
        alivePlayers.add(hohPlayer);
        return true;
    }

    public void removePlayer(HOHPlayer hohPlayer) {
        players.remove(hohPlayer);
        alivePlayers.remove(hohPlayer);
        hohPlayer.setTeam(null);
        setItemStack(hohPlayer.getGame(), hohPlayer.getPlayer().getName());
        if (players.size() == 0) {
            HOHGame game = HideOrHunt.getInstance().getGame();
            for (int i : game.getTeams().keySet()) {
                if (game.getTeams().get(i).equals(this)) {
                    game.getTeams().remove(i);
                    break;
                }
            }
        }
    }

    public HOHPlayer getLeader() {
        return players.get(0);
    }

    public Block getBeacon() {
        return beacon;
    }

    public void setBeacon(Block blockPlaced) {
        this.beacon = blockPlaced;
    }

    public boolean hasBeacon() {
        return beacon != null;
    }

    public void diedPlayer(HOHPlayer player) {
        alivePlayers.remove(player);
        if (alivePlayers.size() == 0) {
            eliminated = true;
        }
        player.dead = true;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    private void setItemStack(HOHGame game, String name) {
        ItemStack item = game.inv.getItem(id);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.remove(name);
        item.setItemMeta(meta);
        game.inv.setItem(id, item);
    }

    public Block getSign() {
        return sign;
    }

    public void setSign(Block setSign) {
        this.sign = setSign;
    }

    public void setBase(Base b) {
        this.base = b;
    }

    public Base getBase() {
        if (base == null) base = new Base(this);
        return base;
    }

    public void doAsthetic(Player player) {
        MessageManager messageManager = HideOrHunt.getInstance().getMessageManager();
        beacon.getWorld().playSound(beacon.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        String eliminatedMsg = messageManager.getMessage("eliminated-broadcast").replace("%eliminated-team%", getName());
        if (player != null) eliminatedMsg = eliminatedMsg.replace("%eliminating-team%", HideOrHunt.getInstance().game.hohPlayers.get(player.getUniqueId()).getTeam().getName());
        else eliminatedMsg = eliminatedMsg.replace("%eliminating-team%", "Illegal Base");
        Bukkit.broadcastMessage(eliminatedMsg);
        for (HOHPlayer p : players) {
            p.getPlayer().sendTitle(messageManager.getMessage("beacon-destroyed-title"), "", 10, 40, 10);
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
        }
    }
}
