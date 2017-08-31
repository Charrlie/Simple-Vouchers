package com.vanished.vouchers;

import com.vanished.vouchers.commands.CmdVoucher;
import com.vanished.vouchers.file.CustomFile;
import com.vanished.vouchers.file.FileManager;
import com.vanished.vouchers.listeners.PlayerActivity;
import com.vanished.vouchers.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Vouchers extends JavaPlugin {
    private static Vouchers instance;
    public FileManager fileManager = new FileManager();

    @Override
    public void onEnable() {
        instance = this;

        registerConfig();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Vouchers getInstance() {
        return instance;
    }

    private void registerConfig() {
        CustomFile langFile = new CustomFile("lang");
        CustomFile voucherFile = new CustomFile("vouchers");

        saveDefaultConfig();
        langFile.saveDefaultConfig();
        voucherFile.saveDefaultConfig();

        fileManager.registerFile(langFile);
        fileManager.registerFile(voucherFile);

        Lang.setFile(langFile.getCustomConfig());
    }

    private void registerCommands() {
        getCommand("voucher").setExecutor(new CmdVoucher());
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerActivity(), this);
    }
}