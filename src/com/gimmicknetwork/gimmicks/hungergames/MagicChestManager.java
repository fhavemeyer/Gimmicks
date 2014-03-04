package com.gimmicknetwork.gimmicks.hungergames;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class MagicChestManager {
	private HashMap<Location, Integer> magicChests = new HashMap<Location, Integer>();
	private File pluginDataFolder;
	private Logger pluginLogger;
	private int magicChestRespawn;
	
	public MagicChestManager(File dataFolder, Logger logger, int respawn) {
		pluginDataFolder = dataFolder;
		pluginLogger = logger;
		magicChestRespawn = respawn;
	}
	
	public void remakeChest(Location loc) {
		if(!loc.getBlock().getType().equals(Material.CHEST)) {
			loc.getBlock().setType(Material.CHEST);
		}
		Chest c = (Chest) loc.getBlock().getState();
		c.getBlockInventory().setContents(randomItemStack());
	}
	
	public boolean isMagicChest(Location loc) {
		return magicChests.containsKey(loc);
	}
	
	@SuppressWarnings("unchecked")
	public void setChest(Location loc) {
		magicChests.put(loc, magicChestRespawn);
		YamlConfiguration c = new YamlConfiguration();
		//load any current chests from MagicChests.yml
		try {
			c.load(pluginDataFolder + "/MagicChests.yml");
		} catch (IOException e) {
			e.printStackTrace();
			
			//save empty config file for use
	        try {
				c.save(new File(pluginDataFolder + "/MagicChests.yml"));
				pluginLogger.info("[Gimmicks] New MagicChests.yml generated.");
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			pluginLogger.info("[Gimmicks] MagicChests.yml corrupt, saving new file.");
			File f = new File(pluginDataFolder + "/MagicChests.yml");
			f.renameTo(new File(pluginDataFolder + "/MagicChests.yml.broken"));
		}
		
		//turn saved locations into a list and add the new loc
		List<String> locs = new ArrayList<String>();
		
		if ((List<String[]>) c.getList("chests") != null) {
			locs.addAll((List<String>) c.getList("chests"));
		}
		if (loc != null) {
			locs.add(serializeLoc(loc));
			c.set("chests", locs);
		}
		
		//save config
        try {
			c.save(new File(pluginDataFolder + "/MagicChests.yml"));
			pluginLogger.info("[Gimmicks] Chest saved.");
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}
	
	@SuppressWarnings("unchecked")
	public void loadChests() {
		YamlConfiguration c = new YamlConfiguration();
		//load any current chests from MagicChests.yml
		try {
			c.load(pluginDataFolder + "/MagicChests.yml");
		} catch (IOException e) {
			e.printStackTrace();
			//save empty config file for use
	        try {
				c.save(new File(pluginDataFolder + "/MagicChests.yml"));
				pluginLogger.info("[Gimmicks] New MagicChests.yml generated.");
			} catch (IOException e2) {
				e2.printStackTrace();
			}	
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			pluginLogger.info("[Gimmicks] MagicChests.yml corrupt, saving new file.");
			File f = new File(pluginDataFolder + "/MagicChests.yml");
			f.renameTo(new File(pluginDataFolder + "/MagicChests.yml.broken"));
		}
		
		List<String> locs = new ArrayList<String>();
		
		if ((List<String[]>) c.getList("chests") != null) {
			locs.addAll((List<String>) c.getList("chests"));
		}

		//take each loc from config and add it to hashmap
		for (String loc : locs) {
			Location l = deserializeLoc(loc);
			magicChests.put(l, magicChestRespawn);
		}
		pluginLogger.info("[Gimmicks] MagicChests.yml loaded");
	}
	
	public Set<Entry<Location, Integer>> getMagicChestsAsSet() {
		return magicChests.entrySet();
	}
	
	public ItemStack[] randomItemStack() {
		String path = pluginDataFolder + "/Loot/";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Random r = new Random();
		ItemStack[] i = loadItemStack(listOfFiles[r.nextInt(listOfFiles.length)]);
		return i;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ItemStack[] loadItemStack(File file) {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
		ItemStack[] content = (ItemStack[])((List)c.get("inventory.content")).toArray(new ItemStack[0]);
		return content;
	}
	
	public void saveItemStack(ItemStack[] inv) {
		String path = pluginDataFolder + "/Loot/";
		String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), "yml");
        YamlConfiguration c = new YamlConfiguration();
        c.set("inventory.content", inv);
        try {
			c.save(new File(path, name));
		} catch (IOException e) {
			e.printStackTrace();
		}
        pluginLogger.info("[Gimmicks] Magic chest contents saved to " + name);
	}
	
	public String serializeLoc(Location loc){
	    return loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ();
	}
	 
	public Location deserializeLoc(String str){
	    String[] loc = str.split(",");
	    return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
	}
}
