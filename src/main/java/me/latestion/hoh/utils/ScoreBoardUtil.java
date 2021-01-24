package me.latestion.hoh.utils;

import me.latestion.hoh.HideOrHunt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoardUtil {

    public ScoreboardManager manager;
    public Map<String, Integer> scoreboardHolder = new HashMap<>();
    private Scoreboard board;
    private Objective obj;
    private HideOrHunt plugin;
    private int i = 1;

    public ScoreBoardUtil(HideOrHunt plugin) {
        this.plugin = plugin;
        scoreBoardRegister();
    }

    public void addAllPlayers() {
        for (Player player : plugin.game.getWorld().getPlayers()) {
            player.setScoreboard(board);
        }
    }

    public void addTeam(String name) {
        Score team = obj.getScore(ChatColor.translateAlternateColorCodes('&', name) + ": " + ChatColor.BOLD + ""
                + ChatColor.WHITE + "\u2713");
        team.setScore(i);
        scoreboardHolder.put(name, i);
        i++;
    }

    public void beaconPlaceTeam(String name) {
        obj.getScoreboard().resetScores(ChatColor.translateAlternateColorCodes('&', name) + ": " + ChatColor.BOLD + ""
                + ChatColor.WHITE + "\u2713");
        int i = scoreboardHolder.get(name);
        Score score = obj.getScore(ChatColor.translateAlternateColorCodes('&', name)
                + ": " + ChatColor.BOLD + "" + ChatColor.GREEN + "\u2713");
        score.setScore(i);
    }

    public void beaconBreakTeam(String name) {
        obj.getScoreboard().resetScores(ChatColor.translateAlternateColorCodes('&', name) + ": " + ChatColor.BOLD + ""
                + ChatColor.GREEN + "\u2713");
        int i = scoreboardHolder.get(name);
        Score score = obj.getScore(ChatColor.translateAlternateColorCodes('&', name)
                + ": " + ChatColor.BOLD + "" + ChatColor.WHITE + "\u2718");
        score.setScore(i);
    }


    public void eliminateTeam(String name) {
        obj.getScoreboard().resetScores(ChatColor.translateAlternateColorCodes('&', name) + ": " + ChatColor.BOLD + ""
                + ChatColor.WHITE + "\u2718");
        int i = scoreboardHolder.get(name);
        Score score = obj.getScore(ChatColor.translateAlternateColorCodes('&', name)
                + ": " + ChatColor.BOLD + "" + ChatColor.RED + "\u2718");
        score.setScore(i);
    }


    public void setAsthetic() {
        Score top = obj.getScore("\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b\ufe4b");
        Score bot = obj.getScore("\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f\ufe4f");
        Score strTeam = obj.getScore(plugin.getMessageManager().getMessage("scoreboard-teams"));
        top.setScore(plugin.game.getTeams().size() + 2);
        bot.setScore(0);
        strTeam.setScore(plugin.game.getTeams().size() + 1);
    }

    private void scoreBoardRegister() {
        this.manager = Bukkit.getScoreboardManager();
        this.board = this.manager.getNewScoreboard();
        this.obj = this.board.registerNewObjective("HideOrHunt", "dummy", ChatColor.translateAlternateColorCodes('&'
                , plugin.getMessageManager().getMessage("scoreboard-title")));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void addPlayer(Player player) {
        player.setScoreboard(board);
    }
}
