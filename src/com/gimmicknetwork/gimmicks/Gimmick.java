package com.gimmicknetwork.gimmicks;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.plugin.java.JavaPlugin;

import com.gimmicknetwork.gimmicks.GimmickEventListener;
import com.gimmicknetwork.gimmicks.teams.Teams;
import com.gimmicknetwork.gimmicks.teams.TeamsCommandHandler;

public final class Gimmick extends JavaPlugin {
	public HashMap<Location, Integer> magicChests = new HashMap<Location, Integer>();
	public boolean muteAll = false;
	public boolean compassOn = getConfig().getBoolean("hungercompass.enabled", false);
	public boolean introPlaying = false;
	
	public int chestDelay = this.getConfig().getInt("magicchests.respawn");
	
	GimmickEventListener listener = new GimmickEventListener(this);
	HungerCompass hungercompass = new HungerCompass(this);
	
	public void onEnable() {
		CommandsHandler commandHandler = new CommandsHandler(this);
		TeamsCommandHandler teamHandler = new TeamsCommandHandler(this);
		
		
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(listener, this);
		getLogger().info("[Gimmicks] plugin enabled!");
		getCommand("team").setExecutor(teamHandler);
		getCommand("teamtp").setExecutor(teamHandler);
		getCommand("teamsetspawn").setExecutor(teamHandler);
		getCommand("gimmicks").setExecutor(commandHandler);
		getCommand("spreadall").setExecutor(commandHandler);
		getCommand("setchest").setExecutor(commandHandler);
		getCommand("setloot").setExecutor(commandHandler);
		getCommand("muteall").setExecutor(commandHandler);
		getCommand("healall").setExecutor(commandHandler);
		getCommand("playintro").setExecutor(commandHandler);
		
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
        			if (entry.getKey().getBlock().getType() == Material.CHEST) {
        				Chest c = (Chest) entry.getKey().getBlock().getState();
        				c.getInventory().setContents(hungercompass.randomItemStack());
        			} else {
        				hungercompass.remakeChest(entry.getKey());
        				Chest c = (Chest) entry.getKey().getBlock().getState();
        				c.getInventory().setContents(hungercompass.randomItemStack());
        			}
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
		Teams.disable();
	}
}