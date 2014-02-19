package com.gimmicknetwork.gimmicks;

import org.bukkit.ChatColor;
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
				if (args.length > 0) {
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
			
		}
		
		return true;
	}
  
}