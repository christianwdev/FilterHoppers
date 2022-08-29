package com.Emile2250.Commands;

import com.Emile2250.Util.ChatUtil;
import com.Emile2250.Util.NBTUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Admin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) { return false; }
        Player player = (Player) sender;

        if (command.getLabel().equalsIgnoreCase("filterhoppersadmin")) {

            if (args.length == 1) {
                switch (args[0]) {
                    case "give":

                        ItemStack hopper = new ItemStack(Material.HOPPER, 1);
                        hopper = NBTUtil.addTag(hopper, "item_name", "hopper_filter");

                        ItemMeta meta = hopper.getItemMeta();
                        meta.setDisplayName(ChatColor.AQUA + "Hopper Filter");
                        hopper.setItemMeta(meta);

                        player.getInventory().addItem(hopper);

                        ChatUtil.sendMessage(player, "&b&lFilter Hoppers » &7Successfully gave yourself one hopper filter.");

                        return false;
                    case "help":
                    default:
                        sendHelpMessage(player, label);
                        break;
                }
            }


            sendHelpMessage(player, label);
        }

        return false;
    }

    public void sendHelpMessage(Player player, String label) {
        ChatUtil.sendMessage(player, " ");
        ChatUtil.sendMessage(player, "&b&lFilter Hoppers ");
        ChatUtil.sendMessage(player, " ");
        ChatUtil.sendMessage(player, "&7» /" + label + " help, this page.");
        ChatUtil.sendMessage(player, "&7» /" + label + " give, spawns one hopper filter.");
        ChatUtil.sendMessage(player, " ");
    }
}
