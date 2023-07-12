package me.latestion.hoh.commandmanager.helper;


import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Set;

public class SubCommand extends AbstractCommand {
    private AbstractCommand parent;

    public SubCommand(String commandName, CommandExecutor commandHandler, TabCompleter tabHandler, Permission permission, String permissionMessage, String usageMessage, Set<AbstractCommand> subCommands, HashMap<String, Boolean> aliases) {
        super(commandName, commandHandler, tabHandler, permission, permissionMessage, usageMessage, subCommands, aliases);
    }

    public @Nullable
    AbstractCommand getParent() {
        return parent;
    }

    public void setParent(@Nonnull AbstractCommand parent) {
        if (this.parent != null) this.parent.getSubCommands().remove(this);
        parent.getSubCommands().add(this);
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

}
