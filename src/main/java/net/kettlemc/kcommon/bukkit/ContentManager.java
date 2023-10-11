package net.kettlemc.kcommon.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for registering commands and listeners.
 */
public final class ContentManager {

    private final @NotNull JavaPlugin plugin;

    /**
     * Creates a new ContentManager.
     *
     * @param plugin The plugin to register commands and listeners for.
     */
    public ContentManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a command with the given name and executor.
     * If the executor is also a TabCompleter, the command will be registered with the TabCompleter.
     *
     * @param name     The name of the command.
     * @param executor The executor of the command (the class the command is registered in).
     */
    public void registerCommand(@NotNull String name, @NotNull CommandExecutor executor) {
        PluginCommand command = plugin.getCommand(name);
        if (command == null) {
            return;
        }

        command.setExecutor(executor);
        if (executor instanceof TabCompleter)
            command.setTabCompleter((TabCompleter) executor);
    }

    /**
     * Registers a listener.
     *
     * @param listener The listener to register.
     */
    public void registerListener(@NotNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

}
