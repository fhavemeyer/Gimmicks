package com.gimmicknetwork.gimmicks.teams;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

import com.gimmicknetwork.gimmicks.Gimmick;

public class Teams {
	
	private Gimmick gimmickPlugin;
	
	public Teams(Gimmick gimmicks) {
		this.gimmickPlugin = gimmicks;
	}
	
	public void setTeam(Player p, String teamName, ChatColor teamColor) {
		if (teamColor != null) {
			this.gimmickPlugin.teams.put(p.getName(), teamColor);
			p.sendMessage("[TEAMS] Joined team: " + teamColor + teamName.toUpperCase());
			TagAPI.refreshPlayer(p);
		} else {
			p.sendMessage("[TEAMS] Invalid team!");
		}
	}
	
	public void leaveTeam(Player p) {
		this.gimmickPlugin.teams.remove(p.getName());
		p.sendMessage("[TEAMS] Left team.");
	}

	public void teamTP (ChatColor t, Location loc) {
		for (Entry<String, ChatColor> entry : this.gimmickPlugin.teams.entrySet())
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
		this.gimmickPlugin.teamSpawn.put(t, loc);
	}
	
}
