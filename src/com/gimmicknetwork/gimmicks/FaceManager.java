package com.gimmicknetwork.gimmicks;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class FaceManager {
	private Gimmick gimmickPlugin;
	
	private HashMap<String, BufferedImage> faceCache = new HashMap<String, BufferedImage>();
	
	public FaceManager(Gimmick plugin) {
		gimmickPlugin = plugin;
	}
	
	public void cacheFace(String playerName) {
		BufferedImage faceImage = null;
		
		// Don't even try to cache if there's something wrong with the player name
		if (playerName == null || playerName.isEmpty()) {
			return;
		}
		
		try {
			URL imageResourcePath = new URL("https://minotar.net/avatar/" + playerName + "/8.png");
			faceImage = ImageIO.read(imageResourcePath);
		} catch (IOException e) {
			gimmickPlugin.getLogger().info("[Gimmicks] Failed to get skin from: " + "https://minotar.net/avatar/" + playerName + "/8.png, using default instead.");
			try {
				faceImage = ImageIO.read(this.gimmickPlugin.getResource("_default.png"));
			} catch (IOException e2) {
				gimmickPlugin.getLogger().info("[Gimmicks] Failed to load default face.");
			}
		}
		if (faceImage != null) {
			faceCache.put(playerName, faceImage);
		}
	}
	
	public boolean hasFaceCached(String playerName) {
		return faceCache.containsKey(playerName);
	}
	
	public boolean hasFaceCached(Player p) {
		return faceCache.containsKey(p.getName());
	}
	
	public BufferedImage getCachedFace(String playerName) {
		return faceCache.get(playerName);
	}
	
	public BufferedImage getCachedFace(Player p) {
		return faceCache.get(p.getName());
	}
}
