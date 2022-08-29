package com.Emile2250.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HopperFilter {

    private Material filtered;
    private Location location;
    private Inventory inventory;
    private Hopper hopper;

    public HopperFilter(Location location, Material filtered) {
        this.location = location;
        this.filtered = filtered;

        hopper = (Hopper) location.getBlock().getState();

        // Create ItemFilter inventory
        Inventory inv = Bukkit.createInventory(null, 45, "Filter");

        for (int i = 0; i < inv.getSize(); i++) {

            if (i >= 29 && i <= 33) { continue; }

            inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        }

        inv.setItem(13, new ItemStack(filtered, 1));

        this.inventory = inv;
        updateHopperInventory();
    }

    public void updateHopperInventory() {
        for (int i = 0; i < hopper.getInventory().getSize(); i++) {
            inventory.setItem(i + 29, hopper.getInventory().getItem(i));
        }
    }

    public Hopper getHopper() {
        return hopper;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Material getFiltered() {
        return filtered;
    }

    public Location getLocation() {
        return location;
    }

    public void setFiltered(Material filtered) {
        this.filtered = filtered;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public JSONObject toJSON() {

        JSONObject jsonFilter = new JSONObject();

        jsonFilter.put("x", location.getX());
        jsonFilter.put("y", location.getY());
        jsonFilter.put("z", location.getZ());
        jsonFilter.put("world", location.getWorld().getName());

        jsonFilter.put("material", filtered.toString());

        return jsonFilter;
    }
}
