package com.gimmicknetwork.gimmicks;

import java.awt.Image;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.gimmicknetwork.gimmicks.teams;
import com.gimmicknetwork.gimmicks.listener;

public final class gimmicks extends JavaPlugin {
	
	//Hashmap for teams <player name><team ChatColor>
	public HashMap<String, ChatColor> teams = new HashMap<String, ChatColor>();
	public HashMap<ChatColor, Location> teamSpawn = new HashMap<ChatColor, Location>();
	public HashMap<String, Image> faceCache= new HashMap<String, Image>();
	public HashMap<String, Integer> killStreak= new HashMap<String, Integer>();
	
	
	listener listener = new listener(this);
	hungercompass hungercompass = new hungercompass(this);
	
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(listener, this);
		getLogger().info("[Gimmicks] plugin enabled!");
		getCommand("team").setExecutor(new teams(this));
		getCommand("teamtp").setExecutor(new teams(this));
		getCommand("teamsetspawn").setExecutor(new teams(this));
		getCommand("gimmicks").setExecutor(new commands(this));
		getCommand("spreadall").setExecutor(new commands(this));
		
		this.saveDefaultConfig();
		
	}
	
	public void onDisable() {
		getLogger().info("[Gimmicks] plugin enabled!");
	}
	
	public void addFaceCache(String playerName, Image i) {
		faceCache.put(playerName, i);
	}
	
}