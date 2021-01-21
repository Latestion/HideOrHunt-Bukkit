package me.latestion.hoh.utils.commandbuilder;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author BingoRufus
 * @version 1.0
 */
public class CommandBuilder  extends SubCommandBuilder {
    final JavaPlugin plugin;
    final String command;

    public CommandBuilder(JavaPlugin plugin, String command) {
        super(command);
        this.plugin = plugin;
        this.command = command;
    }

    public void register(){
        SubCommand subCommand = super.build();
        setUsage(generateUsageMessage(subCommand));
        subCommand = super.build();
        PluginCommand cmd = plugin.getCommand(command);
        if(cmd == null) throw new IllegalArgumentException("A command with the name of \"{c}\" has not been registered in the plugin.yml".replace("{c}",command));
        cmd.setExecutor(new CommandCaller(subCommand));
       cmd.setTabCompleter(new TabCompleter(subCommand));
    }

    @Override
    protected String generateUsageMessage() {
        return generateUsageMessage(subcommands.toArray(new SubCommand[0]));

    }
    private String generateUsageMessage(SubCommand... tree){
        StringBuilder baseCommand = new StringBuilder("/");
        for(SubCommand cmd : tree){
            baseCommand.append(cmd.getCommandName());
            baseCommand.append(" ");
        }
        baseCommand = new StringBuilder(baseCommand.toString().trim());
        for(SubCommand sub : tree[tree.length-1].getSubCommands()){
            baseCommand.append("\n").append(sub.getUsageMessage());
        }
        return baseCommand.toString();
    }
}
