package net.kettlemc.kcommon.adventure;

import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * A utility class for Adventure messages.
 */
public class AdventureUtil {

    private AdventureUtil() {
    }

    /**
     * Convert a MiniMessage string to a legacy string.
     *
     * @param miniMessage The MiniMessage string to convert.
     * @return The legacy string.
     */
    public static String miniMessageToLegacy(String miniMessage) {
        return componentToLegacy(MiniMessage.miniMessage().deserialize(miniMessage));
    }

    /**
     * Convert a component to a legacy string.
     *
     * @param component The component to convert.
     * @return The legacy string.
     */
    public static String componentToLegacy(Component component) {
        return BukkitComponentSerializer.legacy().serialize(component);
    }

}