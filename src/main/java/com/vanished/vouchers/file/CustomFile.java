package com.vanished.vouchers.file;

import com.vanished.vouchers.Vouchers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

public class CustomFile {
    private String fileName;
    private Vouchers plugin = Vouchers.getInstance().getInstance();
    private File customConfigFile;
    private FileConfiguration customConfig;

    public CustomFile(String fileName) {
        this.fileName = fileName;
    }

    //Reload the file
    public void reloadCustomConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), fileName + ".yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = new InputStreamReader(plugin.getResource(fileName + ".yml"));
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        customConfig.setDefaults(defConfig);
    }

    //Get the file's configuration, can be used to get strings, ints, lists etc...
    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadCustomConfig();
        }
        return customConfig;
    }

    //Save the file
    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    //Save the file defaults
    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), fileName + ".yml");
        }
        if (!customConfigFile.exists()) {
            plugin.saveResource(fileName + ".yml", false);
        }
    }

    //Get the file name minus the extension
    String getFileName() {
        return fileName;
    }
}