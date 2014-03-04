package com.gimmicknetwork.gimmicks;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Utilities {
	
	public static void spreadPlayer(World world, Player player, int x, int z, int radius) {
		Location loc = new Location(world, (double) x, 0d, (double)z);
		Location l = getSafe(world, randomLocation(world, loc, radius));
		while (l == null) {
			l = getSafe(world, randomLocation(world, loc, radius));
		}
		player.teleport(l);
	}
	
	public static Location randomLocation(World world, Location start, int radius) {
		Location output = start;
		int x = nextInt(0, radius);
		int secondMax = (int)Math.sqrt(Math.pow(radius, 2) - Math.pow(x, 2));
		int z = nextInt(0, secondMax);
		
		output.setX((double)x + 0.5);
		output.setZ((double)z + 0.5);
		
		if (nextInt(0, 100) < 50) {
			output.setX(-output.getX());
		}
		if (nextInt(0, 100) < 50) {
			output.setZ(-output.getZ());
		}
		
		return output;
	}
	
	public static int nextInt(int min, int max) {
		Random rnd = new Random();
		return rnd.nextInt(max - min) + min;
	}
	
	public static Location getSafe(World world, Location loc) {
		if (loc != null) {
			double topBlock = world.getHighestBlockYAt(loc);
			loc.setY(topBlock);
			if (loc.getBlock().getRelative(BlockFace.DOWN).getType().isOccluding()) {
				loc.add(0d, 1d, 0d);
				if (loc.getBlock().getRelative(BlockFace.UP).isEmpty()) {
					return loc;
				}
			}
		}
		
		return null;
	}
}
