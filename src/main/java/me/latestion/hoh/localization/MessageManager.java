package me.latestion.hoh.localization;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author barpec12 on 27.12.2020
 */
public class MessageManager {

	String languageFilePath;
	File dataFolder;
	Map<String, String> messages;

	public MessageManager(File dataFolder, String language) {
		this.dataFolder = dataFolder;
		this.languageFilePath = "locales/" + language + ".yml";
		loadMessages();
	}

	private void loadMessages() {
		File file = new File(dataFolder, languageFilePath);
		if (!file.exists()) {
			Bukkit.getLogger().severe("File " + languageFilePath + " does not exists!");
			messages = new HashMap<>();
			return;
		}
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		messages = yaml.getConfigurationSection("messages").getValues(false).entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue()));

	}

	public String getMessage(String key) {
		if (messages.containsKey(key)) {
			return ChatColor.translateAlternateColorCodes('&', messages.get(key));
		} else {
			Bukkit.getLogger().severe("Message " + key + " not found!");
			return "";
		}
	}
}
