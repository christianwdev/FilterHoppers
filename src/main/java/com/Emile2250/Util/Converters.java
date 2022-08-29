package com.Emile2250.Util;

import org.bukkit.Location;

public class Converters {

    public static String LocationToString(Location loc) {
        return loc.getWorld().getName() + "" + loc.getX() + "" + loc.getY() + "" + loc.getZ();
    }

}
