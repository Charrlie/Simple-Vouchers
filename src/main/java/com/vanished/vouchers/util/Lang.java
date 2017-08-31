package com.vanished.vouchers.util;

import com.vanished.vouchers.Vouchers;
import org.bukkit.configuration.file.FileConfiguration;

public enum Lang {
    ERR_BAD_AMOUNT("err-bad-amount", "The amount specified must be a number!"),
    ERR_BAD_SOUND("err-bad-sound", "The sound specified for the voucher &e%voucher% &7doesn't exist!"),
    ERR_MISSING_ITEM("err-missing-item", "The voucher &e%voucher% &7doesn't have an item set to it!"),
    ERR_NO_PERMISSION("err-no-permission", "You need the permission &e%permission% &7to use this command!"),
    ERR_OFFLINE("err-offline", "&e%player% &7is offline!"),
    ERR_SPECIFY_PLAYER("err-specify-player", "You must specify a player!"),
    ERR_SPECIFY_VOUCHER("err-specify-voucher", "You must specify a voucher!"),
    ERR_UNKNOWN_VOUCHER("err-unknown-voucher", "Unknown voucher &e%voucher%&7!"),

    CONFIG_RELOADED("config-reloaded", "Configuration files reloaded (Took &e%time%ms&7)!"),
    VOUCHER_GIVEN("voucher-given", "You've given &e%player% %amount%x %voucher% &7vouchers."),
    VOUCHERS("vouchers", "%vouchers%"),

    PREFIX("prefix", "&6&lVouchers &7&l> &7");

    private final String path;
    private final String def;
    private static FileConfiguration LANG;

    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }

    public static void setFile(FileConfiguration fileConfiguration) {
        LANG = fileConfiguration;
    }

    @Override
    public String toString() {
        Vouchers plugin = Vouchers.getInstance();

        StringBuilder stringBuilder = new StringBuilder();

        if (plugin.getConfig().getBoolean("Use-Prefix")) {
            stringBuilder.append(Lang.LANG.getString("prefix", PREFIX.def));
        }

        stringBuilder.append(LANG.getString(path, def));

        return Util.colorize(stringBuilder.toString());
    }

    public String toString(boolean addPrefix) {
        Vouchers plugin = Vouchers.getInstance();

        StringBuilder stringBuilder = new StringBuilder();

        if (addPrefix && plugin.getConfig().getBoolean("Use-Prefix")) {
            stringBuilder.append(Lang.LANG.getString("prefix", PREFIX.def));
        }

        stringBuilder.append(LANG.getString(path, def));

        return Util.colorize(stringBuilder.toString());
    }
}