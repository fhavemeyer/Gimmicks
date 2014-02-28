package com.gimmicknetwork.gimmicks;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
	 
	 
	 
}
