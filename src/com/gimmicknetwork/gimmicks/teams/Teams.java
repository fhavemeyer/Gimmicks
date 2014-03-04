package com.gimmicknetwork.gimmicks.teams;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.gimmicknetwork.gimmicks.GimmickModule;

public class Teams extends GimmickModule {
	private TeamManager teamManager;
	private TeamsCommandHandler commandHandler;
	private boolean resetOnDeath;
	
	public Teams() {
		commandHandler = new TeamsCommandHandler(this);
		teamManager = new TeamManager();
	}
	
	@Override
	public void enable() {
		gimmickPlugin.getCommand("team").setExecutor(commandHandler);
		gimmickPlugin.getCommand("teamtp").setExecutor(commandHandler);
		gimmickPlugin.getCommand("teamsetspawn").setExecutor(commandHandler);
		resetOnDeath = gimmickPlugin.getConfig().getBoolean("teams.resetondeath", false);
		
		gimmickPlugin.getServer().getPluginManager().registerEvents(new TeamsEventHandler(teamManager, resetOnDeath), this.gimmickPlugin);
	}
	
	@Override
	public void reload(CommandSender sender) {
		teamManager.clearAllTeams();
		sender.sendMessage("[" + ChatColor.GREEN + "Gimmicks" + ChatColor.WHITE + "] Teams cleared.");
	}
	
	@Override
	public void disable() {
		teamManager.clearAllTeams();
	}
	
	public TeamManager teamManager() { 
		return teamManager;
	}
}
