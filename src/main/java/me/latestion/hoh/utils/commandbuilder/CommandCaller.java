package me.latestion.hoh.utils.commandbuilder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

class CommandCaller implements CommandExecutor {
     private final SubCommand subCommand;
     public CommandCaller(SubCommand subCommand) {
         this.subCommand = subCommand;
     }

    @Override
    public boolean onCommand( @Nonnull CommandSender sender, Command command, @Nonnull String label, @Nonnull String[] args) {
        if(!command.getName().equalsIgnoreCase(subCommand.getCommandName())){
            return true;
        }
        SubCommand applicableCommand = getSubCommand(subCommand,args);
        if(applicableCommand == null){
            sender.sendMessage( getSubCommand(subCommand,removeFirstItem(args)) == null  ? getLastCommand(subCommand,args).getUsageMessage(): subCommand.getUsageMessage());
            return true;
        }
        if(applicableCommand.getPermission() != null && !sender.hasPermission(applicableCommand.getPermission())){
            sender.sendMessage(applicableCommand.getPermissionMessage());
            return true;
        }

        if(applicableCommand.getCommandHandler() != null) {
            if(!applicableCommand.getCommandHandler().execute(sender,command,label,args)) {
                SubCommand helpCommand = subCommand.getSubCommands().stream().filter(subCommand1 -> subCommand1.getCommandName().equalsIgnoreCase("help")).findFirst().orElse(applicableCommand);
                if(helpCommand != applicableCommand && helpCommand.getCommandName().equalsIgnoreCase("help")){
                    helpCommand.getCommandHandler().execute(sender,command,label,args);
                    return true;
                }
                sender.sendMessage(applicableCommand.getUsageMessage());
            }
        }

        return true;
    }
    private SubCommand getSubCommand(SubCommand command, String[] args){
         if(command.getSubCommands().size() == 0 || args.length == 0) return command;
         for(SubCommand sub : command.getSubCommands()){
             for(String alias : sub.getAliases().keySet()){
                 if(alias.equalsIgnoreCase(args[0])){
                     return getSubCommand(sub, removeFirstItem(args));
                 }
             }
         }
         return null;
    }
    private String[] removeFirstItem(String[] args){
        ArrayList<String > out = new ArrayList<>(Arrays.asList(args));
        out.remove(0);
        return out.toArray(new String[0]);
    }
    private SubCommand getLastCommand(SubCommand command, String[] args){
        if(command.getSubCommands().size() == 0 || args.length == 0) return command;
        for (SubCommand commandSubCommand : command.getSubCommands()) {
            for (String commandName : commandSubCommand.getAliases().keySet()) {
                if (commandName.equalsIgnoreCase(args[0])) {
                    return getLastCommand(commandSubCommand, removeFirstItem(args));
                }
            }
        }
        return command;
    }
}
