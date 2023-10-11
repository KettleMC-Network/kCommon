package net.kettlemc.kcommon.luckperms;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LuckPermsUtil {

    private LuckPermsUtil() {
    }

    /**
     * Get the LuckPerms prefix of a player.
     *
     * @param player The player to get the prefix of.
     * @return The LuckPerms prefix of the player or an empty string if the player is not found.
     */
    public static @NotNull String getLuckPermsPrefix(@NotNull Player player) {
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return "";
        }
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix == null ? "" : prefix;
    }

    /**
     * Get the LuckPerms suffix of a player.
     *
     * @param player The player to get the suffix of.
     * @return The LuckPerms suffix of the player or an empty string  if the player is not found.
     */
    public static @NotNull String getLuckPermsSuffix(@NotNull Player player) {
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return "";
        }
        String suffix = user.getCachedData().getMetaData().getSuffix();
        return suffix == null ? "" : suffix;
    }

}