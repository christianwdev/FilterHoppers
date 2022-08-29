package com.Emile2250.Events;

import com.Emile2250.FilterHoppers;
import com.Emile2250.Objects.HopperFilter;
import com.Emile2250.Util.ChatUtil;
import com.Emile2250.Util.Converters;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EditFilter implements Listener {

    public static final HashMap<Inventory, HopperFilter> openInvs = new HashMap<>();

    @EventHandler
    public void interactWithFilter(PlayerInteractEvent e) {

        Block block = e.getClickedBlock();

        if (block == null) { return; } // Clicked air
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) { return; } // Wasn't attempting to open something
        if (block.getType() != Material.HOPPER) { return; } // Not a filter

        String key = Converters.LocationToString(block.getLocation());
        HopperFilter filter = FilterHoppers.getInstance().getFilters().get(key);

        if (filter == null) { return; } // Not a filter, treat it as normal

        e.setCancelled(true);

        e.getPlayer().openInventory(filter.getInventory());
        openInvs.put(filter.getInventory(), filter);
    }

    @EventHandler
    public void closeFilter(InventoryCloseEvent e) {
        HopperFilter filter = openInvs.get(e.getInventory());

        if (filter == null) { return; }
        if (e.getInventory().getViewers().size() != 0) { return; } // Try to clear memory of closed filters.

        openInvs.remove(e.getInventory());
    }

    @EventHandler
    public void changeFiltered(InventoryClickEvent e) {
        HopperFilter filter = openInvs.get(e.getInventory());

        if (filter == null) { return; }

        if (e.getClick().isShiftClick()) {
            ChatUtil.sendMessage(e.getWhoClicked(), "&b&lFilter Hoppers » &7You are unable to shift click items in and out of filters.");
            e.setCancelled(true);
            return;
        }

        int slot = e.getRawSlot();

        if (slot >= 0 && slot <= 45) {
            if (e.getCursor() == e.getInventory().getItem(slot)) {
                e.setCancelled(true);
                return;
            }
        }

        if (slot >= 0 && slot <= 45 && slot != 13) {

            if (slot >= 29 && slot <= 33) {
                int relativeSlot = slot - 29;

                ItemStack cursor = e.getCursor();
                e.setCancelled(true);

                if (cursor != null && cursor.getType() != Material.AIR && cursor.getType() != filter.getFiltered()) {
                    ChatUtil.sendMessage(e.getWhoClicked(), "&b&lFilter Hoppers » &7You can only place items of the same type in the filter.");
                    return;
                }

                Inventory hopperInv = filter.getHopper().getInventory();
                ItemStack relativeItem = hopperInv.getItem(relativeSlot);

                if (e.getClick().isLeftClick()) {
                    if (relativeItem == null || relativeItem.getType() == Material.AIR) {
                        hopperInv.setItem(relativeSlot, e.getCursor());
                        e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));

                        filter.updateHopperInventory();
                        return;
                    }

                    if (cursor == null || cursor.getType() == Material.AIR) {
                        e.getWhoClicked().setItemOnCursor(relativeItem);
                        hopperInv.setItem(relativeSlot, new ItemStack(Material.AIR, 1));

                        filter.updateHopperInventory();
                        return;
                    }

                    int maxAllowed = filter.getFiltered().getMaxStackSize() - hopperInv.getItem(relativeSlot).getAmount();
                    int howMuchToAdd = Math.min(maxAllowed, cursor.getAmount());

                    relativeItem.setAmount(relativeItem.getAmount() + howMuchToAdd);
                    hopperInv.setItem(relativeSlot, relativeItem);

                    cursor.setAmount(cursor.getAmount() - howMuchToAdd);
                    e.getWhoClicked().setItemOnCursor(cursor);

                    filter.updateHopperInventory();
                    return;
                }

                if (e.getClick().isRightClick()) {
                    if (relativeItem == null || relativeItem.getType() == Material.AIR) { return; }
                    if (cursor == null || cursor.getType() == Material.AIR) {

                        int userReceives = (int) Math.ceil(relativeItem.getAmount() / 2);

                        e.getWhoClicked().setItemOnCursor(new ItemStack(relativeItem.getType(), userReceives));
                        relativeItem.setAmount(relativeItem.getAmount() - userReceives);

                        filter.updateHopperInventory();
                        return;
                    }

                    if (relativeItem.getMaxStackSize() == relativeItem.getAmount()) { return; }

                    relativeItem.setAmount(relativeItem.getAmount() + 1);
                    hopperInv.setItem(relativeSlot, relativeItem);

                    cursor.setAmount(cursor.getAmount() - 1);
                    e.getWhoClicked().setItemOnCursor(cursor);

                    filter.updateHopperInventory();
                    return;
                }

                return;
            }

            e.setCancelled(true); return;
        }

        if (slot == 13) {

            e.setCancelled(true);

            ItemStack item = e.getCursor();
            Material material;

            if (item == null) { material = Material.AIR; }
            else { material = item.getType(); }

            filter.setFiltered(material);
            e.getInventory().setItem(13, new ItemStack(material, 1));

            FilterHoppers.getInstance().saveAllFilters();
        }
    }

}
