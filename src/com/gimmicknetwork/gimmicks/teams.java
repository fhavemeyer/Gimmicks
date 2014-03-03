package com.gimmicknetwork.gimmicks;

import java.util.Arrays;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class Teams implements CommandExecutor {
	
	private gimmicks gimmicks;
	public Teams(gimmicks gimmicks) {
		this.gimmicks = gimmicks;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//check if teams are disabled
		if (gimmicks.getConfig().getBoolean("teams.enabled", false)) {
			sender.sendMessage("[Gimmicks] Teams are disabled.");
			return true;
		}
	
		//SETTING TEAMS (/team)
		if (cmd.getName().equalsIgnoreCase("team")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can run this command");
			} else {
				Player p = (Player) sender;
				
				// /team <colour>
				if (args.length == 1) {
					if (p.hasPermission("gimmicks.teams.use")) {
						setTeam(p, args[0]);
					} else {
						sender.sendMessage("You don't have permission to set your team.");
					}
				} else if (args.length > 1) {
				
				// /team <colour> <player> (<player2> <player3> ...)
				
					if (p.hasPermission("gimmicks.teams.admin")) {
						Player[] players = Bukkit.getOnlinePlayers();
						for (Player player : players) {
							if(Arrays.asList(args).contains(player.getName().toString())) {
								setTeam(player, args[0]);
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have permission to set other players' teams.");
					}
				}				
			}
		}
		
	
		//TEAM TELEPORT (/teamtp)
		if (cmd.getName().equalsIgnoreCase("teamtp")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can run this command");
			} else {
				Player p = (Player) sender;
				if(args.length > 0) {						
					Location loc = p.getLocation();
					switch (args[0]) {
						case "red":		teamTP(ChatColor.RED, loc);
				    					break;
				    	case "blue":	teamTP(ChatColor.BLUE, loc);
										break;
				    	case "green":	teamTP(ChatColor.GREEN, loc);
										break;
				    	case "purple":	teamTP(ChatColor.LIGHT_PURPLE, loc);
										break;
				    	case "gold":	teamTP(ChatColor.GOLD, loc);
										break;
				    	case "aqua":	teamTP(ChatColor.AQUA, loc);
				    					break;			
				    	default:		sender.sendMessage("Invalid team");
				    					break;
					}
				}
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("teamsetspawn")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can run this command");
			} else {
				Player p = (Player) sender;
				if(args.length > 0) {						
					Location loc = p.getLocation();
					switch (args[0]) {
						case "red":		teamSetSpawn(ChatColor.RED, loc);
				    					break;
				    	case "blue":	teamSetSpawn(ChatColor.BLUE, loc);
										break;
				    	case "green":	teamSetSpawn(ChatColor.GREEN, loc);
										break;
				    	case "purple":	teamSetSpawn(ChatColor.LIGHT_PURPLE, loc);
										break;
				    	case "gold":	teamSetSpawn(ChatColor.GOLD, loc);
										break;
				    	case "aqua":	teamSetSpawn(ChatColor.AQUA, loc);
				    					break;			
				    	default:		sender.sendMessage("Invalid team");
				    					break;
					}
				} else {
					sender.sendMessage("Usage: /teamsetspawn <colour>");
				}
			}
		}
		return true;
	}

	public void setTeam(Player p, String t) {
        switch (t) {	
       		case "red":		this.gimmicks.teams.put(p.getName(), ChatColor.RED);
        					p.sendMessage("[TEAMS] Joined team: " + ChatColor.RED + "RED");
        					break;
        	case "blue":	this.gimmicks.teams.put(p.getName(), ChatColor.BLUE);
        					p.sendMessage("[TEAMS] Joined team: " + ChatColor.BLUE + "BLUE");
							break;
        	case "green":	this.gimmicks.teams.put(p.getName(), ChatColor.GREEN);
        					p.sendMessage("[TEAMS] Joined team: " + ChatColor.GREEN + "GREEN");
							break;
        	case "purple":	this.gimmicks.teams.put(p.getName(), ChatColor.LIGHT_PURPLE);
        					p.sendMessage("[TEAMS] Joined team: " + ChatColor.LIGHT_PURPLE + "PURPLE");
							break;
        	case "gold":	this.gimmicks.teams.put(p.getName(), ChatColor.GOLD);
        					p.sendMessage("[TEAMS] Joined team: " + ChatColor.GOLD + "GOLD");
							break;
        	case "aqua":	this.gimmicks.teams.put(p.getName(), ChatColor.AQUA);
        					p.sendMessage("[TEAMS] Joined team: " + ChatColor.AQUA + "AQUA");
							break;
        	case "leave":	this.gimmicks.teams.remove(p.getName());
        					p.sendMessage("[TEAMS] Left team.");
        					break;
        }
        	TagAPI.refreshPlayer(p);
	}

	public void teamTP (ChatColor t, Location loc) {
		for (Entry<String, ChatColor> entry : this.gimmicks.teams.entrySet())
		{
		    if (entry.getValue().equals(t)) {
		    	Player p = Bukkit.getPlayer(entry.getKey());
		    	if (p != null) {
		    		p.teleport(loc);
		    	}
		    }
		}
	}
	
	public void teamSetSpawn (ChatColor t, Location loc) {
		this.gimmicks.teamSpawn.put(t, loc);
	}
	
}
