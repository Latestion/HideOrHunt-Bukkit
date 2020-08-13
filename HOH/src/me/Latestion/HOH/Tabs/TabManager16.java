package me.Latestion.HOH.Tabs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.Latestion.HOH.Main;
import net.minecraft.server.v1_16_R1.ChatComponentText;
import net.minecraft.server.v1_16_R1.PacketPlayOutPlayerListHeaderFooter;

public class TabManager16 {
	
	public List<ChatComponentText> headers = new ArrayList<>();
	public List<ChatComponentText> footers = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private Main plugin;
	public TabManager16(Main plugin) {
		this.plugin = plugin;
	}
	
	// WILL BE EZ AF
	
	public void showTab() {
		if (headers.isEmpty() && footers.isEmpty()) {
			return;
		}
		
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		int count1 = 0;
		int count2 = 0;
		
		try {
			
			Field a = packet.getClass().getDeclaredField("header");
			a.setAccessible(true);
			Field b = packet.getClass().getDeclaredField("footer");
			b.setAccessible(true);
			
			if (count1 >= headers.size()) {
				count1 = 0;
			}
			
			if (count2 >= footers.size()) {
				count2 = 0;
			}
			
			a.set(packet, headers.get(count1));
			b.set(packet, footers.get(count2));
			
			if (Bukkit.getOnlinePlayers().size() != 0) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
				}
			}
			
			count1++;
			count2++;
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addHeader(String header) {
		headers.add(new ChatComponentText(format(header)));
	}
	
	public void addFooter(String footer) {
		footers.add(new ChatComponentText(format(footer)));
	}
	
	private String format(String messages) {
		return ChatColor.translateAlternateColorCodes('&', messages);
	}
	
	
}
