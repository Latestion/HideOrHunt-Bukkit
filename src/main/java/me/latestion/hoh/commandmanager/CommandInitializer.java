package me.latestion.hoh.commandmanager;


import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.commandmanager.builders.CommandBuilder;
import me.latestion.hoh.commandmanager.builders.SubCommandBuilder;
import me.latestion.hoh.commandmanager.helper.HelpCommand;
import me.latestion.hoh.data.flat.FlatHOHGame;
import me.latestion.hoh.game.GameState;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.game.HOHPlayer;
import me.latestion.hoh.localization.MessageManager;
import me.latestion.hoh.party.HOHPartyHandler;
import me.latestion.hoh.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandInitializer {
    private final HideOrHunt plugin;

    public CommandInitializer(HideOrHunt main) {
        this.plugin = main;
    }

    public void initialize() {
        MessageManager messageManager = plugin.getMessageManager();
        CommandBuilder builder = new CommandBuilder("hoh");
        PluginManager pm = Bukkit.getPluginManager();
        builder.addSubCommand(new SubCommandBuilder("start").setPermission(pm.getPermission("hoh.start")).setUsageMessage("/hoh start <team size>").setCommandHandler((sender, command, label, args) -> {
            if (args.length != 1) {
                return false;
            }
            Util util = new Util(plugin);
            int size = util.getInt(args[0]);
            if (size <= 0) {
                sender.sendMessage("§cSize must me greater than 0");
                //todo: add to message manager
                return true;
            }
            HOHGame game = plugin.game;
            if (game.getGameState() != GameState.OFF) {
                sender.sendMessage("§cThere is already a game in progress");
                //todo: add to message manager
                return true;
            }
            if (sender instanceof Player) {
                game.setSpawnLocation(((Player) sender).getLocation());
            }
            else {
                game.setSpawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
            if (game.getWorld().getPlayers().size() == 0) {
                // NO Players Online
                return false;
            }
            game.teamSize = size;
            game.prepareGame();
            return true;
        }).build());

        builder.addSubCommand(new SubCommandBuilder("freeze").setPermission(pm.getPermission("hoh.freeze")).setCommandHandler((sender, command, label, args) -> {
            if (plugin.game.getGameState() != GameState.ON) {
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
        }).setUsageMessage("/hoh freeze").build());

        builder.addSubCommand(new SubCommandBuilder("reload").setPermission(pm.getPermission("hoh.reload")).addAlias("reloadconfig", false).setCommandHandler((sender, command, label, args) -> {
            this.plugin.saveDefaultConfig();
            this.plugin.reloadConfig();
            sender.sendMessage(messageManager.getMessage("reloaded-config"));
            return true;
        }).setUsageMessage("/hoh reload").build());
        builder.addSubCommand(new SubCommandBuilder("rules").setCommandHandler((sender, command, label, args) -> {
            List<String> rules = plugin.getConfig().getStringList("Rules-Messages");
            for (String rule : rules) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rule));
            }
            return true;
        }).setUsageMessage("/hoh rules").build());
        builder.addSubCommand(new SubCommandBuilder("stop").setPermission(pm.getPermission("hoh.reload")).setCommandHandler((sender, command, label, args) -> {
            if (plugin.game.getGameState() == GameState.ON)
                plugin.game.endGame("none");
            else
                sender.sendMessage(messageManager.getMessage("game-not-started"));
            return true;
        }).setUsageMessage("/hoh stop").build());

        builder.addSubCommand(new SubCommandBuilder("beacon").setPermission(pm.getPermission("hoh.beacon")).setCommandHandler((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + " This command can only be ran by players.");
                return true;
            }
            if (plugin.game.getGameState() != GameState.ON) {
                sender.sendMessage(messageManager.getMessage("game-not-started"));
                return true;
            }
            if (args.length != 1) return false;
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
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
        }).setUsageMessage("/hoh beacon <player>").build());

        builder.addSubCommand(new SubCommandBuilder("chat").setCommandHandler((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + " This command can only be ran by players.");
                return true;
            }

            Player player = (Player) sender;
            if (plugin.game.getGameState() == GameState.OFF) {
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
        }).setUsageMessage("/hoh chat").build());

        builder.addSubCommand(new SubCommandBuilder("continue").setPermission(pm.getPermission("hoh.continue")).setCommandHandler((sender, command, label, args) -> {
            if (plugin.game.getGameState() != GameState.OFF) {
                sender.sendMessage("§cThere is already a game in progress");
                return true;
            }
            HOHGame test = FlatHOHGame.deserialize(new File(plugin.getDataFolder(), "hohGame.yml"), plugin);
            if (test != null) {
                plugin.game = test;
                plugin.game.loadGame();
            } else {
                sender.sendMessage(ChatColor.RED + "There is no game to continue!");
            }

            return true;
        }).setUsageMessage("/hoh continue").build());

        builder.addSubCommand(new SubCommandBuilder("craft").setPermission(pm.getPermission("hoh.craft")).setCommandHandler((sender, command, label, args) -> {
            if (plugin.game.allowCrafting) {
                plugin.game.allowCrafting = false;
                sender.sendMessage(ChatColor.RED + "Crafting is disabled!");
                return true;
            }
            plugin.game.allowCrafting = true;
            sender.sendMessage(ChatColor.AQUA + "Crafting is enabled!");
            return true;
        }).setUsageMessage("/hoh craft").build());

        builder.addSubCommand(new SubCommandBuilder("spy").setPermission(pm.getPermission("hoh.spy")).setCommandHandler((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + " This command can only be ran by players.");
                return true;
            }
            Player player = (Player) sender;
            boolean nowSpying = !plugin.game.isSpying(player);
            plugin.game.setSpying(player, nowSpying);
            if (nowSpying) {
                sender.sendMessage("§aYou can now view team chats");
                return true;
                //todo localized messages
            }
            sender.sendMessage("§cYou can no longer view team chats");
            return true;
        }).setUsageMessage("/hoh spy").build());

        builder.addSubCommand(new SubCommandBuilder("queue").setCommandHandler((sender, command, label, args) -> {
            if (plugin.support == null) {
                return true;
            }
            if (args.length == 2) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("l")) {
                        plugin.support.removeQueuePlayer(player.getUniqueId());
                        return true;
                    }
                    int teamSize = new Util(plugin).getInt(args[0]);
                    if (teamSize <= 0) {
                        return true;
                    }
                    int maxPlayers = new Util(plugin).getInt(args[1]);
                    if (maxPlayers <= 0) {
                        return true;
                    }
                    UUID id = player.getUniqueId();
                    if (plugin.party.inParty(id)) {
                        if (plugin.party.ownsParty(id)) {
                            if (plugin.party.getPartySize(id) == 1) {
                                return true;
                            }
                            else {
                                plugin.support.queueTeam(plugin.party.getParty(id).getParty(), teamSize, maxPlayers);
                                return true;
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    plugin.support.queueTeam(id, teamSize, maxPlayers);
                } else {
                    sender.sendMessage(ChatColor.RED + " This command can only be ran by players.");
                    return false;
                }
                return true;
            }
            if (args.length == 3) {
                try {
                    int teamSize = new Util(plugin).getInt(args[0]);
                    if (teamSize <= 0) {
                        return true;
                    }
                    int maxPlayers = new Util(plugin).getInt(args[1]);
                    if (maxPlayers <= 0) {
                        return true;
                    }
                    Player p = Bukkit.getPlayerExact(args[2]);
                    if (p == null || !p.isValid()) {
                        sender.sendMessage(messageManager.getMessage("invalid-player"));
                        return true;
                    }
                    UUID id = p.getUniqueId();
                    if (plugin.party.inParty(id)) {
                        if (plugin.party.ownsParty(id)) {
                            if (plugin.party.getPartySize(id) == 1) {
                                return true;
                            }
                            else {
                                plugin.support.queueTeam(plugin.party.getParty(id).getParty(), teamSize, maxPlayers);
                                return true;
                            }
                        }
                        else {
                            return true;
                        }
                    }
                    plugin.support.queueTeam(p.getUniqueId(), teamSize, maxPlayers);
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(messageManager.getMessage("invalid-player"));
                    return true;
                }
            }
            return false;
        }).setTabHandler((sender, command, label, args) -> {
            List<String> out = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> out.add(p.getName()));
            return out;
        }).setUsageMessage("/hoh queue [<teamsize>] [<maxplayers>] [<player>]").build());


        builder.addSubCommand(new SubCommandBuilder("rejoin").setCommandHandler((sender, command, label, args) -> {
            if (plugin.support == null) {
                return true;
            }
            if (!(sender instanceof Player)) {
                // Not Player
            }
            Player player = (Player) sender;
            if (plugin.support.rejoin(player.getUniqueId())) {
                plugin.support.pm.connect(player, plugin.support.rejoinServer.get(player.getUniqueId()));
            }
            else {
                //todo: msg
                // No GAME
                return true;
            }

            return false;
        }).setUsageMessage("/hoh rejoin").build());

        builder.addSubCommand(new SubCommandBuilder("party").setCommandHandler((sender, command, label, args) -> {
            if (plugin.party == null) {
                return true;
            }
            if (!(sender instanceof Player)) {
                // Not Player
            }
            Player player = (Player) sender;

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("join")) {
                    Player p = Bukkit.getPlayerExact(args[1]);
                    if (!p.isValid() || p == null) {
                        sender.sendMessage(messageManager.getMessage("invalid-player"));
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("invite")) plugin.party.createParty(player.getUniqueId(), p.getUniqueId());
                    else plugin.party.joinParty(player.getUniqueId(), p.getUniqueId());
                    return true;
                }
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("leave")) {
                    if (plugin.party.inParty(player.getUniqueId())) {
                        HOHPartyHandler hand = plugin.party.partyPlayer.get(player.getUniqueId()).party;
                        hand.removePlayer(player.getUniqueId());
                    }
                    else {
                        // Not In Party
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("disband")) {
                    if (plugin.party.inParty(player.getUniqueId())) {
                        HOHPartyHandler hand = plugin.party.partyPlayer.get(player.getUniqueId()).party;
                        hand.removePlayer(hand.getLeader());
                    }
                    else {
                        // Not In Party
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (plugin.party.inParty(player.getUniqueId())) {
                        HOHPartyHandler hand = plugin.party.partyPlayer.get(player.getUniqueId()).party;
                        List<String> players = hand.getPartyPlayerNames();
                        player.sendMessage((ChatColor.GREEN + "Party (%size%): " + ChatColor.AQUA + String.join(", ", players))
                                .replace("%size%", Integer.toString(players.size())));
                    }
                }
            }
            return false;
        }).setTabHandler((sender, command, label, args) -> {
            List<String> out = new ArrayList<>();
            out.add("invite"); out.add("join"); out.add("leave"); out.add("disband");
            return out;
        }).setUsageMessage("/hoh party invite,join,leave,disband").build());


        builder.addSubCommand(new SubCommandBuilder("wb").setPermission(pm.getPermission("hoh.wb")).setCommandHandler((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                // Not Player
            }
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(""); // TODO: COMMANDS HERE
                return true;
            }

            if (plugin.game.getGameState() != GameState.ON) {
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {

                if (args.length == 1) {
                    player.sendMessage(""); // TODO: COMMANDS HERE
                    return true;
                }

                int i = new Util(plugin).getInt(args[1]);

                if (i <= 0) {
                    player.sendMessage(""); // TODO: COMMANDS HERE
                    return true;
                }

                plugin.game.setBorder(i);
                // TODO: SEND MESSAGE

                return true;
            }

            return false;
        }).setUsageMessage("/hoh wb").build());


        builder.addSubCommand(new SubCommandBuilder("help").setUsageMessage("/hoh help").setCommandHandler((sender, command, label, args) -> {
            //todo: idk what color scheme you use so i just did Gold and gray
            HelpCommand helpMessage = new HelpCommand(sender);
            helpMessage.addCommand("§6/hoh help §7§l>§8  view this help message", null);
            helpMessage.addCommand("§6/hoh start <team size> §7§l>§8 start the game with a specific amount of players", "hoh.start");
            helpMessage.addCommand("§6/hoh stop §7§l>§8 stop the active game", "hoh.stop");
            helpMessage.addCommand("§6/hoh freeze §7§l>§8 pause/freezes the active game", "hoh.freeze");
            helpMessage.addCommand("§6/hoh reload §7§l>§8 reload the config", "hoh.reload");
            helpMessage.addCommand("§6/hoh beacon <player> §7§l>§8 teleport to a player's beacon", "hoh.beacon");
            helpMessage.addCommand("§6/hoh continue §7§l>§8 continues previous game", "hoh.continue");
            helpMessage.addCommand("§6/hoh spy §7§l>§8 \"spy\" on other teams' chat", "hoh.spy");
            helpMessage.addCommand("§6/hoh craft §7§l>§8 enables/disables crafting table crafting", "hoh.craft");
            helpMessage.addCommand("§6/hoh rules §7§l>§8 view the server's rules", null);
            helpMessage.addCommand("§6/hoh chat §7§l>§8  toggle team chat", null);
            if (plugin.support != null) {
                helpMessage.addCommand("§6/hoh queue <teamsize> <max_players> §7§l>§8 join the game queue", null);
                helpMessage.addCommand("§6/hoh rejoin §7§l>§8 rejoin the previous game you were in", null);
            }
            if (plugin.party != null) {
                helpMessage.addCommand("§6/hoh party [invite] <player> §7§l>§8 invite someone to your party", null);
                helpMessage.addCommand("§6/hoh party [join] <player> §7§l>§8 join someone to your party", null);
                helpMessage.addCommand("§6/hoh party leave> §7§l>§8 leave your party", null);
                helpMessage.addCommand("§6/hoh party disband> §7§l>§8 disbands your party", null);
                helpMessage.addCommand("§6/hoh party list> §7§l>§8 list of players in your party", null);
            }
            sender.sendMessage(helpMessage.getFinalMessage());
            return true;
        }).build());

        builder.setCommandHandler((sender, command, label, args) -> {
            Bukkit.dispatchCommand(sender, "hoh help");
            return true;
        }).setUsageMessage("/hoh");

        builder.build().register(plugin);
    }
}
