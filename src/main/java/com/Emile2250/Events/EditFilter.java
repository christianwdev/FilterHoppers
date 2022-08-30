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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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
        if (!e.getPlayer().isSneaking()) { return; } // Way to differentiate the menus
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) { return; } // Trying to place a block?
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

        if (e.getClick().isShiftClick() || e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            e.setCancelled(true);
            return;
        }

        int slot = e.getRawSlot();

        if (slot == 13) {

            e.setCancelled(true);

            ItemStack item = e.getCursor();
            Material material;

            if (item == null) { material = Material.AIR; }
            else { material = item.getType(); }
            if (filter.getFiltered() == material) { return; }

            filter.setFiltered(material);
            e.getInventory().setItem(13, new ItemStack(material, 1));

            FilterHoppers.getInstance().saveAllFilters();
        }
    }
}
