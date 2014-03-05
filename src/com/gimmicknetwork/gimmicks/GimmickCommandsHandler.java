package com.gimmicknetwork.gimmicks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GimmickCommandsHandler implements CommandExecutor 
{

	private Gimmick gimmicks;
	public GimmickCommandsHandler(Gimmick gimmicks) {
		this.gimmicks = gimmicks;
	}
  
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("gimmicks") && args.length == 1) {
			if (args[0].equals("reload")) {
				this.gimmicks.reloadConfig();
				this.gimmicks.requestReloadEnabledModules(sender);
				//clear all current teams if it's now disabled
				if (!gimmicks.getConfig().getBoolean("teams.enabled", false)) {
					//Teams.teamManager().clearAllTeams();
					sender.sendMessage("[" + ChatColor.GREEN + "Gimmicks" + ChatColor.WHITE + "] Teams cleared.");
				}
				sender.sendMessage("[" + ChatColor.GREEN + "Gimmicks" + ChatColor.WHITE + "] Configuration reloaded.");
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("spreadall")) {
			if (args.length == 3) {
				sender.sendMessage("[Gimmicks] Spreading players.");
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p != null) {
						Utilities.spreadPlayer(p.getWorld(), p, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
					}
				}
			} else {
				sender.sendMessage("Usage: /spreadall <center x> <center z> <radius>");
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("muteall")) {
			gimmicks.muteAll = !gimmicks.muteAll;
			if (gimmicks.muteAll) {
				sender.sendMessage("[Gimmicks] Mute All on!");
			} else {
				sender.sendMessage("[Gimmicks] Mute All off!");
			}
			
		}
		
		if (cmd.getName().equalsIgnoreCase("healall")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if(p != null) {
					p.setHealth(20d);
					p.setFoodLevel(20);
					p.setSaturation(20f);
				}
			}
			
		}
		
		return true;
	}
  
}