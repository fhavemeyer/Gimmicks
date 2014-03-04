package com.gimmicknetwork.gimmicks.hungergames;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

import com.gimmicknetwork.gimmicks.*;

public class HungerGames {
	Gimmick gimmickPlugin;
	HungerCompass hungerCompass;
	
	public int chestDelay; // = this.getConfig().getInt("magicchests.respawn");
	
	public HashMap<Location, Integer> magicChests = new HashMap<Location, Integer>();
	
	public HungerGames(Gimmick gimmick) {
		gimmickPlugin = gimmick;
		hungerCompass = new HungerCompass(gimmick);
		chestDelay = gimmickPlugin.getConfig().getInt("magicchests.respawn");
		
		// Populate magic chests hashmap from saved chests
		if(gimmickPlugin.getConfig().getBoolean("magicchests.enabled", false)) {
			hungerCompass.loadChests();
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(gimmickPlugin, new Runnable() {
			@Override
			public void run() {
				for (Entry<Location, Integer> entry : magicChests.entrySet()) {
					if (entry.getValue() >= chestDelay) {
						if (entry.getKey().getBlock().getType() == Material.CHEST) {
							Chest c = (Chest)entry.getKey().getBlock().getState();
							c.getInventory().setContents(hungerCompass.randomItemStack());
						} else {
							hungerCompass.remakeChest(entry.getKey());
							Chest c = (Chest)entry.getKey().getBlock().getState();
							c.getInventory().setContents(hungerCompass.randomItemStack());
						}
						entry.setValue(0);
					} else {
						entry.setValue(entry.getValue() + 1);
					}
				}
			}
		}, 0, 20);
	}
}
