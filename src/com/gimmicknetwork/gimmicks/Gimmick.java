package com.gimmicknetwork.gimmicks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.gimmicknetwork.gimmicks.GimmickEventListener;
import com.gimmicknetwork.gimmicks.hungergames.HungerGames;
import com.gimmicknetwork.gimmicks.teams.Teams;

public final class Gimmick extends JavaPlugin {
	public boolean muteAll = false;
	public boolean introPlaying = false;
	
	private ArrayList<GimmickModule> gimmickModules = new ArrayList<GimmickModule>();

	public void onEnable() {
		GimmickCommandsHandler commandHandler = new GimmickCommandsHandler(this);
		
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(new GimmickEventListener(this), this);
		getLogger().info("[Gimmicks] plugin enabled!");
		// BEGIN GENERAL GIMMICK COMMANDS
		getCommand("gimmicks").setExecutor(commandHandler);
		getCommand("spreadall").setExecutor(commandHandler);
		getCommand("muteall").setExecutor(commandHandler);
		getCommand("healall").setExecutor(commandHandler);
		// END GENERAL GIMMICK COMMANDS
		
		if (this.getConfig().getBoolean("teams.enabled", false)) {
			gimmickModules.add(new Teams());
		}
		
		if (getConfig().getBoolean("hungergames.enabled", false)) {
			gimmickModules.add(new HungerGames());
		}
		
		this.enableGimmickModules();
	}
	
	private void enableGimmickModules() {
		for (GimmickModule g : gimmickModules) {
			g.setPluginOwner(this);
			g.enable();
		}
	}
	
	public void requestReloadEnabledModules(CommandSender sender) {
		
	}
	
	private void disableGimmickModules() {
		for (GimmickModule g : gimmickModules) {
			g.disable();
		}
	}
	
	public void onDisable() {
		getLogger().info("[Gimmicks] plugin disabled!");
		this.disableGimmickModules();
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