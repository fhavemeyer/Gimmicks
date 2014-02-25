package com.gimmicknetwork.gimmicks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class hungercompass {

	@SuppressWarnings("unused")
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
}
