package me.latestion.hoh.commandmanager.builders;

import me.latestion.hoh.commandmanager.helper.AbstractCommand;
import me.latestion.hoh.commandmanager.helper.HeadCommand;
import me.latestion.hoh.commandmanager.helper.SubCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author BingoRufus
 * @version 1.2
 */
public class CommandBuilder extends AbstractCommandBuilder<HeadCommand> {
    private boolean generateHelpMessages = true;

    /**
     * Create a {@link HeadCommand}
     *
     * @param command The name of the command, this should only be one word
     * @see HeadCommand#register(JavaPlugin)
     * After building, HeadCommands will do nothing unless they are registered
     */
    public CommandBuilder(String command) {
        super(command);
    }

    @Override
    protected HeadCommand build(String commandName, CommandExecutor commandHandler, TabCompleter tabHandler, Permission permission, String permissionMessage, String usageMessage, Set<AbstractCommand> subCommands, HashMap<String, Boolean> aliases) {
        HeadCommand cmd = new HeadCommand(commandName, commandHandler, tabHandler, permission, permissionMessage, usageMessage, subCommands, aliases);
        String generatedUsage = generateUsageMessage(cmd, new StringBuilder());
        if (generateHelpMessages && cmd.getSubCommands().stream().noneMatch(sub -> sub.getCommandName().equalsIgnoreCase("help"))) {
            String usage = generatedUsage + "\n/" + commandName + " help";
            SubCommand help = new SubCommandBuilder("help").setCommandHandler(((commandSender, command, s, strings) -> {
                commandSender.sendMessage(usage);
                return true;
            })).build();
            cmd.getSubCommands().add(help);

        }

        return cmd;
    }

    /**
     * By default, a subcommand called '/<command> help' help will be generated which when ran will send the player
     * a list of all of commands' usage messages. This can be overwrote by creating your own help sub command or setting
     * this to false
     *
     * @param generateHelpMessage enable / disable automatically making the help command
     */

    public void setGenerateHelpMessage(boolean generateHelpMessage) {
        this.generateHelpMessages = generateHelpMessage;
    }

    private String generateUsageMessage(AbstractCommand cmd, final StringBuilder message) {
        if (message.length() != 0) message.append("\n");

        String usage = getUsage(cmd);
        if (cmd.getUsageMessage() == null) {
            cmd.setUsageMessage(usage);
        }
        message.append(usage);
        cmd.getSubCommands().forEach(subcommands -> generateUsageMessage(subcommands, message));

        return message.toString();
    }

    private String getUsage(AbstractCommand end) {
        if (end.getUsageMessage() != null) return end.getUsageMessage(); // Use already existing usage message

        StringBuilder message = new StringBuilder("/");
        for (AbstractCommand cmd : getPath(end)) { // Create a path like [head, sub, sub-sub]
            if (message.length() != 1) message.append(" ");
            message.append(cmd.getCommandName());
        }
        return message.toString();
    }

    private AbstractCommand[] getPath(AbstractCommand end) {
        ArrayList<AbstractCommand> out = new ArrayList<>();
        AbstractCommand edit = end;
        while (edit != null) {
            out.add(0, edit);
            if (edit instanceof SubCommand) {
                SubCommand sub = (SubCommand) edit;
                if (sub.hasParent()) {
                    edit = sub.getParent();
                    continue;
                }
            }
            break;
        }
        return out.toArray(new AbstractCommand[0]);

    }


}
