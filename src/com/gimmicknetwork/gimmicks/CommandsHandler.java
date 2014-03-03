package com.gimmicknetwork.gimmicks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class CommandsHandler implements CommandExecutor 
{

	private gimmicks gimmicks;
	public CommandsHandler(gimmicks gimmicks) {
		this.gimmicks = gimmicks;
	}
  
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("gimmicks") && args.length == 1) {
			if (args[0].equals("reload")) {
				this.gimmicks.reloadConfig();
				//clear all current teams if it's now disabled
				if (!gimmicks.getConfig().getBoolean("teams.enabled", false)) {
					gimmicks.teams.clear();
					for (Player p : Bukkit.getOnlinePlayers()) {
						TagAPI.refreshPlayer(p);
					}
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
						gimmicks.hungercompass.spreadPlayer(p.getWorld(), p, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
					}
				}
			} else {
				sender.sendMessage("Usage: /spreadall <center x> <center z> <radius>");
			}
		}
		
		/*
		 * Magic Chests
		 */
		
		if (cmd.getName().equalsIgnoreCase("setchest")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("[Gimmicks] Only players can run this command.");
			} else {
				Player p = (Player) sender;
				Block b = p.getTargetBlock(null, 5);
				if (b.getType() == Material.CHEST) {
					if (gimmicks.magicChests.get(b.getLocation()) == null) {
						p.sendMessage("[Gimmicks] Chest converted to Magic Chest");
						gimmicks.hungercompass.setChest(b.getLocation());
					} else {
						p.sendMessage("[Gimmicks] This is already a Magic Chest");
					}
					
				} else {
					p.sendMessage("[Gimmicks] You need to target a chest.");
				}
				
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("setloot")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("[Gimmicks] Only players can run this command.");
			} else {
				Player p = (Player) sender;
				Block b = p.getTargetBlock(null, 5);
				if (b.getType() == Material.CHEST) {
					if (gimmicks.magicChests.get(b.getLocation()) == null) {
						p.sendMessage("[Gimmicks] Chest content added to loot table.");
						Chest c = (Chest) b.getState();
						gimmicks.hungercompass.saveItemStack(c.getInventory().getContents());
						c.getInventory().clear();
					} else {
						p.sendMessage("[Gimmicks] You can't add a Magic Chest's contents to the loot table, use a normal chest instead.");
					}
					
				} else {
					p.sendMessage("[Gimmicks] You need to target a chest.");
				}
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
		
		if (cmd.getName().equalsIgnoreCase("compasstracker")) {
			gimmicks.compassOn = !gimmicks.compassOn;
			if (gimmicks.muteAll) {
				sender.sendMessage("[Gimmicks] Compass tracking enabled!");
			} else {
				sender.sendMessage("[Gimmicks] Compass tracking!");
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("playintro")) {
			gimmicks.hungercompass.playIntro();
		}
		
		return true;
	}
  
}