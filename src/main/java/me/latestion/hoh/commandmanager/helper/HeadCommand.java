package me.latestion.hoh.commandmanager.helper;

import me.latestion.hoh.commandmanager.builders.CommandBuilder;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

/**
 * The main parent for all commands
 *
 * @see CommandBuilder
 */

public class HeadCommand extends AbstractCommand {
    private boolean isRegistered = false;

    /**
     * It is highly recommended to use the command builder
     *
     * @see CommandBuilder
     */
    public HeadCommand(String commandName, CommandExecutor commandHandler, TabCompleter tabHandler, Permission permission, String permissionMessage, String usageMessage, Set<AbstractCommand> subCommands, HashMap<String, Boolean> aliases) {
        super(commandName, commandHandler, tabHandler, permission, permissionMessage, usageMessage, subCommands, aliases);
    }

    /**
     * Register a command, this will create a CommandExecutor and TabCompleter for all subcommands
     *
     * @param plugin your main class
     */
    public void register(JavaPlugin plugin) {
        if (plugin == null) throw new IllegalArgumentException("plugin cannot be null");
        if (isRegistered) throw new RuntimeException("This command has already been registered");
        PluginCommand cmd = plugin.getCommand(getCommandName());
        if (cmd == null)
            throw new RuntimeException("A command with the name of \"{c}\" has not been registered in the plugin.yml".replace("{c}", getCommandName()));
        cmd.setExecutor(new CommandCaller(this));
        cmd.setTabCompleter(new me.latestion.hoh.commandmanager.helper.TabCompleter(this));
        isRegistered = true;
    }

    public boolean isRegistered() {
        return isRegistered;
    }
}
