package com.Emile2250;

import com.Emile2250.Commands.Admin;
import com.Emile2250.Events.EditFilter;
import com.Emile2250.Events.MoveItemsHopper;
import com.Emile2250.Events.PlaceHopper;
import com.Emile2250.Objects.HopperFilter;
import com.Emile2250.Util.Converters;
import com.Emile2250.Util.JSONEditor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class FilterHoppers extends JavaPlugin {

    private static FilterHoppers instance;
    private HashMap<String, HopperFilter> filters;

    @Override
    public void onEnable() {
        instance = this;
        filters = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new PlaceHopper(), this);
        Bukkit.getPluginManager().registerEvents(new EditFilter(), this);
        Bukkit.getPluginManager().registerEvents(new MoveItemsHopper(), this);

        getCommand("filterhoppersadmin").setExecutor(new Admin());

        // Load blocks
        loadAllCustomFilters();
    }

    public static FilterHoppers getInstance() {
        return instance;
    }

    public HashMap<String, HopperFilter> getFilters() {
        return filters;
    }

    @Override
    public void onDisable() {
        saveAllFilters();
    }

    public void saveAllFilters() {

        JSONObject obj = new JSONObject();
        JSONArray filtersArray = new JSONArray();

        for (HopperFilter filter : filters.values()) {
            filtersArray.add(filter.toJSON());
        }

        obj.put("hoppers", filtersArray);
        JSONEditor.writeToJSONFile("plugins/FilterHoppers/data.json", obj);

    }

    private void loadAllCustomFilters() {

        JSONObject obj = JSONEditor.getJSONFile("plugins/FilterHoppers/data.json");

        if (!obj.containsKey("hoppers")) { return; }

        JSONArray blocks = (JSONArray) obj.get("hoppers");
        Iterator<JSONObject> iterator = blocks.iterator();

        while (iterator.hasNext()) {

            JSONObject jsonFilter = iterator.next();

            double x = (double) jsonFilter.get("x");
            double y = (double) jsonFilter.get("y");
            double z = (double) jsonFilter.get("z");
            String world = (String) jsonFilter.get("world");
            Location location = new Location(Bukkit.getWorld(world), x, y, z);

            Material filtered = Material.getMaterial((String) jsonFilter.get("material"));

            filters.put(Converters.LocationToString(location), new HopperFilter(location, filtered));
        }
    }
}
