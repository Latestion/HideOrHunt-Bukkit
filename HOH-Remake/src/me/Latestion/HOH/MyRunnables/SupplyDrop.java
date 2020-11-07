package me.Latestion.HOH.MyRunnables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Latestion.HOH.HideOrHunt;

public class SupplyDrop extends BukkitRunnable {

	private HideOrHunt plugin;
	
	public SupplyDrop(HideOrHunt plugin) {
		this.plugin = plugin;
		runTaskTimer(plugin, plugin.getConfig().getInt("Supply-Drop-Delay") * 60 * 20L
				, this.plugin.getConfig().getInt("Supply-Drop-Delay") * 60 * 20L);
	}

	@Override
	public void run() {
        Location borderCenter = plugin.game.loc.getWorld().getWorldBorder().getCenter();
        int borderRadius = plugin.getConfig().getInt("WorldBorder") / 2;
        int x = getRandomNumberInRange(borderCenter.getBlockX() - borderRadius, borderCenter.getBlockX() + borderRadius);
        int z = getRandomNumberInRange(borderCenter.getBlockZ() - borderRadius, borderCenter.getBlockZ() + borderRadius);
        Location higestBlock = borderCenter.getWorld().getHighestBlockAt(x, z).getLocation().clone().add(0.0, 60.0, 0.0);
        createDrop(higestBlock);
	}
 
	 public void createDrop(Location loc) {
        World workWorld = plugin.game.loc.getWorld();
        workWorld.getBlockAt(loc.clone()).setType(Material.LIGHT_BLUE_WOOL);
        workWorld.getBlockAt(loc.clone().add(0.0, 1.0, 0.0)).setType(Material.CHEST);
        workWorld.getBlockAt(loc.clone().add(0.0, 4.0, 0.0)).setType(Material.CAMPFIRE);
        workWorld.getBlockAt(loc.clone().add(0.0, 5.0, 0.0)).setType(Material.WHITE_WOOL);
        workWorld.getBlockAt(loc.clone().add(1.0, 4.0, 0.0)).setType(Material.LIGHT_BLUE_WOOL);
        workWorld.getBlockAt(loc.clone().add(0.0, 4.0, 1.0)).setType(Material.LIGHT_BLUE_WOOL);
        workWorld.getBlockAt(loc.clone().add(-1.0, 4.0, 0.0)).setType(Material.LIGHT_BLUE_WOOL);
        workWorld.getBlockAt(loc.clone().add(0.0, 4.0, -1.0)).setType(Material.LIGHT_BLUE_WOOL);
        workWorld.getBlockAt(loc.clone().add(1.0, 1.0, 0.0)).setType(Material.OAK_PLANKS);
        workWorld.getBlockAt(loc.clone().add(-1.0, 1.0, 0.0)).setType(Material.OAK_PLANKS);
        workWorld.getBlockAt(loc.clone().add(0.0, 1.0, 1.0)).setType(Material.OAK_PLANKS);
        workWorld.getBlockAt(loc.clone().add(0.0, 1.0, -1.0)).setType(Material.OAK_PLANKS);
        for (int i = 1; i < 4; ++i) {
            workWorld.getBlockAt(loc.clone().add(1.0, i, 1.0)).setType(Material.OAK_FENCE);
            workWorld.getBlockAt(loc.clone().add(1.0, i, -1.0)).setType(Material.OAK_FENCE);
            workWorld.getBlockAt(loc.clone().add(-1.0, i, 1.0)).setType(Material.OAK_FENCE);
            workWorld.getBlockAt(loc.clone().add(-1.0, i, -1.0)).setType(Material.OAK_FENCE);
        }
        workWorld.getBlockAt(loc.clone().add(1.0, 4.0, 1.0)).setType(Material.WHITE_WOOL);
        workWorld.getBlockAt(loc.clone().add(1.0, 4.0, -1.0)).setType(Material.WHITE_WOOL);
        workWorld.getBlockAt(loc.clone().add(-1.0, 4.0, 1.0)).setType(Material.WHITE_WOOL);
        workWorld.getBlockAt(loc.clone().add(-1.0, 4.0, -1.0)).setType(Material.WHITE_WOOL);
        Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "Supply drop at X:" + loc.getBlockX() + " Z:" + loc.getBlockZ());
        Chest chest = (Chest )workWorld.getBlockAt(loc.clone().add(0.0, 1.0, 0.0)).getState();
        List<String> getRandomAll = new ArrayList<String>();
        this.plugin.getConfig().getConfigurationSection("Supply-Drop-Items").getKeys(false).forEach(key -> getRandomAll.add(key));
        Random rand = new Random();
        int lol = rand.nextInt(getRandomAll.size());
        if (lol == 0) {
        	lol++;
        }
        for (String addMaterialToChest : plugin.getConfig().getStringList("Supply-Drop-Items." + lol)) {
            final String[] ahh = addMaterialToChest.split(", ");
            Integer amt = Integer.parseInt(ahh[1]);
            if (ahh[0].equalsIgnoreCase("air")) {
                for (int x = 0; x < amt; x++) {
                	amt++;
                }
                continue;
            }
            ItemStack item = new ItemStack(Material.matchMaterial(ahh[0].toUpperCase()), amt);
            chest.getInventory().addItem(item);
	    }
	}
	 
    public int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }
}
