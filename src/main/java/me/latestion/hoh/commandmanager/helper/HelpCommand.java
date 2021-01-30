package me.latestion.hoh.commandmanager.helper;

import org.bukkit.command.CommandSender;

public class HelpCommand {

    private StringBuilder message;
    private CommandSender sender;

    public HelpCommand(CommandSender sender) {
        this.sender = sender;
        message = new StringBuilder();
    }

    public void addCommand(String command, String permission) {
        if (isEmpty(permission)) {
            message.append("\n");
            message.append(command);
        }
        else if (sender.hasPermission(permission)) {
            message.append("\n");
            message.append(command);
        }
    }

    public String getFinalMessage() {
        return message.toString();
    }

    private boolean isEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
