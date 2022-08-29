package com.Emile2250.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class ChatUtil {
    public static void broadcast(String msg) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(color(msg));
        }
    }

    public static void broadcastToWorld(String msg, World world) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getLocation().getWorld() == world) {
                p.sendMessage(color(msg));
            }
        }
    }

    public static void sendMessage(Player p, String msg) {
        p.sendMessage(color(msg));
    }

    public static void sendMessage(HumanEntity p, String msg) {
        p.sendMessage(color(msg));
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
