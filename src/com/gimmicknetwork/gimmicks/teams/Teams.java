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
		gimmickPlugin.registerModuleForCommand(this, "team", commandHandler);
		gimmickPlugin.registerModuleForCommand(this, "teamtp", commandHandler);
		gimmickPlugin.registerModuleForCommand(this, "teamsetspawn", commandHandler);
		
		resetOnDeath = gimmickPlugin.getConfig().getBoolean("teams.resetondeath", false);
		
		gimmickPlugin.registerModuleForEvents(this, new TeamsEventListener(teamManager, resetOnDeath));
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
