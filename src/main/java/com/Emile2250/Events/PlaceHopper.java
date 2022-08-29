package com.Emile2250.Events;

import com.Emile2250.FilterHoppers;
import com.Emile2250.Objects.HopperFilter;
import com.Emile2250.Util.ChatUtil;
import com.Emile2250.Util.Converters;
import com.Emile2250.Util.NBTUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlaceHopper implements Listener {

    /*
        DISABLE HOPPERS IN GENERAL BUT WE NEED TO ENABLE TO USE MOVEITEMEVENT
        org.bukkit.block.data.type.Hopper data = (org.bukkit.block.data.type.Hopper) e.getBlock().getBlockData();
        data.setEnabled(false);
        e.getBlock().setBlockData(data);
        e.getBlock().getState().update();
     */

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.HOPPER) {
            if (NBTUtil.isItem("item_name", "hopper_filter", e.getItemInHand())) {

                String locString = Converters.LocationToString(e.getBlock().getLocation());
                FilterHoppers.getInstance().getFilters().put(locString, new HopperFilter(e.getBlock().getLocation(), Material.AIR));
                FilterHoppers.getInstance().saveAllFilters();

                System.out.println(e.getBlock().getLocation() + " " + locString);

                ChatUtil.sendMessage(e.getPlayer(), "&b&lFilter Hoppers » &7Successfully placed your hopper filter.");

            }
        }
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.HOPPER) {

            String locString = Converters.LocationToString(e.getBlock().getLocation());

            HopperFilter filter = FilterHoppers.getInstance().getFilters().get(locString);
            if (filter == null) { return; }

            for (HumanEntity entity : filter.getInventory().getViewers()) {
                entity.closeInventory();
            }

            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);

            Player player = e.getPlayer();

            FilterHoppers.getInstance().getFilters().remove(locString);
            FilterHoppers.getInstance().saveAllFilters();

            ItemStack hopper = new ItemStack(Material.HOPPER, 1);
            hopper = NBTUtil.addTag(hopper, "item_name", "hopper_filter");

            ItemMeta meta = hopper.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Hopper Filter");
            hopper.setItemMeta(meta);

            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), hopper);

            ChatUtil.sendMessage(player, "&b&lFilter Hoppers » &7Successfully broke your hopper filter.");

        }
    }

}
