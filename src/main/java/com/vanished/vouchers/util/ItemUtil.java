package com.vanished.vouchers.util;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ItemUtil {

    public static String serializeItem(ItemStack itemStack) {
        StringBuilder stringBuilder = new StringBuilder();

        if (itemStack.getDurability() != 0) {
            stringBuilder.append(itemStack.getType().toString()).append(":").append(itemStack.getDurability()).append(" ");
        } else {
            stringBuilder.append(itemStack.getType().toString());
        }

        stringBuilder.append(itemStack.getAmount()).append(" ");

        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
                stringBuilder.append("name:").append(itemStack.getItemMeta().getDisplayName().replace(Character.toString(ChatColor.COLOR_CHAR), "&").replace(" ", "")).append(" ");
            }

            if (itemStack.getItemMeta().hasLore()) {
                stringBuilder.append("lore:");
                boolean firstLine = true;

                for (String line : itemStack.getItemMeta().getLore()) {
                    if (firstLine) {
                        firstLine = false;
                        stringBuilder.append(line.replace(Character.toString(ChatColor.COLOR_CHAR), "&").replace(" ", ""));
                        continue;
                    }

                    stringBuilder.append("/n").append(line.replace(Character.toString(ChatColor.COLOR_CHAR), "&").replace(" ", ""));
                }

                stringBuilder.append(" ");
            }
        }

        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            try {
                Enchantments.valueOf(enchantment.getName());
            } catch (IllegalArgumentException e) {
                continue;
            }

            stringBuilder.append(Enchantments.valueOf(enchantment.getName()).getNiceName().replace(" ", "")).append(":").append(itemStack.getEnchantmentLevel(enchantment));
            stringBuilder.append(" ");
        }

        return stringBuilder.toString().trim();
    }

    public static ItemStack deserializeItem(String str) {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String[] parsedString = str.split(" ");

        if (parsedString[0].contains(":")) {
            itemStack.setType(Material.valueOf(parsedString[0].split(":")[0].toUpperCase()));
            itemStack.setDurability(Short.parseShort(parsedString[0].split(":")[1]));
        } else {
            itemStack.setType(Material.valueOf(parsedString[0].split(":")[0].toUpperCase()));
        }

        itemStack.setAmount(Integer.parseInt(parsedString[1]));
        ArrayUtils.remove(parsedString, 0);
        ArrayUtils.remove(parsedString, 0);

        for (String parsedInfo : parsedString) {
            if (!parsedInfo.contains(":")) {
                continue;
            }

            String key = parsedInfo.split(":")[0];
            String value = parsedInfo.split(":")[1];

            switch (key) {
                case "name": {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', value.replace("_", " ")));
                    continue;
                }

                case "lore": {
                    List<String> lore = new ArrayList<>();

                    for (String line : value.split("/n")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', line.replace("_", " ")));
                    }

                    itemMeta.setLore(lore);
                    continue;
                }

                case "protection": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.parseInt(value), true);
                    continue;
                }
                case "fire_protection": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_FIRE, Integer.parseInt(value), true);
                    continue;
                }
                case "feather_falling": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_FALL, Integer.parseInt(value), true);
                    continue;
                }
                case "blast_protection": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, Integer.parseInt(value), true);
                    continue;
                }
                case "projectile_protection": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, Integer.parseInt(value), true);
                    continue;
                }
                case "respiration": {
                    itemMeta.addEnchant(Enchantment.OXYGEN, Integer.parseInt(value), true);
                    continue;
                }
                case "aqua_affinity": {
                    itemMeta.addEnchant(Enchantment.WATER_WORKER, Integer.parseInt(value), true);
                    continue;
                }
                case "thorns": {
                    itemMeta.addEnchant(Enchantment.THORNS, Integer.parseInt(value), true);
                    continue;
                }
                case "sharpness": {
                    itemMeta.addEnchant(Enchantment.DAMAGE_ALL, Integer.parseInt(value), true);
                    continue;
                }
                case "smite": {
                    itemMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, Integer.parseInt(value), true);
                    continue;
                }
                case "bane_of_arthropods": {
                    itemMeta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, Integer.parseInt(value), true);
                    continue;
                }
                case "knockback": {
                    itemMeta.addEnchant(Enchantment.KNOCKBACK, Integer.parseInt(value), true);
                    continue;
                }
                case "fire_aspect": {
                    itemMeta.addEnchant(Enchantment.FIRE_ASPECT, Integer.parseInt(value), true);
                    continue;
                }
                case "looting": {
                    itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, Integer.parseInt(value), true);
                    continue;
                }
                case "efficiency": {
                    itemMeta.addEnchant(Enchantment.DIG_SPEED, Integer.parseInt(value), true);
                    continue;
                }
                case "silk_touch": {
                    itemMeta.addEnchant(Enchantment.SILK_TOUCH, Integer.parseInt(value), true);
                    continue;
                }
                case "unbreaking": {
                    itemMeta.addEnchant(Enchantment.DURABILITY, Integer.parseInt(value), true);
                    continue;
                }
                case "power": {
                    itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, Integer.parseInt(value), true);
                    continue;
                }
                case "punch": {
                    itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, Integer.parseInt(value), true);
                    continue;
                }
                case "flame": {
                    itemMeta.addEnchant(Enchantment.ARROW_FIRE, Integer.parseInt(value), true);
                    continue;
                }
                case "infinity": {
                    itemMeta.addEnchant(Enchantment.ARROW_INFINITE, Integer.parseInt(value), true);
                    continue;
                }
                case "luck_of_the_sea": {
                    itemMeta.addEnchant(Enchantment.LUCK, Integer.parseInt(value), true);
                    continue;
                }
                case "lured": {
                    itemMeta.addEnchant(Enchantment.LURE, Integer.parseInt(value), true);
                    continue;
                }
                case "fortune": {
                    itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, Integer.parseInt(value), true);
                    continue;
                }
                case "depth_strider": {
                    itemMeta.addEnchant(Enchantment.DEPTH_STRIDER, Integer.parseInt(value), true);
                }
            }
        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
     *
     * @param playerInventory to turn into an array of strings.
     * @return Array of strings: [ main content, armor content ]
     * @throws IllegalStateException Unable to save item stacks
     */
    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        //get the main content part, this doesn't return the armor
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[] { content, armor };
    }

    /**
     *
     * A method to serialize an {@link ItemStack} array to Base64 String.
     *
     * <p />
     *
     * Based off of {@link #toBase64(Inventory)}.
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException Unable to save item stacks
     */
    private static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * A method to serialize an inventory to Base64 string.
     *
     * <p />
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException Unable to save item stacks
     */
    private static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Gets an array of ItemStacks from Base64 string.
     *
     * <p />
     *
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException Unable to decode class type
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     *
     * A method to get an {@link Inventory} from an encoded, Base64, string.
     *
     * <p />
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param data Base64 string of data containing an inventory.
     * @return Inventory created from the Base64 string.
     * @throws IOException Unable to decode class type
     */
    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     * Returns an itemstack using the info you provided
     *
     * @param material the material to create the item with
     * @return the generated item
     */
    private static ItemStack createItem(Material material) {
        return new ItemStack(material);
    }

    /**
     * Returns an itemstack using the info you provided
     *
     * @param material the material to create the item with
     * @param amount the amount of items in the stack
     * @return the generated item
     */
    public static ItemStack createItem(Material material, int amount) {
        return new ItemStack(material, amount);
    }

    /**
     * Returns an itemstack using the info you provided
     *
     * @param material the material to create the item with
     * @param amount the amount of items in the stack
     * @param dataValue the data value of the item
     * @return the generated item
     */
    public static ItemStack createItem(Material material, int amount, int dataValue) {
        return new ItemStack(material, amount, (short) dataValue);
    }

    /**
     * Returns an itemstack using the info you provided
     *
     * @param material the material to create the item with
     * @param itemName the display name of the item
     * @return the generated item
     */
    public static ItemStack createItem(Material material, String itemName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Returns an itemstack using the info you provided
     *
     * @param material the material to create the item with
     * @param itemName the display name of the item
     * @param itemLore the lore of the item
     * @return the generated item
     */
    public static ItemStack createItem(Material material, String itemName, List<String> itemLore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> newLore = new ArrayList<>();
        for (String str : itemLore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        itemMeta.setLore(newLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Returns an itemstack using the info you provided
     *
     * @param material the material to create the item with
     * @param amount the amount of items in the stack
     * @param itemName the display name of the item
     * @param itemLore the lore of the item
     * @return the generated item
     */
    public static ItemStack createItem(Material material, int amount, String itemName, List<String> itemLore) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> newLore = new ArrayList<>();
        for (String str : itemLore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        itemMeta.setLore(newLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Returns an itemstack using the info you provided
     *
     * @param material the material to create the item with
     * @param amount the amount of items in the stack
     * @param dataValue the data value of the item
     * @param itemName the display name of the item
     * @return the generated item
     */
    public static ItemStack createItem(Material material, int amount, int dataValue, String itemName) {
        ItemStack itemStack = new ItemStack(material, amount, (short) dataValue);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Util.colorize(itemName));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Returns an itemstack using the info you provided
     *
     * @param material the material to create the item with
     * @param amount the amount of items in the stack
     * @param dataValue the data value of the item
     * @param itemName the display name of the item
     * @param itemLore the lore of the item
     * @return the generated item
     */
    public static ItemStack createItem(Material material, int amount, int dataValue, String itemName, List<String> itemLore) {
        ItemStack itemStack = new ItemStack(material, amount, (short) dataValue);
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> newLore = new ArrayList<>();
        for (String str : itemLore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        itemMeta.setLore(newLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
