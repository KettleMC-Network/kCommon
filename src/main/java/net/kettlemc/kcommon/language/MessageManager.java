package net.kettlemc.kcommon.language;

import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.minimessage.AdventureMessage;
import net.kettlemc.klanguage.api.LanguageAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A utility class for sending messages.
 */
public class MessageManager {

    private final AdventureMessage prefix;
    private final LanguageAPI<?> languageAPI;
    private final BukkitAudiences adventure;

    /**
     * Creates a new message manager
     *
     * @param prefix      The prefix to use for messages
     * @param languageAPI The language API to use for messages
     * @param adventure   The adventure instance to use for messages
     */
    public MessageManager(AdventureMessage prefix, LanguageAPI<?> languageAPI, BukkitAudiences adventure) {
        this.prefix = prefix;
        this.languageAPI = languageAPI;
        this.adventure = adventure;
    }

    /**
     * Sends an adventure message to the sender
     *
     * @param sender  The sender to send the message to (can be a player or console)
     * @param message The message to send
     */
    public void sendMessage(CommandSender sender, AdventureMessage message) {
        sendMessage(sender, message, true, null, null);
    }

    /**
     * Sends an adventure message to the sender
     *
     * @param sender    The sender to send the message to (can be a player or console)
     * @param message   The message to send
     * @param usePrefix Whether to prefix the message with the prefix
     */
    public void sendMessage(CommandSender sender, AdventureMessage message, boolean usePrefix) {
        sendMessage(sender, message, usePrefix, null, null);
    }

    /**
     * Sends an adventure message to the target
     *
     * @param target       The target to send the message to (can be a player or console)
     * @param message      The message to send
     * @param placeholders The placeholders to replace in the message
     */
    public void sendMessage(CommandSender target, AdventureMessage message, Placeholder... placeholders) {
        sendMessage(target, message, true, null, null, placeholders);
    }

    /**
     * Sends an adventure message to the target
     *
     * @param target       The target to send the message to (can be a player or console)
     * @param message      The message to send
     * @param usePrefix    Whether to prefix the message with the prefix
     * @param hover        The hover message to add to the message
     * @param command      The command to run when the message is clicked
     * @param placeholders The placeholders to replace in the message
     */
    public void sendMessage(CommandSender target, AdventureMessage message, boolean usePrefix, AdventureMessage hover, String command, Placeholder... placeholders) {
        Audience audience = adventure.sender(target);
        if (target instanceof Player) {
            Player player = (Player) target;
            Component component = message.value(languageAPI.getEntity(player.getUniqueId()), placeholders);
            if (hover != null) {
                component = component.hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(hover.value(languageAPI.getEntity(player.getUniqueId()), placeholders)));
            }
            if (command != null) {
                component = component.clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand(command));
            }
            audience.sendMessage(usePrefix ? prefix.value().append(component) : component);
            return;
        }
        audience.sendMessage(prefix.value().append(message.value(placeholders)));
    }

    /**
     * Broadcasts an adventure message to all players and the console
     *
     * @param message The message to broadcast
     */
    public void broadcastMessage(AdventureMessage message) {
        broadcastMessage(message, true);
    }

    /**
     * Broadcasts an adventure message to all players and the console
     *
     * @param message      The message to broadcast
     * @param placeholders The placeholders to replace in the message
     */
    public void broadcastMessage(AdventureMessage message, Placeholder... placeholders) {
        broadcastMessage(message, true, placeholders);
    }

    /**
     * Broadcasts an adventure message to all players and the console.
     *
     * @param message      The message to broadcast
     * @param prefix       Whether to prefix the message with the prefix
     * @param placeholders The placeholders to replace in the message
     */
    public void broadcastMessage(AdventureMessage message, boolean prefix, Placeholder... placeholders) {
        Component finalMessage = prefix ? this.prefix.value().append(message.value(placeholders)) : message.value(placeholders);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(AdventureUtil.componentToLegacy(finalMessage)));
        Bukkit.getConsoleSender().sendMessage(AdventureUtil.componentToLegacy(finalMessage));
    }

}
