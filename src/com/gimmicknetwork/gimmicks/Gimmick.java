package com.gimmicknetwork.gimmicks;

import org.bukkit.plugin.java.JavaPlugin;

import com.gimmicknetwork.gimmicks.GimmickEventListener;
import com.gimmicknetwork.gimmicks.teams.Teams;
import com.gimmicknetwork.gimmicks.teams.TeamsCommandHandler;

public final class Gimmick extends JavaPlugin {
	public boolean muteAll = false;
	public boolean compassOn = getConfig().getBoolean("hungercompass.enabled", false);
	public boolean introPlaying = false;
	
	GimmickEventListener eventListener = new GimmickEventListener(this);
	
	public void onEnable() {
		CommandsHandler commandHandler = new CommandsHandler(this);
		TeamsCommandHandler teamHandler = new TeamsCommandHandler(this);
		
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(eventListener, this);
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
	}
	
	public void onDisable() {
		getLogger().info("[Gimmicks] plugin enabled!");
		Teams.disable();
	}
}