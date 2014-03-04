package com.gimmicknetwork.gimmicks.hungergames;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HungerGamesCommandHandler implements CommandExecutor {
	private HungerGames hungerGames;
	
	public HungerGamesCommandHandler(HungerGames hg) {
		hungerGames = hg;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setchest")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("[Gimmicks] Only players can run this command.");
			} else {
				Player p = (Player) sender;
				Block b = p.getTargetBlock(null, 5);
				if (b.getType() == Material.CHEST) {
					if (hungerGames.getChestManager().isMagicChest(b.getLocation())) {
						p.sendMessage("[Gimmicks] This is already a Magic Chest");
					} else {
						p.sendMessage("[Gimmicks] Chest converted to Magic Chest");
						hungerGames.getChestManager().setChest(b.getLocation());
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
					if (hungerGames.getChestManager().isMagicChest(b.getLocation())) {
						p.sendMessage("[Gimmicks] You can't add a Magic Chest's contents to the loot table, use a normal chest instead.");
					} else {
						Chest c = (Chest)b.getState();
						hungerGames.getChestManager().saveItemStack(c.getInventory().getContents());
						c.getInventory().clear();
					}
				} else {
					p.sendMessage("[Gimmicks] You need to target a chest.");
				}
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("compasstracker")) {
			hungerGames.toggleCompassTracking();
			if (hungerGames.isTrackingCompass()) {
				sender.sendMessage("[Gimmicks] Compass tracking enabled!");
			} else {
				sender.sendMessage("[Gimmicks] Compass tracking!");
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("playintro")) {
			hungerGames.playIntro();
		}
		
		return false;
	}
}
