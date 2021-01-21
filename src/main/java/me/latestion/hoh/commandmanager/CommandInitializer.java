package me.latestion.hoh.commandmanager;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.localization.MessageManager;
import me.latestion.hoh.utils.Util;
import me.latestion.hoh.utils.commandbuilder.CommandBuilder;
import me.latestion.hoh.utils.commandbuilder.SubCommand;
import me.latestion.hoh.utils.commandbuilder.SubCommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class CommandInitializer {
    private final HideOrHunt plugin;
    public CommandInitializer(HideOrHunt main){
        this.plugin = main;
    }
    public void initialize(){
        MessageManager messageManager = plugin.getMessageManager();
        CommandBuilder builder = new CommandBuilder(plugin,"hoh");
        PluginManager pm = Bukkit.getPluginManager();
        builder.addSubCommand(new SubCommandBuilder("start").setPermission(pm.getPermission("hoh.start")).setUsage("/hoh start <team size>").setCommandHandler((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + " This command can only be ran by players.");
                return false;
            }
            Player player = (Player) sender;
            if(args.length != 2){
                return false;
            }
            Util util = new Util(plugin);
            int size = util.getInt(args[1]);
            if (size <= 0) {
                sender.sendMessage("§cSize must me greater than 0");
                //todo: add to message manager
                return true;
            }
            HOHGame game = plugin.game;
            if (game.gameState != GameState.OFF) {
                sender.sendMessage("§cThere is already a game in progress");
                //todo: add to message manager
                return true;
            }
            game.setSpawnLocation(player.getLocation());
            game.teamSize = size;
            game.prepareGame();
            return true;
        }).build());

        builder.addSubCommand(new SubCommandBuilder("freeze").setPermission(pm.getPermission("hoh.freeze")).setCommandHandler((sender, command, label, args) -> {
            if(plugin.game.gameState != GameState.ON){
                sender.sendMessage(messageManager.getMessage("game-not-started"));
                return true;
            }
            if (plugin.game.freeze) {
                Bukkit.broadcastMessage(messageManager.getMessage("unfreezed-game"));
                plugin.game.freeze = false;
            } else {
                Bukkit.broadcastMessage(messageManager.getMessage("freezed-game"));
                plugin.game.freeze = true;
            }
            return true;
        }).setUsage("/hoh freeze").build());

        builder.addSubCommand(new SubCommandBuilder("reload").setPermission(pm.getPermission("hoh.reload")).addAlias("reloadconfig",false).setCommandHandler((sender, command, label, args) -> {
            this.plugin.reloadConfig();
            sender.sendMessage(messageManager.getMessage("reloaded-config"));
            return true;
        }).setUsage("/hoh reload").build());
        builder.addSubCommand(new SubCommandBuilder("rules").setCommandHandler((sender, command, label, args) -> {
            List<String> rules = plugin.getConfig().getStringList("Rules-Messages");
            for (String rule : rules) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rule));
            }
            return true;
        }).setUsage("/hoh rules").build());
        builder.addSubCommand(new SubCommandBuilder("stop").setPermission(pm.getPermission("hoh.reload")).setCommandHandler((sender, command, label, args) -> {
            if (plugin.game.gameState == GameState.ON)
                plugin.game.endGame();
            else
                sender.sendMessage(messageManager.getMessage("game-not-started"));
            return true;
        }).setUsage("/hoh stop").build());

        builder.addSubCommand(new SubCommandBuilder("beacon").setPermission(pm.getPermission("hoh.beacon")).setCommandHandler((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + " This command can only be ran by players.");
                return true;
            }
            if(plugin.game.gameState != GameState.ON){
                sender.sendMessage(messageManager.getMessage("game-not-started"));
                return true;
            }
            if(args.length != 2) return false;
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null){
                sender.sendMessage(messageManager.getMessage("invalid-player"));
                return false;
            }

            Player player = (Player) sender;
            player.teleport(plugin.game.hohPlayers.get(target.getUniqueId()).getTeam().getBeacon().getLocation());
            //todo: add confirmation message to teleported player
            return true;
        }).setTabHandler((sender, command, label, args) -> {
            List<String> out = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> out.add(p.getName()));
            return out;
        }).setUsage("/hoh beacon <Player>").build());

        builder.addSubCommand(new SubCommandBuilder("chat").setCommandHandler((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + " This command can only be ran by players.");
                return true;
            }

            Player player = (Player) sender;
            if(plugin.game.gameState == GameState.OFF){
                sender.sendMessage(messageManager.getMessage("game-not-started"));
                return true;
            }
            if (plugin.game.hohPlayers.containsKey(player.getUniqueId())) {
                HOHPlayer p = plugin.game.hohPlayers.get(player.getUniqueId());
                if (p.teamChat) {
                    p.teamChat = false;
                    player.sendMessage(messageManager.getMessage("team-chat-off"));
                } else {
                    p.teamChat = true;
                    player.sendMessage(messageManager.getMessage("team-chat-on"));
                }
            }
            return true;
        }).setUsage("/hoh chat").build());
        builder.addSubCommand(new SubCommandBuilder("help").setUsage("/hoh help").setCommandHandler((sender, command, label, args) -> {
            //todo: idk what color scheme you use so i just did Gold and gray
            StringBuilder helpMessage = new StringBuilder();
            helpMessage.append("§6/hoh rules §7§l>§8 view the server's rules");
            helpMessage.append("\n");
            helpMessage.append("§6/hoh chat §7§l>§8  toggle team chat");
            helpMessage.append("\n");
            helpMessage.append("§6/hoh help §7§l>§8  view this help message");

            if(sender.hasPermission("hoh.start")){
                helpMessage.append("\n");
                helpMessage.append("§6/hoh start <team size> §7§l>§8 start the game with a specific amount of players");
            }
            if(sender.hasPermission("hoh.stop")){
                helpMessage.append("\n");
                helpMessage.append("§6/hoh stop §7§l>§8 stop the active game");
            }
            if(sender.hasPermission("hoh.freeze")){
                helpMessage.append("\n");
                helpMessage.append("§6/hoh stop §7§l>§8 pause the active game");
            }
            if(sender.hasPermission("hoh.reload")){
                helpMessage.append("\n");
                helpMessage.append("§6/hoh reload §7§l>§8 reload the config");
            }
            if(sender.hasPermission("hoh.beacon")){
                helpMessage.append("\n");
                helpMessage.append("§6/hoh beacon <player> §7§l>§8 Teleport to a player's beacon");
            }
            sender.sendMessage(helpMessage.toString());
            return true;
        }).build());

        builder.setCommandHandler((sender, command, label, args) -> {
            Bukkit.dispatchCommand(sender,"hoh help");
            return true;
        }).setUsage("/hoh");
        builder.register();
    }
}
