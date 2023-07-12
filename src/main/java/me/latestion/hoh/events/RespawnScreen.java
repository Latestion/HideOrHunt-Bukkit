package me.latestion.hoh.events;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.localization.MessageManager;
import me.latestion.hoh.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RespawnScreen implements Listener {

    private HideOrHunt plugin;
    private Util util;

    public RespawnScreen(HideOrHunt plugin) {
        this.plugin = plugin;
        this.util = new Util(plugin);
    }

    @EventHandler
    public void damageEntityByEntity(EntityDamageByEntityEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            damageHandler(event, event.getDamager());
    }

    @EventHandler
    public void damageEntity(EntityDamageEvent event){
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            damageHandler(event, null);
    }

    public void damageHandler(EntityDamageEvent event, Entity damager) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (plugin.game.getGameState() != GameState.ON)
            return;
        Player p = (Player) event.getEntity();
        double health = p.getHealth();
        if (!(event.getFinalDamage() >= health))
            return;
        HOHPlayer player = plugin.game.hohPlayers.get(p.getUniqueId());
        MessageManager messageManager = plugin.getMessageManager();

        event.setCancelled(true);
        p.setHealth(20);

        if (!player.getTeam().hasBeacon()) {
            plugin.getGame().eliminatePlayer(player);
        }
        else {
            if (this.plugin.getConfig().getInt("Inventory-Keep") != 0) {
                Random rand = new Random();
                int i = rand.nextInt(100);
                if (i + 1 <= this.plugin.getConfig().getInt("Inventory-Keep")) {
                    player.getPlayer().sendMessage(messageManager.getMessage("kept-inventory"));
                } else {
                    Location loc = p.getLocation();
                    for (ItemStack item : p.getInventory()) {
                        if (item == null) continue;
                        loc.getWorld().dropItemNaturally(loc, item);
                    }
                    p.getInventory().clear();
                    p.updateInventory();
                    player.getPlayer().sendMessage(messageManager.getMessage("lost-inventory"));
                }
            }

            p.teleport(plugin.game.hohPlayers.get(p.getUniqueId()).getTeam().getBeacon().getLocation().clone().add(0, 1, 0));

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    for (String s : plugin.getConfig().getStringList("Item-On-Respawn")) {
                        String[] split = s.split(", ");
                        ItemStack item = new ItemStack(Material.matchMaterial(split[0].toUpperCase()), Integer.parseInt(split[1]));
                        p.getInventory().addItem(item);
                    }
                }
            }, 1);

            p.updateInventory();
        }

    }
}
