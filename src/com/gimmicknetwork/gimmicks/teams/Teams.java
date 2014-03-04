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
	
	public static Teams teamManager() {
		if (instance == null) {
			instance = new Teams();
		}
		return instance;
	}
	
	public static void disable() {
		instance.teams.clear();
		instance.teamSpawn.clear();
		instance = null;
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
	
	public void removeFromTeam(Player p) {
		teams.remove(p.getName());
		TagAPI.refreshPlayer(p);
	}
	
	public void leaveTeam(Player p) {
		removeFromTeam(p);
		p.sendMessage("[TEAMS] Left team.");
	}
	
	public void clearAllTeams() {
		this.teams.clear();
		for (Player p : Bukkit.getOnlinePlayers()) {
			TagAPI.refreshPlayer(p);
		}
	}
	
	public void teamSetSpawn (ChatColor t, Location loc) {
		teamSpawn.put(t, loc);
	}
	
	public boolean hasTeamSpawn(ChatColor teamColor) {
		return this.teamSpawn.containsKey(teamColor);
	}
	
	public Location getTeamSpawn(ChatColor teamColor) {
		return this.teamSpawn.get(teamColor);
	}
	
	public ChatColor getTeamColor(Player p) {
		return this.teams.get(p.getName());
	}
	
	public ChatColor getTeamColor(String playerName) {
		return this.teams.get(playerName);
	}
	
	public boolean isInTeam(Player p) {
		return this.teams.containsKey(p.getName());
	}
	
	public boolean isInTeam(String playerName) {
		return this.teams.containsKey(playerName);
	}
}
