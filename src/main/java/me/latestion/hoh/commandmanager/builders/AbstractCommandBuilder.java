package me.latestion.hoh.commandmanager.builders;


import javafx.util.Builder;
import me.latestion.hoh.commandmanager.AbstractCommand;
import me.latestion.hoh.commandmanager.HeadCommand;
import me.latestion.hoh.commandmanager.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCommandBuilder<T extends AbstractCommand> {
    private final Set<AbstractCommand> subcommands = new HashSet<>();
    private final String commandName;
    private final HashMap<String, Boolean> aliases = new HashMap<>();
    private String permissionName;
    private PermissionDefault permissionLevel;
    private boolean createPermission = true;
    private Permission permission;
    private String usageMessage;
    private String permissionMessage = "§cYou do not permission to do that!";
    private CommandExecutor commandHandler;
    private TabCompleter tabHandler;

    /**
     * Create a new command
     *
     * @param commandName Only one word is required, and not the full path
     */
    AbstractCommandBuilder(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Set the permission for the command, players who do not have permission to run the command will not be able
     * to tab complete the command. Upon running the command, players without permission will be sent the permission message {@link AbstractCommandBuilder#setPermissionMessage(String)}
     *
     * @param permission      Full permission in the form of a string
     * @param permissionLevel The default permission level
     * @return the builder
     * @see AbstractCommandBuilder#setPermissionMessage(String)
     * @see AbstractCommandBuilder#setPermission(Permission)
     */
    public AbstractCommandBuilder<T> setPermission(final String permission, final PermissionDefault permissionLevel) {
        this.permissionName = permission;
        this.permissionLevel = permissionLevel;
        createPermission = true;
        return this;
    }

    /**
     * Set the permission for the command, players who do not have permission to run the command will not be able
     * to tab complete the command. Upon running the command, players without permission will be sent the permission message {@link AbstractCommandBuilder#setPermissionMessage(String)}
     * <p>
     * If the permission has not already been registered it will be registered automatically.
     *
     * @param permission Permission registered or unregistered
     * @return the builder
     * @see AbstractCommandBuilder#setPermissionMessage(String)
     * @see AbstractCommandBuilder#setPermission(String, PermissionDefault)
     */
    public AbstractCommandBuilder<T> setPermission(final Permission permission) {
        this.permission = permission;
        createPermission = false;
        return this;
    }

    /**
     * Upon running the command, players without permission will be sent this message.
     * <p>
     * By default the permission message will be "§cYou do not permission to do that!"
     *
     * @param permissionMessage the message that will be sent
     * @return the builder
     * The permission can be set with {@link AbstractCommandBuilder#setPermission(String, PermissionDefault)} or {@link AbstractCommandBuilder#setPermission(Permission)}
     */
    public AbstractCommandBuilder<T> setPermissionMessage(final String permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }

    /**
     * The usage message will be sent to the player if the command handler returns false.
     * If this is not set, a  usage message will automatically be generated based upon the command's parents
     *
     * @param usageMessage the message that will be sent
     * @return the builder
     */

    public AbstractCommandBuilder<T> setUsageMessage(final String usageMessage) {
        this.usageMessage = usageMessage;
        return this;
    }


    /**
     * Add command aliases. Note: the command name is automatically added upon building {@link AbstractCommandBuilder#build()}
     *
     * @param alias    The name of the alias
     * @param isTabbed Setting this to true will allow the command to be tabbed in chat, otherwise it will not show up
     *                 Subcommands will still show up after writing the alias if this is set to false
     * @return the builder
     */
    public AbstractCommandBuilder<T> addAlias(String alias, boolean isTabbed) {
        aliases.put(alias, isTabbed);
        return this;
    }

    /**
     * Add a subcommand to the command executor and to the tab completer
     * It is highly recommended to use {@link SubCommandBuilder}
     *
     * @param subCommand the added subcommand
     * @return the builder
     */
    public AbstractCommandBuilder<T> addSubCommand(SubCommand subCommand) {
        this.subcommands.add(subCommand);
        return this;
    }

    /**
     * Set the command executor.  This method will only be called when the player has typed this AbstractCommand, meaning
     * that you do not need to check arguments. Returning false in the command handler will send the command sender the
     * usage message {@link AbstractCommandBuilder#setUsageMessage(String)}
     *
     * @param commandHandler the command executor
     * @return the builder
     */
    public AbstractCommandBuilder<T> setCommandHandler(CommandExecutor commandHandler) {
        this.commandHandler = commandHandler;
        return this;
    }

    /**
     * Set the tab handler. This method will only be called when the player has typed this AbstractCommand, meaning
     * that you do not need to check arguments. Items in the returned string list will automatically be removed if the
     * arguments do not start with the arguments
     *
     * @param tabHandler the tab handler
     * @return the builder
     */
    public AbstractCommandBuilder<T> setTabHandler(TabCompleter tabHandler) {
        this.tabHandler = tabHandler;
        return this;
    }

    /**
     * Compiles the command. When making a parent command, the command handler and tab handler will not be ran unless the
     * command is registered {@link HeadCommand#register(JavaPlugin)}
     *
     * @return the built command
     */

    public T build() {
        if (createPermission && permissionName != null) {
            if (Bukkit.getPluginManager().getPermission(permissionName) != null) {
                permission = Bukkit.getPluginManager().getPermission(permissionName);
                assert permission != null;
            } else {
                permission = new Permission(permissionName);
                permission.setDefault(permissionLevel);
                Bukkit.getServer().getPluginManager().addPermission(permission);
            }

        } else if (permission != null && !Bukkit.getServer().getPluginManager().getPermissions().contains(permission)) {
            Bukkit.getServer().getPluginManager().addPermission(permission);
        }

        return build(commandName, commandHandler, tabHandler, permission, permissionMessage, usageMessage, subcommands, aliases);
    }

    protected abstract T build(String commandName, CommandExecutor commandHandler, TabCompleter tabHandler, Permission permission, String permissionMessage, String usageMessage, Set<AbstractCommand> subCommands, HashMap<String, Boolean> aliases);
}
