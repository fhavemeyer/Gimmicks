package com.gimmicknetwork.gimmicks;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.gimmicknetwork.gimmicks.teams;
import com.gimmicknetwork.gimmicks.listener;

public final class gimmicks extends JavaPlugin {
	
	//Hashmap for teams <player name><team ChatColor>
	public HashMap<String, ChatColor> teams = new HashMap<String, ChatColor>();
	
	listener listener = new listener(this);
	hungercompass hungercompass = new hungercompass(this);
	
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(listener, this);
		getLogger().info("[CivpvpInventory] plugin enabled!");
		getCommand("team").setExecutor(new teams(this));
		getCommand("teamtp").setExecutor(new teams(this));
		getCommand("gimmicks").setExecutor(new commands(this));
		getCommand("spreadall").setExecutor(new commands(this));
		
		
		this.saveDefaultConfig();
		
	}
	
	public void onDisable() {
		getLogger().info("[CivpvpInventory] plugin enabled!");
	}
	
	

	
	
}