package me.latestion.hoh.commandmanager.helper;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractCommand {

    private final String commandName;
    private final CommandExecutor commandHandler;
    private final TabCompleter tabHandler;
    private final Permission permission;
    private final String permissionMessage;
    private final Set<AbstractCommand> subCommands;
    private final HashMap<String, Boolean> aliases;
    private String usageMessage;

    AbstractCommand(String commandName, CommandExecutor commandHandler, TabCompleter tabHandler, Permission permission, String permissionMessage, String usageMessage, Set<AbstractCommand> subCommands, HashMap<String, Boolean> aliases) {
        this.commandName = commandName;
        this.commandHandler = commandHandler;
        this.tabHandler = tabHandler;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.usageMessage = usageMessage;
        this.subCommands = subCommands;
        this.aliases = aliases;
        this.aliases.put(commandName, true);
        assignHead(this);
    }

    public String getCommandName() {
        return commandName;
    }

    public CommandExecutor getCommandHandler() {
        return commandHandler;
    }

    public TabCompleter getTabHandler() {
        return tabHandler;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public String getUsageMessage() {
        return usageMessage;
    }

    public void setUsageMessage(String usageMessage) {
        this.usageMessage = usageMessage;
    }

    public Set<AbstractCommand> getSubCommands() {
        return subCommands;
    }

    public HashMap<String, Boolean> getAliases() {
        return new HashMap<String, Boolean>() {{
            putAll(aliases);
        }};
    }

    private void assignHead(AbstractCommand command) {
        command.getSubCommands().forEach(cmd -> {
            if (cmd instanceof SubCommand) {
                ((SubCommand) cmd).setParent(command);
            }
            assignHead(cmd);
        });
    }
}
