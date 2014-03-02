package com.gimmicknetwork.gimmicks;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map.Entry;

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
	public HashMap<Location, Integer> magicChests = new HashMap<Location, Integer>();
	
	public int chestDelay = this.getConfig().getInt("magicchests.respawn");
	
	listener listener = new listener(this);
	hungercompass hungercompass = new hungercompass(this);
	
	public void onEnable() {
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(listener, this);
		getLogger().info("[Gimmicks] plugin enabled!");
		getCommand("team").setExecutor(new teams(this));
		getCommand("teamtp").setExecutor(new teams(this));
		getCommand("teamsetspawn").setExecutor(new teams(this));
		getCommand("gimmicks").setExecutor(new commands(this));
		getCommand("spreadall").setExecutor(new commands(this));
		getCommand("setchest").setExecutor(new commands(this));
		getCommand("setloot").setExecutor(new commands(this));
		
		//populate magic chests hashmap from saved chests
		this.magicChests.clear();
		if(this.getConfig().getBoolean("magicchests.enabled", false)) {
			hungercompass.loadChests();
		}
				
		
		//start timer for magic chests
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			
        @Override
        public void run() {
        	for (Entry<Location, Integer> entry : magicChests.entrySet()) {
        		if (entry.getValue() >= chestDelay) {
        			getLogger().info("[Gimmicks] A chest would be reloaded.");
        			entry.setValue(0);
        		} else {
        			entry.setValue(entry.getValue() + 1);
        		}
        	}
        }
        }, 0, 20);
	}
	
	public void onDisable() {
		getLogger().info("[Gimmicks] plugin enabled!");
	}
	
	public void addFaceCache(String playerName, Image i) {
		faceCache.put(playerName, i);
	}
	
}