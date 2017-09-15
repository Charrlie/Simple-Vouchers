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

import java.util.Arrays;
import java.util.Collections;

public class CmdVoucher implements CommandExecutor {
    private Vouchers plugin = Vouchers.getInstance();
    private FileManager fileManager = plugin.fileManager;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("vouchers")) {
            return false;
        }

        if (args.length == 0) {
            if (!sender.hasPermission("vouchers.help")) {
                Lang.ERR_NO_PERMISSION.sendMessage(sender, Collections.singletonList("%permission%;vouchers.help"), true);
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
                Lang.ERR_NO_PERMISSION.sendMessage(sender, Collections.singletonList("%permission%;vouchers.reload"), true);
                return false;
            }
            long startingTime = System.currentTimeMillis();

            plugin.reloadConfig();
            fileManager.getFile("vouchers").reloadCustomConfig();
            fileManager.getFile("lang").reloadCustomConfig();
            Lang.setFile(fileManager.getFile("lang").getCustomConfig());
            Lang.CONFIG_RELOADED.sendMessage(sender, Collections.singletonList("%time%;" + Long.toString(System.currentTimeMillis() - startingTime)), true);
            return true;
        }

        if (subCommand.equalsIgnoreCase("list")) {
            if (!sender.hasPermission("vouchers.list")) {
                Lang.ERR_NO_PERMISSION.sendMessage(sender, Collections.singletonList("%permission%;vouchers.list"), true);
                return false;
            }
            StringBuilder stringBuilder = new StringBuilder();

            for (String voucherName : fileManager.getFile("vouchers").getCustomConfig().getConfigurationSection("Vouchers").getKeys(false)) {
                stringBuilder.append(voucherName).append(", ");
            }

            String voucherList = stringBuilder.toString().trim();
            voucherList = voucherList.substring(0, voucherList.length() - 1);

            Lang.VOUCHERS.sendMessage(sender, Collections.singletonList("%vouchers%;" + voucherList), true);
            return true;
        }

        if (subCommand.equalsIgnoreCase("give")) {
            if (!sender.hasPermission("vouchers.give")) {
                Lang.ERR_NO_PERMISSION.sendMessage(sender, Collections.singletonList("%permission%;vouchers.give"), true);
                return false;
            }

            if (args.length == 1) {
                Lang.ERR_SPECIFY_PLAYER.sendMessage(sender, null, true);
                return false;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                Lang.ERR_OFFLINE.sendMessage(sender, Collections.singletonList("%player%;" + args[1]), true);
                return false;
            }

            if (args.length == 2) {
                Lang.ERR_SPECIFY_VOUCHER.sendMessage(sender, null, true);
                return false;
            }

            for (String voucher : fileManager.getFile("vouchers").getCustomConfig().getConfigurationSection("Vouchers").getKeys(false)) {
                if (!voucher.equalsIgnoreCase(args[2])) {
                    continue;
                }

                if (!fileManager.getFile("vouchers").getCustomConfig().contains("Vouchers." + voucher + ".Item")) {
                    Lang.ERR_MISSING_ITEM.sendMessage(sender, Collections.singletonList("%voucher%;" + voucher), true);
                    return false;
                }

                ItemStack voucherItem = ItemUtil.deserializeItem(fileManager.getFile("vouchers").getCustomConfig().getString("Vouchers." + voucher + ".Item"));

                if (args.length == 4) {
                    try {
                        voucherItem.setAmount(Integer.parseInt(args[3]));
                    } catch (IllegalArgumentException e) {
                        Lang.ERR_BAD_AMOUNT.sendMessage(sender, null, true);
                    }
                }

                if (target.getInventory().firstEmpty() == -1) {
                    target.getWorld().dropItem(target.getLocation(), voucherItem);
                } else {
                    target.getInventory().addItem(voucherItem);
                }
                Lang.VOUCHER_GIVEN.sendMessage(sender, Arrays.asList("%player%;" + target.getName(), "%amount%;" + Integer.toString(voucherItem.getAmount()), "%voucher%;" + voucher), true);
                return true;
            }

            Lang.ERR_UNKNOWN_VOUCHER.sendMessage(sender, null, true);
            return false;
        }

        Bukkit.dispatchCommand(sender, "vouchers");
        return false;
    }
}
