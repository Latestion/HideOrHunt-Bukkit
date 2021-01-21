package me.latestion.hoh.utils.commandbuilder;

import javafx.util.Builder;

import me.latestion.hoh.utils.commandbuilder.handlers.CommandHandler;
import me.latestion.hoh.utils.commandbuilder.handlers.TabHandler;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SubCommandBuilder implements Builder<SubCommand> {
    private final String commandName;
    private String permissionName;
    private PermissionDefault permissionLevel;
    private boolean createPermission = true;
    private Permission permission;
    private String usage;
    private String permissionMessage = "§cYou do not permission to do that!";
    private final HashMap<String,Boolean> aliases = new HashMap<>();
    protected final Set<SubCommand> subcommands = new HashSet<>();
    private CommandHandler handler;
    private TabHandler tabHandler;


    public SubCommandBuilder(final String name) {
        this.commandName = name;
    }
    public SubCommandBuilder setPermission(final String permission,final PermissionDefault permissionLevel){
        this.permissionName = permission;
        this.permissionLevel = permissionLevel;
        createPermission = true;
        return this;
    }

    public SubCommandBuilder setPermission(final Permission permission){
        this.permission = permission;
        createPermission = false;
        return this;
    }

    public SubCommandBuilder setUsage(final String usage) {
        this.usage = usage;
        return this;
    }

    public SubCommandBuilder setPermissionMessage(final String permissionMessage){
        this.permissionMessage = permissionMessage;
        return this;
    }

    public SubCommandBuilder addAlias(String alias, boolean isTabbed){
        aliases.put(alias,isTabbed);
        return this;
    }
    public SubCommandBuilder addSubCommand(SubCommand subCommand){
        this.subcommands.add(subCommand);
        return this;
    }
    public SubCommandBuilder setCommandHandler(CommandHandler handler){
        this.handler = handler;
        return this;
    }
    public SubCommandBuilder setTabHandler(TabHandler handler){
        this.tabHandler = handler;
        return this;
    }
    @Override
    public SubCommand build() {

            if (createPermission && permissionName != null) {
                if(Bukkit.getPluginManager().getPermission(permissionName) != null){
                    permission =  Bukkit.getPluginManager().getPermission(permissionName);
                    assert permission != null;
                }
                else {
                    permission = new Permission(permissionName);
                    permission.setDefault(permissionLevel);
                    Bukkit.getServer().getPluginManager().addPermission(permission);
                }

            } else if (permission != null && !Bukkit.getServer().getPluginManager().getPermissions().contains(permission)) {
                    Bukkit.getServer().getPluginManager().addPermission(permission);
            }


        return new SubCommand(commandName, permission, handler, tabHandler, usage == null ? generateUsageMessage() : usage, permissionMessage, subcommands, aliases);

    }
    protected String generateUsageMessage(){
        return "/" + commandName;
    }



}
