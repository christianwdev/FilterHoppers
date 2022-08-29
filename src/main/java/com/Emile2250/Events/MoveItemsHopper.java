package com.Emile2250.Events;

import com.Emile2250.FilterHoppers;
import com.Emile2250.Objects.HopperFilter;
import com.Emile2250.Util.Converters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MoveItemsHopper implements Listener {

    @EventHandler
    public void MoveIntoHopper(InventoryMoveItemEvent e) {

        Location loc = e.getDestination().getLocation();

        if (loc == null) { return; } // Not a placed inventory, ie not our filters.

        String key = Converters.LocationToString(loc);
        HopperFilter filter = FilterHoppers.getInstance().getFilters().get(key);

        if (filter == null) { return; }

        if (e.getItem().getType() != filter.getFiltered()) {

            Block block = e.getInitiator().getLocation().getWorld().getBlockAt(e.getInitiator().getLocation());
            BlockData data = block.getBlockData();

            if (data instanceof Directional) {
                Directional directional = (Directional) data;
                Block relative = block.getRelative(directional.getFacing());

                if (relative.getType() == Material.HOPPER) {

                    Hopper hopper = (Hopper) relative;
                    Inventory hopperInv = hopper.getInventory();
                    ItemStack maxItems = getMaximumItems(e.getItem(), hopperInv);

                    hopperInv.addItem(maxItems);
                    e.getSource().remove(maxItems);

                }
            }

            e.setCancelled(true);
            return;
        }

        filter.updateHopperInventory();

    }

    @EventHandler
    public void MoveOutOfHopper(InventoryMoveItemEvent e) {

        Location source = e.getSource().getLocation();

        if (source == null) { return; } // Not a placed inventory, ie not our filters.

        String key = Converters.LocationToString(source);
        HopperFilter filter = FilterHoppers.getInstance().getFilters().get(key);

        if (filter == null) { return; }

        filter.updateHopperInventory();

    }

    @EventHandler
    public void HopperPickup(InventoryPickupItemEvent e) {

        Location loc = e.getInventory().getLocation();

        if (loc == null) { return; } // Not a placed inventory, ie not our filters.

        String key = Converters.LocationToString(loc);
        HopperFilter filter = FilterHoppers.getInstance().getFilters().get(key);

        if (filter == null) { return; }

        if (e.getItem().getItemStack().getType() != filter.getFiltered()) {
            e.setCancelled(true);
            return;
        }

        filter.updateHopperInventory();

    }

    public ItemStack getMaximumItems(ItemStack item, Inventory inv) {

        if (inv.contains(Material.AIR)) return item;
        if (!inv.contains(item.getType())) return new ItemStack(item.getType(), 0); // No item of the same type and no space

        int amountOfSpace = 0;
        for (ItemStack slot : inv.getContents()) { // Find how many open slots there are open if any.
            if (item.getType() == slot.getType()) {
                amountOfSpace += slot.getMaxStackSize() - slot.getAmount();
            }
        }

        item.setAmount(amountOfSpace);
        return item;

    }

}
