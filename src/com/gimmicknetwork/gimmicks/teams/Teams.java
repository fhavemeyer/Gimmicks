package com.gimmicknetwork.gimmicks.teams;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class Teams {
	
	private static Teams instance = null;

	public HashMap<String, ChatColor> teams = new HashMap<String, ChatColor>();
	public HashMap<ChatColor, Location> teamSpawn = new HashMap<ChatColor, Location>();
	
	protected Teams() { }
	
	public static Teams getInstance() {
		if (instance == null) {
			instance = new Teams();
		}
		return instance;
	}
	
	public void setTeam(Player p, String teamName, ChatColor teamColor) {
		if (teamColor != null) {
			teams.put(p.getName(), teamColor);
			p.sendMessage("[TEAMS] Joined team: " + teamColor + teamName.toUpperCase());
			TagAPI.refreshPlayer(p);
		} else {
			p.sendMessage("[TEAMS] Invalid team!");
		}
	}
	
	public void leaveTeam(Player p) {
		teams.remove(p.getName());
		p.sendMessage("[TEAMS] Left team.");
	}

	public void teamTP (ChatColor t, Location loc) {
		for (Entry<String, ChatColor> entry : teams.entrySet())
		{
		    if (entry.getValue().equals(t)) {
		    	Player p = Bukkit.getPlayer(entry.getKey());
		    	if (p != null) {
		    		p.teleport(loc);
		    	}
		    }
		}
	}
	
	public void teamSetSpawn (ChatColor t, Location loc) {
		teamSpawn.put(t, loc);
	}
	
}
