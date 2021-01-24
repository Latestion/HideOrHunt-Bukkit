package me.latestion.hoh.commandmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

class CommandCaller implements CommandExecutor {
    private final HeadCommand headCommand;

    CommandCaller(HeadCommand headCommand) {
        this.headCommand = headCommand;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase(headCommand.getCommandName())) {
            return true;
        }
        AbstractCommand applicableCommand = getSubCommand(headCommand, args);
        if (applicableCommand == null) applicableCommand = getLastCommand(headCommand, args);


        if (applicableCommand.getPermission() != null && !sender.hasPermission(applicableCommand.getPermission())) {
            sender.sendMessage(applicableCommand.getPermissionMessage());
            return true;
        }
        String[] relevantArgs = getRelevantArgs(args, applicableCommand);
        if (applicableCommand.getCommandHandler() != null) {
            if (!applicableCommand.getCommandHandler().onCommand(sender, command, label, relevantArgs)) {
                sender.sendMessage(applicableCommand.getUsageMessage());
            }
            return true;

        }
        if (getHelpCommand() != null) {
            getHelpCommand().getCommandHandler().onCommand(sender, command, label, relevantArgs);
            return true;
        }
        sender.sendMessage(headCommand.getUsageMessage());

        return true;
    }

    private AbstractCommand getSubCommand(AbstractCommand command, String[] args) {
        if (command.getSubCommands().size() == 0 || args.length == 0) return command;
        for (AbstractCommand sub : command.getSubCommands()) {
            for (String alias : sub.getAliases().keySet()) {
                if (alias.equalsIgnoreCase(args[0])) {
                    return getSubCommand(sub, removeFirstItem(args));
                }
            }
        }
        return null;
    }

    private String[] removeFirstItem(String[] args) {
        ArrayList<String> out = new ArrayList<>(Arrays.asList(args));
        out.remove(0);
        return out.toArray(new String[0]);
    }

    private AbstractCommand getLastCommand(AbstractCommand command, String[] args) {
        if (command.getSubCommands().size() == 0 || args.length == 0) return command;
        for (AbstractCommand commandSubCommand : command.getSubCommands()) {
            for (String commandName : commandSubCommand.getAliases().keySet()) {
                if (commandName.equalsIgnoreCase(args[0])) {
                    return getLastCommand(commandSubCommand, removeFirstItem(args));
                }
            }
        }
        return command;
    }

    private AbstractCommand getHelpCommand() {
        return headCommand.getSubCommands().stream().filter(subCommand1 -> subCommand1.getCommandName().equalsIgnoreCase("help")).findFirst().orElse(null);
    }

    private String[] getRelevantArgs(String[] args, AbstractCommand command) {
        if (args.length == 0) return args;
        String[] out = args;
        while (command instanceof SubCommand) {
            SubCommand sub = (SubCommand) command;
            out = removeFirstItem(out);
            if (!sub.hasParent()) break;
            command = sub.getParent();
        }
        return out;
    }
}
