package com.gimmicknetwork.gimmicks;

import java.util.HashMap;

import org.bukkit.ChatColor;
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
		this.saveDefaultConfig();
		
	}
	
	public void onDisable() {
		getLogger().info("[CivpvpInventory] plugin enabled!");
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
		String name = event.getNamedPlayer().getName().toString();
		ChatColor cc = teams.get(name);
		getLogger().info("NAMETAG EVENT CALLED");
		if (cc != null) {
			event.setTag(cc + name);
		} else {
			event.setTag(name);
		}
	}
	
}