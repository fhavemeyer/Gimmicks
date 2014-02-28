package com.gimmicknetwork.gimmicks;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class hungercompass {

	private gimmicks gimmicks;
	public hungercompass(gimmicks gimmicks) {
		this.gimmicks = gimmicks;
	}
	
	
	public Player getNearest(Player p, Double range) {
		double distance = Double.POSITIVE_INFINITY; // To make sure the first
		// player checked is closest
		Player target = null;
		for (Entity e : p.getNearbyEntities(range, range, range)) {
			if (!(e instanceof Player)) continue;
			if(e.equals(p)) continue; //Added this check so you don't target yourself.
			double distanceto = p.getLocation().distanceSquared(e.getLocation());
			if (distanceto > distance) continue;
			distance = distanceto;
			target = (Player) e;
		}
		return target;
	}
	 	 
	 
	 /*
	  * Spread Players
	  */
	 
	public void spreadPlayer(World world, Player player, int x, int z, int radius) {
		Location loc = new Location(world, (double) x, (double) 0d, z);
		Location l = getSafe(world, randomLocation(world, loc, radius));
		while (l == null) {
			gimmicks.getLogger().info("[Gimmicks] random location for " + player.getName() + " failed safety check, trying again.");
			l = getSafe(world, randomLocation(world, loc, radius));
		}
		player.teleport(l);
	}
	 
	public Location randomLocation(World world, Location start, int radius) {
		Location output = start;
		int x = nextInt(0, radius);
		int secondmax = (radius ^ 2) - (x ^ 2);
		int z = nextInt(0, secondmax);
		if (nextInt(0,100) < 50) {
			output.setX((double) -x-0.5);
		} else {
			output.setX((double) x+0.5);
		}
		if(nextInt(0,100) < 50) {
			output.setZ((double) -z-0.5);
		} else {
			output.setZ((double) z+0.5);
		}
		return output;
	 }
	 
	public int nextInt(int min, int max) {
		    Random rnd = new Random();
		    return rnd.nextInt(max - min) + min;
		}
	 
	public Location getSafe (World world, Location loc) {
		if (loc != null) {
			double topBlock = world.getHighestBlockYAt(loc);
			loc.setY(topBlock);
			if (loc.getBlock().getRelative(BlockFace.DOWN).getType().isOccluding()) {
				loc.add(0d, 1d, 0d);
				if (loc.getBlock().getRelative(BlockFace.UP).isEmpty()){
					return loc;
				}
			}
		}
		return null;	
	 }
	 
	 /*
	  * Magic Chests
	  */
	 
	public void remakeChest(Location loc) {
		if(!loc.getBlock().getType().equals(Material.CHEST)) {
			loc.getBlock().setType(Material.CHEST);
		}
		Chest c = (Chest) loc.getBlock();
		c.getBlockInventory().setContents(randomItemStack());
	}
	
	public ItemStack[] randomItemStack() {
		String path = gimmicks.getDataFolder() + "/MagicChests/";
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
		String path = gimmicks.getDataFolder() + "/MagicChests/";
		String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), ".yml");
        YamlConfiguration c = new YamlConfiguration();
        c.set("inventory.content", inv);
        try {
			c.save(new File(path, name));
		} catch (IOException e) {
			e.printStackTrace();
		}
        gimmicks.getLogger().info("[Gimmicks] Magic chest contents saved to " + name);
	}
}
