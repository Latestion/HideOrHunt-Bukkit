package me.latestion.hoh.utils.commandbuilder.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandHandler {
     boolean execute(CommandSender sender, Command command,String label, String[] args);
}
