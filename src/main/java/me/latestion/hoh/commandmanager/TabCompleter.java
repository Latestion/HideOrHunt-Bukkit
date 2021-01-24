package me.latestion.hoh.commandmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TabCompleter implements org.bukkit.command.TabCompleter {
    private final HeadCommand headCommand;

    TabCompleter(HeadCommand headCommand) {
        this.headCommand = headCommand;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        ArrayList<String> out = new ArrayList<>();
        ArrayList<String> possible = new ArrayList<>();
        AbstractCommand writtenCommand = getSubCommand(headCommand, args);
        if (writtenCommand == null) return out;
        if (writtenCommand.getPermission() != null && !sender.hasPermission(writtenCommand.getPermission())) return out;

        String[] relevantArgs = getRelevantArgs(args, writtenCommand);
        if (writtenCommand.getTabHandler() != null) {
            possible.addAll(writtenCommand.getTabHandler().onTabComplete(sender, command, label, relevantArgs));
        }

        writtenCommand.getSubCommands().forEach(sub -> {
            if (sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) return;
            sub.getAliases().forEach((alias, tab) -> {
                if (tab) possible.add(alias);
            });
        });
        possible.forEach(possibleTab -> {
            if (relevantArgs != null && !startsWith(relevantArgs, splitToArg(possibleTab))) return;
            out.add(possibleTab);
        });
        return out;
    }

    private AbstractCommand getSubCommand(AbstractCommand command, String[] args) {
        if (command.getSubCommands().size() == 0 || args.length == 0) return command;
        for (AbstractCommand commandSubCommand : command.getSubCommands()) {
            for (String commandName : commandSubCommand.getAliases().keySet()) {
                if (commandName.equalsIgnoreCase(args[0]))
                    return getSubCommand(commandSubCommand, removeFirstItem(args));
                if (commandName.toUpperCase().startsWith(args[0].toUpperCase()) && args.length == 1) return command;
            }
        }
        return null;
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

    private String[] removeFirstItem(String[] args) {
        ArrayList<String> out = new ArrayList<>(Arrays.asList(args));
        out.remove(0);
        return out.toArray(new String[0]);
    }

    private String[] splitToArg(String argument) {
        return argument.split(" ");
    }

    private boolean startsWith(String[] args, String[] suggestion) {
        if (args.length > suggestion.length) return false;
        if (args.length == 0) return true;
        if (suggestion[0].equals(args[0])) return startsWith(removeFirstItem(args), removeFirstItem(suggestion));

        return suggestion[0].startsWith(args[0]);
    }
}
