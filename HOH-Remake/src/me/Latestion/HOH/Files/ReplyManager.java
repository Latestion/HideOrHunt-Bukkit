package me.Latestion.HOH.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Latestion.HOH.Main;

public class ReplyManager {
	private Main plugin;
    private FileConfiguration dataConfig;
    private File configFile;
    
    public ReplyManager(final Main plugin) {
        this.dataConfig = null;
        this.configFile = null;
        this.plugin = plugin;
        this.saveDefaultConfig();
    }
    
    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "custom.yml");
        }
        this.dataConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configFile);
        final InputStream defaultStream = this.plugin.getResource("custom.yml");
        if (defaultStream != null) {
            final YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults((Configuration)defaultConfig);
        }
    }
    
    public FileConfiguration getConfig() {
        if (this.dataConfig == null) {
            this.reloadConfig();
        }
        return this.dataConfig;
    }
    
    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        }
        catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Could Not Save Config to" + this.configFile, e);
        }
    }
    
    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "custom.yml");
        }
        if (!this.configFile.exists()) {
            this.plugin.saveResource("custom.yml", false);
        }
    }
}
