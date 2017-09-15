package com.vanished.vouchers.listeners;

import com.vanished.vouchers.Vouchers;
import com.vanished.vouchers.file.CustomFile;
import com.vanished.vouchers.file.FileManager;
import com.vanished.vouchers.util.ItemUtil;
import com.vanished.vouchers.util.Lang;
import com.vanished.vouchers.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class PlayerActivity implements Listener {
    private Vouchers plugin = Vouchers.getInstance();
    private FileManager fileManager = plugin.fileManager;
    private CustomFile vouchersFile = fileManager.getFile("vouchers");

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        ItemStack current = player.getItemInHand();

        if (current == null) {
            return;
        }

        for (String voucher : vouchersFile.getCustomConfig().getConfigurationSection("Vouchers").getKeys(false)) {
            if (!vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Item")) {
                continue;
            }

            ItemStack voucherItem = ItemUtil.deserializeItem(vouchersFile.getCustomConfig().getString("Vouchers." + voucher + ".Item"));

            if (!current.isSimilar(voucherItem)) {
                continue;
            }
            e.setCancelled(true);

            if (current.getAmount() == 1) {
                player.setItemInHand(null);
            } else {
                current.setAmount(current.getAmount() - 1);
            }

            if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Message")) {
                String message = vouchersFile.getCustomConfig().getString("Vouchers." + voucher + ".Message");
                message = message.replace("%prefix%", Lang.PREFIX.toString(false));
                message = message.replace("%voucher%", voucher);
                message = message.replace("%username%", player.getName());
                message = message.replace("%displayname%", player.getDisplayName());

                player.sendMessage(Util.colorize(message));
            }

            if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Broadcast")) {
                String broadcast = vouchersFile.getCustomConfig().getString("Vouchers." + voucher + ".Broadcast");
                broadcast = broadcast.replace("%prefix%", Lang.PREFIX.toString(false));
                broadcast = broadcast.replace("%voucher%", voucher);
                broadcast = broadcast.replace("%username%", player.getName());
                broadcast = broadcast.replace("%displayname%", player.getDisplayName());

                Bukkit.broadcastMessage(broadcast);
            }

            if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Sound")) {
                try {
                    player.getWorld().playSound(player.getLocation(), Sound.valueOf(vouchersFile.getCustomConfig().getString("Vouchers." + voucher + ".Sound").toUpperCase()), 1, 1);
                } catch (IllegalArgumentException ex) {
                    Lang.ERR_BAD_SOUND.sendMessage(player, Collections.singletonList("%voucher%;" + voucher), true);
                }
            }

            if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Commands")) {
                for (String commandNode : vouchersFile.getCustomConfig().getConfigurationSection("Vouchers." + voucher + ".Commands").getKeys(false)) {
                    if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Delay")) {
                        final CustomFile vouchersFinal = vouchersFile;

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            if (vouchersFinal.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Command")) {
                                String command = vouchersFinal.getCustomConfig().getString("Vouchers." + voucher + ".Commands." + commandNode + ".Command");
                                command = command.replace("%prefix%", Lang.PREFIX.toString(false));
                                command = command.replace("%voucher%", voucher);
                                command = command.replace("%username%", player.getName());
                                command = command.replace("%displayname%", player.getDisplayName());

                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace(ChatColor.COLOR_CHAR, '&'));
                            }

                            if (vouchersFinal.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Message")) {
                                String message = vouchersFinal.getCustomConfig().getString("Vouchers." + voucher + ".Commands." + commandNode + ".Message");
                                message = message.replace("%prefix%", Lang.PREFIX.toString(false));
                                message = message.replace("%voucher%", voucher);
                                message = message.replace("%username%", player.getName());
                                message = message.replace("%displayname%", player.getDisplayName());

                                player.sendMessage(Util.colorize(message));
                            }

                            if (vouchersFinal.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Broadcast")) {
                                String message = vouchersFinal.getCustomConfig().getString("Vouchers." + voucher + ".Commands." + commandNode + ".Broadcast");
                                message = message.replace("%prefix%", Lang.PREFIX.toString(false));
                                message = message.replace("%voucher%", voucher);
                                message = message.replace("%username%", player.getName());
                                message = message.replace("%displayname%", player.getDisplayName());

                                player.sendMessage(Util.colorize(message));
                            }

                            if (vouchersFinal.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Sound")) {
                                try {
                                    player.getWorld().playSound(player.getLocation(), Sound.valueOf(vouchersFinal.getCustomConfig().getString("Vouchers." + voucher + ".Commands." + commandNode + ".Sound").toUpperCase()), 1, 1);
                                } catch (IllegalArgumentException ex) {
                                    Lang.ERR_BAD_SOUND.sendMessage(player, Collections.singletonList("%voucher%;" + voucher), true);
                                }
                            }
                        }, vouchersFile.getCustomConfig().getInt("Vouchers." + voucher + ".Commands." + commandNode + ".Delay") * 20L);
                        return;
                    }

                    if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Command")) {
                        String command = vouchersFile.getCustomConfig().getString("Vouchers." + voucher + ".Commands." + commandNode + ".Command");
                        command = command.replace("%prefix%", Lang.PREFIX.toString(false));
                        command = command.replace("%voucher%", voucher);
                        command = command.replace("%username%", player.getName());
                        command = command.replace("%displayname%", player.getDisplayName());

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace(ChatColor.COLOR_CHAR, '&'));
                    }

                    if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Message")) {
                        String message = vouchersFile.getCustomConfig().getString("Vouchers." + voucher + ".Commands." + commandNode + ".Message");
                        message = message.replace("%prefix%", Lang.PREFIX.toString(false));
                        message = message.replace("%voucher%", voucher);
                        message = message.replace("%username%", player.getName());
                        message = message.replace("%displayname%", player.getDisplayName());

                        player.sendMessage(Util.colorize(message));
                    }

                    if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Broadcast")) {
                        String message = vouchersFile.getCustomConfig().getString("Vouchers." + voucher + ".Commands." + commandNode + ".Broadcast");
                        message = message.replace("%prefix%", Lang.PREFIX.toString(false));
                        message = message.replace("%voucher%", voucher);
                        message = message.replace("%username%", player.getName());
                        message = message.replace("%displayname%", player.getDisplayName());

                        player.sendMessage(Util.colorize(message));
                    }

                    if (vouchersFile.getCustomConfig().contains("Vouchers." + voucher + ".Commands." + commandNode + ".Sound")) {
                        try {
                            player.getWorld().playSound(player.getLocation(), Sound.valueOf(vouchersFile.getCustomConfig().getString("Vouchers." + voucher + ".Commands." + commandNode + ".Sound").toUpperCase()), 1, 1);
                        } catch (IllegalArgumentException ex) {
                            Lang.ERR_BAD_SOUND.sendMessage(player, Collections.singletonList("%voucher%;" + voucher), true);
                        }
                    }
                }
            }
        }
    }
}
