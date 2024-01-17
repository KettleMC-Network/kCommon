package net.kettlemc.kcommon.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.regex.Pattern;

/**
 * Utility class for Bukkit, contains methods that are useful for Bukkit plugins
 */
public class BukkitUtil {

    // Inspired by Spigot's Chatcolor
    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)" + "(&|§)" + "[0-9A-FK-OR]");

    private BukkitUtil() {
    }

    /**
     * Removes all minecraft color codes from the given String
     *
     * @param input The String to remove color codes from
     * @return The String without color codes
     */
    public static String stripColor(String input) {
        if (input == null) {
            return null;
        }
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }

    /**
     * Used to retrieve numerical values from permissions (e.g. plugin.amount.1)
     *
     * @param player         The player to check
     * @param permissionBase The base permission (e.g. plugin.amount)
     * @return The numerical value of the permission, 0 if not found, -1 if *
     */
    public static int getPermissionAmount(Player player, String permissionBase) {
        for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
            String permString = permission.getPermission();
            if (permString.startsWith(permissionBase.endsWith(".") ? permissionBase : permissionBase + ".")) {
                String[] amount = permString.split("\\.");

                if (amount[amount.length - 1].equalsIgnoreCase("*")) {
                    return -1;
                }

                try {
                    return Integer.parseInt(amount[amount.length - 1]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

}
