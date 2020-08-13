// 
// Decompiled by Procyon v0.5.36
// 

package me.Latestion.HOH.MyVoids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import me.Latestion.HOH.Main;

@SuppressWarnings("all")
public class CreateVoids
{
    private Main plugin;
    
    public CreateVoids(final Main plugin) {
        this.plugin = plugin;
    }
    
    public Location getBeaconLocation(final Player player) {
        final String teamName = this.getPlayerTeam(player);
        for (final Location loc : this.plugin.blockLocation.keySet()) {
            if (this.plugin.blockLocation.get(loc).equalsIgnoreCase(teamName)) {
                return loc;
            }
        }
        return null;
    }
   
    public boolean end() {
        List<String> aliveTeams = new ArrayList<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
        	if (!plugin.deadTalk.contains(p)) {
        		if (!aliveTeams.contains(this.getPlayerTeam(p))) {
            		aliveTeams.add(this.getPlayerTeam(p));
        		} // else dont add
        	}
        }
        if (aliveTeams.size() == 1) {
        	return true;
        }
        else {
        	return false;
        }
    }
    
    public void createInv(final int i) {
        this.plugin.inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Teams");
        for (int i2 = 0; i2 < i; ++i2) {
            final ItemStack item = new ItemStack(Material.BEACON);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Team: " + i2);
            final List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.RED + "Players:");
            meta.setLore(lore);
            item.setItemMeta(meta);
            this.plugin.inv.setItem(i2, item);
        }
    }
    
    public boolean isPlayerNaming(Player player) {
    	if (plugin.chat.contains(player)) return true;
    	else return false;
    }
    
    public boolean isTeamTaken(final String teamName) {
        final List<String> allTeams = new ArrayList<String>();
        try {
            this.plugin.data.getConfig().getConfigurationSection(this.plugin.time.toString()).getKeys(false).forEach(key -> allTeams.add(key.toLowerCase()));
        }
        catch (Exception ex) {}
        return allTeams.contains(teamName.toLowerCase());
    }
    
    public List<String> getTeamPlayers(final String s) {
        return (List<String>)this.plugin.data.getConfig().getStringList(String.valueOf(this.plugin.time) + "." + s);
    }
    
    public String getPlayerTeam(final Player p) {
        final String[] send = { "" };
        try {
            
            this.plugin.data.getConfig().getConfigurationSection(this.plugin.time).getKeys(false).forEach(key -> {
                if (this.getTeamPlayers(key).contains(p.getUniqueId().toString())) {
                    send[0] = key;
                }
                return;
            });
        }
        catch (Exception ex) {}
        return send[0];
    }
    
    public List<String> getAllTeam() {
        final List<String> send = new ArrayList<String>();
        this.plugin.data.getConfig().getConfigurationSection(this.plugin.time.toString()).getKeys(false).forEach(key -> send.add(key));
        return send;
    }
    
    public ItemStack beacon(final String name) {
        final ItemStack item = new ItemStack(Material.BEACON);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.valueOf(ChatColor.translateAlternateColorCodes('&', name)) + ChatColor.BLUE + "'s beacon");
        item.setItemMeta(meta);
        return item;
    }
    
    public void prepareTab() {
    	plugin.tab.addHeader(plugin.getConfig().getString("Tab-List-Title"));
    	plugin.tab.addFooter(plugin.getConfig().getString("Tab-List-Footer"));
    	plugin.tab.showTab();
    }
    
    public String biggestName() {
    	List<String> names = new ArrayList<String>();
    	
    	for (Player p : plugin.played) {
    		names.add("[" + plugin.voids.format(plugin.voids.getPlayerTeam(p)) + ChatColor.RESET + "]" + p.getName());
    	}
        String longestString = names.get(0);
        for (String element : names) {
            if (element.length() > longestString.length()) {
                longestString = element;
            }
        }
    	return longestString;
    }
    
    public void tabListName() {
    	int length = biggestName().length();
    	for (Player p : plugin.played) {
    		if (("[" + plugin.voids.format(plugin.voids.getPlayerTeam(p)) + ChatColor.RESET + "]" + p.getName()).equalsIgnoreCase(biggestName())) {
    			int health = (int) p.getHealth();
    			String newString = StringUtils.rightPad("[" + plugin.voids.format(plugin.voids.getPlayerTeam(p)) + ChatColor.RESET + "]" + p.getName(), length + 2)
        				+ ChatColor.YELLOW + "(" + health + ")";
        		p.setPlayerListName(newString);
        		continue;
    		}
    		
    		else if ( length == ("[" + plugin.voids.format(plugin.voids.getPlayerTeam(p)) + ChatColor.RESET + "]" + p.getName()).length()) {
    			int health = (int) p.getHealth();
    			String newString = StringUtils.rightPad("[" + plugin.voids.format(plugin.voids.getPlayerTeam(p)) + ChatColor.RESET + "]" + p.getName(), length + 2)
        				+ ChatColor.YELLOW + "(" + health + ")";
        		p.setPlayerListName(newString);
    			continue;
    		}
    		
    		int health = (int) p.getHealth();
    		String newString = StringUtils.rightPad("[" + plugin.voids.format(plugin.voids.getPlayerTeam(p)) + ChatColor.RESET + "]" + p.getName(), length + 2)
    				+ ChatColor.YELLOW + "(" + health + ")";
    		p.setPlayerListName(newString);
    	}
    }
    
    public void doIt(final Player player) {
        if (this.plugin.gameOn) {
            return;
        }
        if (!player.hasPermission("hoh.start")) {
            return;
        }
        if (!this.plugin.cache2.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Not all player have selected their teams!");
            for (final Player p : this.plugin.cache2) {
                player.sendMessage(p.getName());
            }
            return;
        }
        this.plugin.gameOn = true;
        Bukkit.getServer().broadcastMessage(ChatColor.RED + "Starting Your" + ChatColor.WHITE + " HOH Game! :)");
        int wb = this.plugin.getConfig().getInt("WorldBorder");
        player.getWorld().getWorldBorder().setCenter(player.getLocation());
        player.getWorld().getWorldBorder().setSize((double)wb);
        player.getWorld().setSpawnLocation(player.getLocation());
        this.plugin.worldGame = player.getWorld();
        this.plugin.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score top = this.plugin.obj.getScore("\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b");
        Score bottom = this.plugin.obj.getScore("\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f");
        Score strTeam = this.plugin.obj.getScore(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.RED).append("Teams:").toString());
        top.setScore(this.getAllTeam().size() + 2);
        bottom.setScore(0);
        strTeam.setScore(this.getAllTeam().size() + 1);
        int i = 1;
        for (String name : this.getAllTeam()) {
            Score team = this.plugin.obj.getScore(String.valueOf(ChatColor.translateAlternateColorCodes('&', name)) + ": " + ChatColor.BOLD + ChatColor.GREEN + "\u2713");
            team.setScore(i);
            List<String> teamPlayer = this.plugin.data.getConfig().getStringList(String.valueOf(this.plugin.time) + "." + name);
            Bukkit.getPlayer(UUID.fromString(teamPlayer.get(0))).getInventory().addItem(new ItemStack[] { this.beacon(name) });
            this.plugin.scoreboardHolder.put(name, i);
            Team teamsb = this.plugin.board.registerNewTeam(name);
            teamsb.setPrefix(ChatColor.translateAlternateColorCodes('&', name));
            teamsb.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            for (String pp : teamPlayer) {
                Player p2 = Bukkit.getPlayer(UUID.fromString(pp));
                for (String items : this.plugin.getConfig().getStringList("Kits")) {
                    String[] item = items.split(", ");
                    if (!item[0].equalsIgnoreCase("BEACON")) {
                        ItemStack itemstack = new ItemStack(Material.matchMaterial(item[0]), Integer.parseInt(item[1]));
                        p2.getInventory().addItem(new ItemStack[] { itemstack });
                    }
                }
                for (final String potion : this.plugin.getConfig().getStringList("Kits-Potion")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Main.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "give " + p2.getName() + " " + potion);
                        }
                    }, 0L);
                }
            }
            ++i;
        }
        for (final Player sbp : Bukkit.getServer().getOnlinePlayers()) {
            sbp.setScoreboard(this.plugin.board);
        }
        final int interval = this.plugin.getConfig().getInt("Episode-Time") - 1;
        if (plugin.version && plugin.getConfig().getBoolean("Custom-Tab-List")) {
        	tabListName();
        }
        if (this.plugin.getConfig().getBoolean("Grace-Period")) {
            this.afterGrace(interval);
            this.plugin.graceOn = true;
        }
        if (this.plugin.getConfig().getBoolean("Auto-Episodes")) {
            this.episodes(interval);
        }
        if (this.plugin.getConfig().getBoolean("Auto-Supply-Drops")) {
            this.supplydrop();
        }
    }
    
    public String format(String s) {
    	return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    public void afterGrace(final int time) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.AQUA).append("Grace Period Will End In 1 Minute!").toString());
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.AQUA).append("Grace Period Will End In 10 Seconds!").toString());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.AQUA).append("Grace Period Will End In 5 Seconds!").toString());
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.AQUA).append("Grace Period Ended").toString());
                                        CreateVoids.this.plugin.graceOn = false;
                                    }
                                }, 100L);
                            }
                        }, 100L);
                    }
                }, 1000L);
            }
        }, time * 60 * 20L);
    }
    
    public void episodes(final int i) {
        final BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, (Runnable)new Runnable() {
			@Override
            public void run() {
                Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append("Episode ").append(CreateVoids.this.plugin.ep).append(" has ended!").toString());
                final Main access$0 = CreateVoids.this.plugin;
                ++access$0.ep;
                if (CreateVoids.this.plugin.getConfig().getInt("Episode-End-Break-Time") != 0) {
                    CreateVoids.this.plugin.freeze = true;
                    Bukkit.broadcastMessage(ChatColor.RED + "Game has been freezed!");
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable)new Runnable() {
                        @Override
                        public void run() {
                            CreateVoids.this.plugin.freeze = false;
                            Bukkit.broadcastMessage(ChatColor.RED + "Game has been unfreezed!");
                        }
                    }, (long)(CreateVoids.this.plugin.getConfig().getInt("Episode-End-Break-Time") * 20));
                }
            }
        }, (long)(this.plugin.getConfig().getInt("Episode-Time") * 60 * 20), this.plugin.getConfig().getInt("Episode-Time") * 60 * 20L);
    }
    
    public void supplydrop() {
        final BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                final Location borderCenter = CreateVoids.this.plugin.worldGame.getWorldBorder().getCenter();
                final int borderRadius = CreateVoids.this.plugin.getConfig().getInt("WorldBorder") / 2;
                final int x = CreateVoids.this.getRandomNumberInRange(borderCenter.getBlockX() - borderRadius, borderCenter.getBlockX() + borderRadius);
                final int z = CreateVoids.this.getRandomNumberInRange(borderCenter.getBlockZ() - borderRadius, borderCenter.getBlockZ() + borderRadius);
                final Location higestBlock = borderCenter.getWorld().getHighestBlockAt(x, z).getLocation().clone().add(0.0, 80.0, 0.0);
                CreateVoids.this.createDrop(higestBlock);
            }
        }, this.plugin.getConfig().getInt("Supply-Drop-Delay") * 60 * 20L, this.plugin.getConfig().getInt("Supply-Drop-Delay") * 60 * 20L);
    }
    
    public int getRandomNumberInRange(final int min, final int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        final Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }
    
    public void createDrop(final Location loc) {
        final World workWorld = this.plugin.worldGame;
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
            workWorld.getBlockAt(loc.clone().add(1.0, (double)i, 1.0)).setType(Material.OAK_FENCE);
            workWorld.getBlockAt(loc.clone().add(1.0, (double)i, -1.0)).setType(Material.OAK_FENCE);
            workWorld.getBlockAt(loc.clone().add(-1.0, (double)i, 1.0)).setType(Material.OAK_FENCE);
            workWorld.getBlockAt(loc.clone().add(-1.0, (double)i, -1.0)).setType(Material.OAK_FENCE);
        }
        workWorld.getBlockAt(loc.clone().add(1.0, 4.0, 1.0)).setType(Material.WHITE_WOOL);
        workWorld.getBlockAt(loc.clone().add(1.0, 4.0, -1.0)).setType(Material.WHITE_WOOL);
        workWorld.getBlockAt(loc.clone().add(-1.0, 4.0, 1.0)).setType(Material.WHITE_WOOL);
        workWorld.getBlockAt(loc.clone().add(-1.0, 4.0, -1.0)).setType(Material.WHITE_WOOL);
        Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append("Supply drop at X:").append(loc.getBlockX()).append(" Z:").append(loc.getBlockZ()).toString());
        final Chest chest = (Chest)workWorld.getBlockAt(loc.clone().add(0.0, 1.0, 0.0)).getState();
        final List<String> getRandomAll = new ArrayList<String>();
        this.plugin.getConfig().getConfigurationSection("Supply-Drop-Items").getKeys(false).forEach(key -> getRandomAll.add(key));
        final Random rand = new Random();
        final int lol = rand.nextInt(getRandomAll.size()) + 1;
        for (final String addMaterialToChest : this.plugin.getConfig().getStringList("Supply-Drop-Items." + lol)) {
            final String[] ahh = addMaterialToChest.split(", ");
            if (ahh[0].equalsIgnoreCase("air")) {
                continue;
            }
            final Integer amt = Integer.parseInt(ahh[1]);
            final ItemStack item = new ItemStack(Material.matchMaterial(ahh[0].toUpperCase()), (int)amt);
            chest.getInventory().addItem(new ItemStack[] { item });
        }
    }
    public void createMyInv() {
    	plugin.stats = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Game History");
    	int[] i = {0};
    	plugin.data.getConfig().getConfigurationSection("").getKeys(false).forEach(key -> {
    		ItemStack item = new ItemStack(Material.PAPER);
    		ItemMeta meta = item.getItemMeta();
    		meta.setDisplayName(key);
    		item.setItemMeta(meta);
    		plugin.stats.setItem(i[0], item);
    		i[0]++;
    	});
    }
}
