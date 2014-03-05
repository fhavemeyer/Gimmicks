package com.gimmicknetwork.gimmicks.killstreaks;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class KillStreaksManager {
	public HashMap<String, Integer> killStreaks = new HashMap<String, Integer>();
	
	public KillStreaksManager() { }
	
	public void incrementKillStreak(String playerName) {
		if (!killStreaks.containsKey(playerName)) {
			killStreaks.put(playerName, 1);
		} else {
			killStreaks.put(playerName, killStreaks.get(playerName) + 1);
		}
	}
	
	public int getKillStreak(String playerName) {
		return killStreaks.get(playerName);
	}
	
	public int getKillStreak(Player p) {
		return killStreaks.get(p.getName());
	}
	
	public void incrementKillStreak(Player p) {
		incrementKillStreak(p.getName());
	}
	
	public void clearKillStreakForPlayer(String playerName) {
		killStreaks.remove(playerName);
	}
	
	public void clearKillStreakForPlayer(Player p) {
		clearKillStreakForPlayer(p.getName());
	}
	
	public void clearAllKillStreaks() {
		killStreaks.clear();
	}
	
	public boolean hasKillStreak(String playerName) {
		return killStreaks.containsKey(playerName);
	}
	
	public boolean hasKillStreak(Player p) {
		return hasKillStreak(p.getName());
	}
}
