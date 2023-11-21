package net.kettlemc.kcommon.language;

import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.minimessage.AdventureMessage;
import net.kettlemc.klanguage.api.LanguageAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
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
        Audience audience = adventure.sender(sender);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            audience.sendMessage(prefix.value().append(message.value(languageAPI.getEntity(player.getUniqueId()))));
            return;
        }
        audience.sendMessage(prefix.value().append(message.value()));
    }

    /**
     * Sends an adventure message to the target
     *
     * @param target       The target to send the message to (can be a player or console)
     * @param message      The message to send
     * @param placeholders The placeholders to replace in the message
     */
    public void sendMessage(CommandSender target, AdventureMessage message, Placeholder... placeholders) {
        sendMessage(target, message, null, null, placeholders);
    }

    /**
     * Sends an adventure message to the target
     *
     * @param target       The target to send the message to (can be a player or console)
     * @param message      The message to send
     * @param hover        The hover message to add to the message
     * @param command      The command to run when the message is clicked
     * @param placeholders The placeholders to replace in the message
     */
    public void sendMessage(CommandSender target, AdventureMessage message, AdventureMessage hover, String command, Placeholder... placeholders) {
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
            audience.sendMessage(prefix.value().append(component));
            return;
        }
        audience.sendMessage(prefix.value().append(message.value(placeholders)));
    }

}
