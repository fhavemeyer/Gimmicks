package com.gimmicknetwork.gimmicks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gimmicknetwork.gimmicks.gimmicks;

public class commands implements CommandExecutor 
{
	private gimmicks plugin;
  
	public commands(gimmicks plugin) {
		this.plugin = plugin;
	}
  
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		
		return true;
	}
  
}