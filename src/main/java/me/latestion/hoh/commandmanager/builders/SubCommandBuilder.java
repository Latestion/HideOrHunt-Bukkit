package me.latestion.hoh.commandmanager.builders;


import me.latestion.hoh.commandmanager.AbstractCommand;
import me.latestion.hoh.commandmanager.SubCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.Set;

public class SubCommandBuilder extends AbstractCommandBuilder<SubCommand> {

    /**
     * Crate a {@link SubCommand}. Subcommands parents / grandparents must eventually lead to a HeadCommand,
     * to create a HeadCommand see {@link CommandBuilder}
     *
     * @param commandName the name of the command, this should only be one word
     */
    public SubCommandBuilder(String commandName) {
        super(commandName);
    }

    @Override
    protected SubCommand build(String commandName, CommandExecutor commandHandler, TabCompleter tabHandler, Permission permission, String permissionMessage, String usageMessage, Set<AbstractCommand> subCommands, HashMap<String, Boolean> aliases) {
        return new SubCommand(commandName, commandHandler, tabHandler, permission, permissionMessage, usageMessage, subCommands, aliases);
    }
}
