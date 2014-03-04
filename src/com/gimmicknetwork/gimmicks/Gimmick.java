package com.gimmicknetwork.gimmicks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.plugin.java.JavaPlugin;

import com.gimmicknetwork.gimmicks.GimmickEventListener;
import com.gimmicknetwork.gimmicks.hungergames.HungerGames;
import com.gimmicknetwork.gimmicks.hungergames.HungerGamesCommandHandler;
import com.gimmicknetwork.gimmicks.teams.Teams;
import com.gimmicknetwork.gimmicks.teams.TeamsCommandHandler;

public final class Gimmick extends JavaPlugin {
	public boolean muteAll = false;
	public boolean introPlaying = false;
	
	GimmickEventListener eventListener = new GimmickEventListener(this);
	HungerGames hungerGames;
	
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
		getCommand("muteall").setExecutor(commandHandler);
		getCommand("healall").setExecutor(commandHandler);
		
		if (getConfig().getBoolean("hungergames.enabled", false)) {
			hungerGames = new HungerGames(this);
		}
	}
	
	public void onDisable() {
		getLogger().info("[Gimmicks] plugin enabled!");
		Teams.disable();
	}
	
	public BufferedImage loadImage(String url) {
		BufferedImage imageToSend;			
		try {
			imageToSend = ImageIO.read(getResource(url));
		} catch (IOException e) {
			getLogger().warning("Unable to read image, play intro stopped.");
			e.printStackTrace();
			return null;
		}
		return imageToSend;
	}
}