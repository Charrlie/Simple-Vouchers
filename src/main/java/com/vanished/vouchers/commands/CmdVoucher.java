package com.vanished.vouchers.commands;

import com.vanished.vouchers.Vouchers;
import com.vanished.vouchers.file.FileManager;
import com.vanished.vouchers.util.ItemUtil;
import com.vanished.vouchers.util.Lang;
import com.vanished.vouchers.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdVoucher implements CommandExecutor {
    private Vouchers plugin = Vouchers.getInstance();
    private FileManager fileManager = plugin.fileManager;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("vouchers")) {
            return false;
        }

        if (args.length == 0) {
            if (!sender.hasPermission("vouchers.help")) {
                sender.sendMessage(Lang.ERR_NO_PERMISSION.toString().replace("%permission%", "vouchers.help"));
                return false;
            }

            for (String str : fileManager.getFile("lang").getCustomConfig().getStringList("help")) {
                sender.sendMessage(Util.colorize(str));
            }
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("vouchers.reload")) {
                sender.sendMessage(Lang.ERR_NO_PERMISSION.toString().replace("%permission%", "vouchers.reload"));
                return false;
            }
            long startingTime = System.currentTimeMillis();

            plugin.reloadConfig();
            fileManager.getFile("vouchers").reloadCustomConfig();
            fileManager.getFile("lang").reloadCustomConfig();
            sender.sendMessage(Lang.CONFIG_RELOADED.toString().replace("%time%", Long.toString(System.currentTimeMillis() - startingTime)));
            return true;
        }

        if (subCommand.equalsIgnoreCase("list")) {
            if (!sender.hasPermission("vouchers.list")) {
                sender.sendMessage(Lang.ERR_NO_PERMISSION.toString().replace("%permission%", "vouchers.list"));
                return false;
            }
            StringBuilder stringBuilder = new StringBuilder();

            for (String voucherName : fileManager.getFile("vouchers").getCustomConfig().getConfigurationSection("Vouchers").getKeys(false)) {
                stringBuilder.append(voucherName).append(", ");
            }

            String voucherList = stringBuilder.toString().trim();
            voucherList = voucherList.substring(0, voucherList.length() - 1);

            sender.sendMessage(Lang.VOUCHERS.toString().replace("%vouchers%", voucherList));
            return true;
        }

        if (subCommand.equalsIgnoreCase("give")) {
            if (!sender.hasPermission("vouchers.give")) {
                sender.sendMessage(Lang.ERR_NO_PERMISSION.toString().replace("%permission%", "vouchers.give"));
                return false;
            }

            if (args.length == 1) {
                sender.sendMessage(Lang.ERR_SPECIFY_PLAYER.toString());
                return false;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage(Lang.ERR_OFFLINE.toString().replace("%player%", args[1]));
                return false;
            }

            if (args.length == 2) {
                sender.sendMessage(Lang.ERR_SPECIFY_VOUCHER.toString());
                return false;
            }

            for (String voucher : fileManager.getFile("vouchers").getCustomConfig().getConfigurationSection("Vouchers").getKeys(false)) {
                if (!voucher.equalsIgnoreCase(args[2])) {
                    continue;
                }

                if (!fileManager.getFile("vouchers").getCustomConfig().contains("Vouchers." + voucher + ".Item")) {
                    sender.sendMessage(Lang.ERR_MISSING_ITEM.toString().replace("%voucher%", voucher));
                    return false;
                }

                ItemStack voucherItem = ItemUtil.deserializeItem(fileManager.getFile("vouchers").getCustomConfig().getString("Vouchers." + voucher + ".Item"));

                if (args.length == 4) {
                    try {
                        voucherItem.setAmount(Integer.parseInt(args[3]));
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(Lang.ERR_BAD_AMOUNT.toString());
                    }
                }

                if (target.getInventory().firstEmpty() == -1) {
                    target.getWorld().dropItem(target.getLocation(), voucherItem);
                } else {
                    target.getInventory().addItem(voucherItem);
                }
                sender.sendMessage(Lang.VOUCHER_GIVEN.toString().replace("%player%", target.getName()).replace("%amount%", Integer.toString(voucherItem.getAmount())).replace("%voucher%", voucher));
                return true;
            }

            sender.sendMessage(Lang.ERR_UNKNOWN_VOUCHER.toString());
            return false;
        }

        Bukkit.dispatchCommand(sender, "vouchers");
        return false;
    }
}
