package com.gimmicknetwork.gimmicks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.gimmicknetwork.gimmicks.gimmicks;

public class listener implements Listener {
	
	private gimmicks gimmicks; // pointer to your main class, unrequired if you don't need methods from the main class
	 
	public listener(gimmicks gimmicks) {
		this.gimmicks = gimmicks;
	}
	
	/*
	 * Teams
	 */
	@EventHandler(priority=EventPriority.NORMAL)
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
		String name = event.getNamedPlayer().getName().toString();
		ChatColor cc = gimmicks.teams.get(name);
		if (cc != null) {
			event.setTag(cc + name);
		} else {
			event.setTag(name);
		}
	}
	
	/*
	 * Hunger Compass
	 */
	
	//Right click from compass
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (gimmicks.getConfig().getBoolean("hungercompass.enabled", false)) {
			if (event.getPlayer().getItemInHand().getType().equals(Material.COMPASS) && event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				double range = gimmicks.getConfig().getDouble("hungercompass.range", 500d);
				Player target = null;
				target = gimmicks.hungercompass.getNearest(event.getPlayer(), range);
				if(target != null) {
					event.getPlayer().setCompassTarget(target.getLocation());
					if (gimmicks.getConfig().getBoolean("hungercompass.showplayer", false)) {
						event.getPlayer().sendMessage(ChatColor.GREEN + "Tracking " + target.getDisplayName().toString());
					} else {
						event.getPlayer().sendMessage(ChatColor.GREEN + "Tracking player...");
					}
				} else {
					event.getPlayer().sendMessage(ChatColor.RED + "There is nobody in range.");
				}
				event.setCancelled(true);
			}
		}		
	}

	//death announce
	
	
	//memcache player faces on join
	@EventHandler
	public void onAsyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
		
		String p = event.getName();

		try {
			URL url = new URL("https://minotar.net/avatar/" + p + "/8.png");
		    gimmicks.addFaceCache(p.toString(), ImageIO.read(url));
		} catch (IOException e) {
			gimmicks.getLogger().info("[Gimmicks] Failed to get skin from: " + "https://minotar.net/avatar/" + p + "/8.png, using default instead.");
			try {
				gimmicks.addFaceCache(p.toString(), ImageIO.read(gimmicks.getResource("_default.png")));
			} catch (IOException e2) {
				gimmicks.getLogger().info("[Gimmicks] Failed to load default face.");
				gimmicks.addFaceCache(p.toString(), null);
			}

		}
	}
	
	
	//show death message
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = (Player) event.getEntity();
		if (gimmicks.faceCache.get(p.getName().toString()) != null){
			BufferedImage imageToSend;
			//URL url = new URL("https://minotar.net/avatar/" + p.getName().toString() + "/8.png");
			//imageToSend = ImageIO.read(url);
			
			imageToSend = (BufferedImage) gimmicks.faceCache.get(p.getName().toString());
			
			new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
					 "",
					 "",
					 "",
					 p.getName().toString() + " was slain.",
					 "Good night, sweet prince.",
					 "RIP IN PEICES"
					 ).sendToPlayers(Bukkit.getOnlinePlayers());
		}
	}
	
	
	
}
