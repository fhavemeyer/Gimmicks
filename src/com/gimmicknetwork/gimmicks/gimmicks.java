package com.gimmicknetwork.gimmicks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

public final class gimmicks extends JavaPlugin implements Listener {
	//set up hashmap for teams
	public HashMap<String, ChatColor> teams = new HashMap<String, ChatColor>();
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("[CivpvpInventory] plugin enabled!");
		getCommand("team").setExecutor(new commands(this));
		getCommand("teamtp").setExecutor(new commands(this));
		this.saveDefaultConfig();
		
	}
	
	public void onDisable() {
		getLogger().info("[CivpvpInventory] plugin enabled!");
	}
	
	public void teamTP(ChatColor team, Location loc) {
		/* for ( String player : teams.keySet() ) {
		    if (teams.get(player).equals(team)) {
		    	Bukkit.getPlayer(player).teleport(loc);
		    }
		} */
		Bukkit.broadcast("TELEPORTING " + team.toString(), "gimmicks.teams.admin");
		Player[] onlinePlayers = getServer().getOnlinePlayers();
	    for (Player player : onlinePlayers){
	    	String playerName = player.getName();
	    	if (teams.get(playerName).equals(team)) {
	    		Bukkit.broadcast("TELEPORTING " + playerName, "gimmicks.teams.admin");
	    		player.teleport(loc);
	    	}
	    }
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
		String name = event.getNamedPlayer().getName().toString();
		ChatColor cc = teams.get(name);
		if (cc != null) {
			event.setTag(cc + name);
		} else {
			event.setTag(name);
		}
	}
	
	
}