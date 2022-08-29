package com.Emile2250.Util;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NBTUtil {

    public static ItemStack addTag(ItemStack item, String name, String value) {
        if (item == null || item.getType() == Material.AIR) { return null; }

        NBTItem nbti = new NBTItem(item);
        nbti.setString(name, value);
        return nbti.getItem();
    }

    public static String getStringTag(ItemStack item, String name) {
        if (item == null || item.getType() == Material.AIR) { return null; }

        NBTItem nbti = new NBTItem(item);
        return nbti.getString(name);
    }

    public static boolean isItem(String name, String equal, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) { return false; }

        String tag = getStringTag(item, name);

        if (tag == null)
            return false;

        return tag.equalsIgnoreCase(equal);
    }
}
