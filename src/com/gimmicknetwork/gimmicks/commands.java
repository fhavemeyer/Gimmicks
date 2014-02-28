package com.gimmicknetwork.gimmicks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class commands implements CommandExecutor 
{

	private gimmicks gimmicks;
	public commands(gimmicks gimmicks) {
		this.gimmicks = gimmicks;
	}
  
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("gimmicks") && args.length == 1) {
			if (args[0].equals("reload")) {
				this.gimmicks.reloadConfig();
				sender.sendMessage("[" + ChatColor.GREEN + "Gimmicks" + ChatColor.WHITE + "] Configuration reloaded.");
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("spreadall")) {
			if (args.length == 3) {
				sender.sendMessage("[Gimmicks] Spreading players.");
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p != null) {
						gimmicks.hungercompass.spreadPlayer(p.getWorld(), p, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
					}
				}
			} else {
				sender.sendMessage("Usage: /spreadall <center x> <center z> <radius>");
			}
		}
		return true;
	}
  
}