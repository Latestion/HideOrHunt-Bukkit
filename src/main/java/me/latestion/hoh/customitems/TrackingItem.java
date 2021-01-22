package me.latestion.hoh.customitems;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.HOHPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author barpec12 on 13.01.2021
 */
public class TrackingItem{

	private int remainingUses = 0;
	private int visibleTime = 5;
	boolean visible = false;
	private HideOrHunt plugin;
	private Player owner;
	private ItemStack item;
	private static final Material hiddenMaterial = Material.CLOCK;
	private static final Material shownMaterial = Material.COMPASS;
	private static HashMap<Player, TrackingItem> trackingItems = new HashMap<>();

	public TrackingItem(HideOrHunt plugin, Player owner){
		this.plugin = plugin;
		this.owner = owner;
		initializeItem();
	}

	public void updateItem(){
		if(visible){
			item.setType(shownMaterial);
		}else{
			item.setType(hiddenMaterial);
		}
		updateLore();
		if(owner == null)
			return;
		Inventory inv = owner.getInventory();
		for(int i = 0; i < inv.getSize(); i++){
			if(isTrackingItem(inv.getItem(i))){
				inv.setItem(i, item);
			}
		}
	}

	private void initializeItem(){
		item = new ItemStack(hiddenMaterial);
		trackingItems.put(owner, this);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Lokalizowanie innych graczy"); //TODO get messages from MessageManager
		item.setItemMeta(meta);
		updateLore();
	}

	public ItemStack getItemStack(){
		return this.item;
	}

	private void updateLore(){
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.YELLOW + "Kliknij prawym, żeby");
		lore.add(ChatColor.YELLOW + "Wskazać najbliższego gracza");
		lore.add(ChatColor.YELLOW + "Pozostałe użycia: "+remainingUses);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public void processRightClick(){
		if(remainingUses>0){
			if(visible)
				return;
			remainingUses--;
			showTracking();
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){

				@Override
				public void run() {
					hideTracking();
				}
			}, visibleTime * 20L);
		}else{
			owner.sendMessage(ChatColor.RED + "Nie masz obecnie kolejnych użyć!");
		}
	}

	public void showTracking(){
		visible = true;
		owner.setCompassTarget(getNearestHohPlayer(400D).getPlayer().getLocation());
		owner.sendMessage(ChatColor.GREEN+ "Wskazywanie lokalizacji gracza przez "+visibleTime +" sekund.");
		updateItem();
	}

	public void hideTracking(){
		this.visible = false;
		owner.setCompassTarget(owner.getLocation());
		updateItem();
	}

	private void setOwner(Player p){
		this.owner = p;
	}

	public void addUses(int uses){
		this.remainingUses += uses;
		updateItem();
	}

	private HOHPlayer getNearestHohPlayer(Double range) {
		double distance = Double.POSITIVE_INFINITY;
		HOHPlayer target = null;
		for (Entity e : owner.getNearbyEntities(range, range, range)) {
			if (!(e instanceof Player) || e == owner)
				continue;
			if(plugin.getGame().getHohPlayer(e.getUniqueId()) == null)
				continue;
			double distanceTo = owner.getLocation().distance(e.getLocation());
			if (distanceTo > distance)
				continue;
			distance = distanceTo;
			target = plugin.getGame().getHohPlayer(e.getUniqueId());
		}
		return target;
	}

	public static boolean isTrackingItem(ItemStack item){
		if(item == null){
			return false;
		}
		if((item.getType().equals(hiddenMaterial) || item.getType().equals(shownMaterial))){
			return true;
		}
		return false;
	}

	public static void addTrackingUses(HideOrHunt plugin, Player p, int uses){
		TrackingItem trackingItem = null;
		for(ItemStack item : p.getInventory().getContents()){
			if(isTrackingItem(item)){
				trackingItem = getTrackingItem(p);
				break;
			}
		}
		if(trackingItem == null){
			trackingItem = new TrackingItem(plugin, p);
			p.getInventory().addItem(trackingItem.getItemStack());
		}
		trackingItem.addUses(uses);
	}

	public static TrackingItem getTrackingItem(Player p){
		return trackingItems.get(p);
	}
}
