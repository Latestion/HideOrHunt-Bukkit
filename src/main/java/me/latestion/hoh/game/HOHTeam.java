package me.latestion.hoh.game;

import me.latestion.hoh.HideOrHunt;
import org.bukkit.block.Block;
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
}
