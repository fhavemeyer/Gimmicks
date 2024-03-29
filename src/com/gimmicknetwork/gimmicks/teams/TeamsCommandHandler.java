package com.gimmicknetwork.gimmicks.teams;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamsCommandHandler implements CommandExecutor {
	private Teams teams;
	
	public TeamsCommandHandler(Teams teams) {
		this.teams = teams;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//check if teams are disabled
		if (!(sender instanceof Player)) {
			sender.sendMessage("[TEAMS] Only players can run this command");
			return false;
		}
		
		Player p = (Player)sender;
		
		if (cmd.getName().equalsIgnoreCase("team")) {
			if (args.length == 1) {
				if (p.hasPermission("gimmicks.teams.use")) {
					if (args[0].equalsIgnoreCase("leave")) {
						teams.teamManager().leaveTeam(p);
					} else {
						ChatColor color = getTeamColor(args[0]);
						teams.teamManager().setTeam(p, args[0], color);
					}
				} else {
					p.sendMessage("[TEAMS] " + ChatColor.RED + "You don't have permission to set your team.");
				}					
			} else if (args.length > 1) {
				if (p.hasPermission("gimmicks.teams.admin")) {
					Player[] players = Bukkit.getOnlinePlayers();
					ChatColor color = getTeamColor(args[0]);
					for (Player player : players) {
						if (Arrays.asList(args).contains(player.getName().toString())) {
							if (args[0].equalsIgnoreCase("leave")) {
								teams.teamManager().leaveTeam(player);
							} else {
								teams.teamManager().setTeam(player, args[0], color);
							}
						}
					}
				} else {
					p.sendMessage("[TEAMS] " + ChatColor.RED + "You don't have permission to set other players' teams.");
				}
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("teamtp")) {
			if (args.length > 0) {
				Location loc = p.getLocation();
				ChatColor color = getTeamColor(args[0]);
				if (color != null) {
					teams.teamManager().teamTP(color, loc);
				} else {
					p.sendMessage("[TEAMS] Invalid team.");
				}
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("teamsetspawn")) {
			if (args.length > 0) {
				Location loc = p.getLocation();
				ChatColor color = getTeamColor(args[0]);
				if (color != null) {
					teams.teamManager().teamSetSpawn(color, loc);
				} else {
					p.sendMessage("[TEAMS] Invalid team.");
				}
			} else {
				p.sendMessage("[TEAMS] Usage: /teamsetspawn <colour>");
			}
		}
		
		return true;
	}
	
	public ChatColor getTeamColor(String teamName) {
		switch (teamName) {
		case TeamColors.TEAM_RED:
			return ChatColor.RED;
		case TeamColors.TEAM_BLUE:
			return ChatColor.BLUE;
		case TeamColors.TEAM_GREEN:
			return ChatColor.LIGHT_PURPLE;
		case TeamColors.TEAM_GOLD:
			return ChatColor.GOLD;
		case TeamColors.TEAM_AQUA:
			return ChatColor.AQUA;
		default:
			return null;
		}
	}
}
