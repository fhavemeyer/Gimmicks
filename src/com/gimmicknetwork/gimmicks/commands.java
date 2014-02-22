package com.gimmicknetwork.gimmicks;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

import com.gimmicknetwork.gimmicks.gimmicks;

public class commands implements CommandExecutor 
{
	private gimmicks plugin;
  
	public commands(gimmicks plugin) {
		this.plugin = plugin;
	}
  
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("team")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can run this command");
			} else {
				Player p = (Player) sender;
				if (args.length == 1) {
					if (p.hasPermission("gimmicks.teams.user")) {
			        	switch (args[0]) {
			        	
			        	case "red":		this.plugin.teams.put(p.getName(), ChatColor.RED);
			        					sender.sendMessage("Joined team: " + ChatColor.RED + "RED");
			        					break;
			        	case "blue":	this.plugin.teams.put(p.getName(), ChatColor.BLUE);
			        					sender.sendMessage("Joined team: " + ChatColor.BLUE + "BLUE");
										break;
			        	case "green":	this.plugin.teams.put(p.getName(), ChatColor.GREEN);
			        					sender.sendMessage("Joined team: " + ChatColor.GREEN + "GREEN");
										break;
			        	case "purple":	this.plugin.teams.put(p.getName(), ChatColor.LIGHT_PURPLE);
			        					sender.sendMessage("Joined team: " + ChatColor.LIGHT_PURPLE + "PURPLE");
										break;
			        	case "gold":	this.plugin.teams.put(p.getName(), ChatColor.GOLD);
			        					sender.sendMessage("Joined team: " + ChatColor.GOLD + "GOLD");
										break;
			        	case "aqua":	this.plugin.teams.put(p.getName(), ChatColor.AQUA);
			        					sender.sendMessage("Joined team: " + ChatColor.AQUA + "AQUA");
										break;
			        	default:		this.plugin.teams.remove(p.getName());
			        					sender.sendMessage("You left your team.");
			        					break;
			        	}
			        	TagAPI.refreshPlayer(p);
					}
		        	
				}
				if (args.length > 1) {
					if (p.hasPermission("gimmicks.teams.admin")) {
						Player[] players = Bukkit.getOnlinePlayers();
						for (Player player : players) {
							if(Arrays.asList(args).contains(player.getName().toString())) {
									//player.getName().toString().toLowerCase().matches(player.getName().toLowerCase())) {
								switch (args[0]) {
								
								case "red":		this.plugin.teams.put(player.getName(), ChatColor.RED);
						    					sender.sendMessage(player.getName() + " joined team: " + ChatColor.RED + "RED");
						    					break;
						    	case "blue":	this.plugin.teams.put(player.getName(), ChatColor.BLUE);
						    					sender.sendMessage(player.getName() + " joined team: " + ChatColor.BLUE + "BLUE");
												break;
						    	case "green":	this.plugin.teams.put(player.getName(), ChatColor.GREEN);
						    					sender.sendMessage(player.getName() + " joined team: " + ChatColor.GREEN + "GREEN");
												break;
						    	case "purple":	this.plugin.teams.put(player.getName(), ChatColor.LIGHT_PURPLE);
						    					sender.sendMessage(player.getName() + " joined team: " + ChatColor.LIGHT_PURPLE + "PURPLE");
												break;
						    	case "gold":	this.plugin.teams.put(player.getName(), ChatColor.GOLD);
						    					sender.sendMessage(player.getName() + " joined team: " + ChatColor.GOLD + "GOLD");
												break;
						    	case "aqua":	this.plugin.teams.put(player.getName(), ChatColor.AQUA);
						    					sender.sendMessage(player.getName() + " joined team: " + ChatColor.AQUA + "AQUA");
												break;
						    	case "remove":	this.plugin.teams.remove(p.getName());
		    									sender.sendMessage(player.getName().toString() + " removed from team.");
								break;				
						    	default:		this.plugin.teams.remove(p.getName());
						    					sender.sendMessage("Invalid team for " + player.getName().toString());
						    					break;
								}
								TagAPI.refreshPlayer(player);
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have permission to set other players' teams.");
					}
					
				}
			}
			
		}
		if (cmd.getName().equalsIgnoreCase("teamtp")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can run this command");
			} else {
				Player p = (Player) sender;
				if(args.length > 0) {						
					Location loc = p.getLocation();
					switch (args[0]) {
					
						case "red":		this.plugin.teamTP(ChatColor.RED, loc);
				    					break;
				    	case "blue":	this.plugin.teamTP(ChatColor.BLUE, loc);
										break;
				    	case "green":	this.plugin.teamTP(ChatColor.GREEN, loc);
										break;
				    	case "purple":	this.plugin.teamTP(ChatColor.LIGHT_PURPLE, loc);
										break;
				    	case "gold":	this.plugin.teamTP(ChatColor.GOLD, loc);
										break;
				    	case "aqua":	this.plugin.teamTP(ChatColor.AQUA, loc);
				    					break;			
				    	default:		sender.sendMessage("Invalid team");
				    					break;
						}
					}
				
			}
		}
		return true;
	}
  
}