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
			if (args.length == 4) {
			Player[] players = Bukkit.getOnlinePlayers();
				String c = "spreadplayers " + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " false";
				for (Player p : players) {
					c = c + " " + p.getName().toString();
				}
				gimmicks.getLogger().info("[Gimmicks] Running command:" + c);
				sender.getServer().dispatchCommand(Bukkit.getConsoleSender(), "command");
			}
		}
		
		return true;
	}
  
}