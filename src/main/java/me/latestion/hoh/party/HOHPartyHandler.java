package me.latestion.hoh.party;

import me.latestion.hoh.HideOrHunt;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HOHPartyHandler {

    private HOHParty p;
    private List<UUID> party = new ArrayList<>();
    private List<UUID> invited = new ArrayList<>();

    public HOHPartyHandler(HOHParty p, UUID id) {
        this.p = p;
        addPlayer(id);
    }

    public HOHPartyHandler(HOHParty p, UUID id, UUID tm) {
        this.p = p;
        addPlayer(id);
        addPlayer(tm);
    }

    public List<UUID> getParty() {
        return party;
    }

    public UUID getLeader() {
        return party.get(0);
    }

    public void invitePlayer(UUID player) {
        invited.add(player);
        //// TODO: Send Message And Make Command And Delayed Task
        runDelayedInviteTask(player);
    }

    public boolean isInvited(UUID uuid) {
        return invited.contains(uuid);
    }

    public void removePlayer(UUID id) {
        if (id.equals(getLeader())) {
            HideOrHunt.getInstance().party.deleteParty(id);
            return;
        }
        party.remove(id);
        if (p.partyPlayer.containsKey(id)) p.partyPlayer.get(id).party = null;
        else p.partyPlayer.put(id, new HOHPartyPlayer(id, null));
        if (party.size() == 1) { HideOrHunt.getInstance().party.deleteParty(getLeader()); }
    }

    public void addPlayer(UUID id) {
        if (HideOrHunt.getInstance().party.inParty(id)) {
            // Already In Team.
            return;
        }
        party.add(id);
        if (p.partyPlayer.containsKey(id)) {
            p.partyPlayer.get(id).party = this;
        }
        else {
            p.partyPlayer.put(id, new HOHPartyPlayer(id, this));
        }
        if (invited.contains(id)) {
            invited.remove(id);
        }
    }

    private void runDelayedInviteTask(UUID player) {
        BukkitScheduler schedule =  Bukkit.getServer().getScheduler();
        int disband  = HideOrHunt.getInstance().getConfig().getInt("Disband-Party-After");
        if (disband > 0) {
            schedule.scheduleSyncDelayedTask(HideOrHunt.getInstance(), () -> {
                if (party.size() == 1) HideOrHunt.getInstance().party.deleteParty(getLeader());
            }, disband * 20L);
        }

        int expire  = HideOrHunt.getInstance().getConfig().getInt("Party-Invite-Expire");
        if (expire <= 0) { return; }
        schedule.scheduleSyncDelayedTask(HideOrHunt.getInstance(), () -> {
            if (invited.contains(player)) {
                invited.remove(player); // Invite Expired
            }
        }, expire * 20L);
    }
}
