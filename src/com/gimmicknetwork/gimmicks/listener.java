package com.gimmicknetwork.gimmicks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

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
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (gimmicks.compassOn) {
			if (event.getPlayer().getItemInHand().getType().equals(Material.COMPASS) && event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				if(gimmicks.compassLastUse.get(event.getPlayer().getName().toString()) != null) {
					int lastUse = gimmicks.compassLastUse.get(event.getPlayer().getName().toString());
					if(lastUse < 30) {
						int timeLeft = 30 - lastUse;
						event.getPlayer().sendMessage(ChatColor.RED + "You need to wait " + ChatColor.WHITE + Integer.toString(timeLeft) + " seconds before you can track again");
						event.setCancelled(true);
						return;
					}
				}
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
				gimmicks.compassLastUse.put(event.getPlayer().getName().toString(), 0);
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
	public void onDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			// Teams - check if resetondeath is enabled and make the player leave their team
			if (gimmicks.getConfig().getBoolean("teams.resetondeath")) {
				if (gimmicks.teams.get(p.getName().toString()) != null) {
					gimmicks.teams.remove(p.getName().toString());
					TagAPI.refreshPlayer(p);
				}
			}
			
			//THUNDER STRUCK, JUST LOOK AT THE MESS YOU MADE
			p.getWorld().strikeLightningEffect(p.getLocation().add(0, 100, 0));
			if (gimmicks.faceCache.get(p.getName().toString()) != null){
				BufferedImage imageToSend;			
				imageToSend = (BufferedImage) gimmicks.faceCache.get(p.getName().toString());
				String slainMsg = p.getName().toString() + " was slain.";
				String killerMsg = "";
				String extraMsg = "Good night, sweet prince.";
				
				//slain messages
				if (p.getKiller() != null && p.getKiller() instanceof Player) {
					slainMsg = p.getName().toString() + " was slain by " + p.getKiller().getName() + ".";
					Player killer = (Player) p.getKiller();
					
					//kill streak of killer
					if ( gimmicks.killStreak.get(p.getKiller().getName()) != null) {
						int killerKillStreak = gimmicks.killStreak.get(killer.getName()) + 1;
						gimmicks.killStreak.put(killer.getName(), killerKillStreak);
						killerMsg = killer.getName() + " has a killstreak of " + Integer.toString(killerKillStreak);
						if (killerKillStreak >= 5) { extraMsg = killer.getName() + " is " + ChatColor.RED + "DOMINATING!"; }
						if (killerKillStreak >= 10) { extraMsg = killer.getName() + " is on a " + ChatColor.RED + ChatColor.BOLD + "RAMPAGE!"; }
						if (killerKillStreak >= 15) { extraMsg = killer.getName() + " is " + ChatColor.AQUA + ChatColor.BOLD + "GOD-LIKE!"; }
					} else {
						gimmicks.killStreak.put(killer.getName(), 1);
						killerMsg = killer.getName() + " now has a killstreak of 1";
					}
					
				}
				
				//remove killstreak
				if (gimmicks.killStreak.get(p.getName()) != null) {
					gimmicks.killStreak.remove(p.getName());
				}
				
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 "",
						 "",
						 slainMsg,
						 killerMsg,
						 extraMsg
						 ).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}
	}
	
	//respawn at team spawn if set
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		//check if teams are enabled
		if(!gimmicks.getConfig().getBoolean("teams.enabled")) {
			return;
		}
		if (gimmicks.teams.get(event.getPlayer().getName().toString()) != null) {
			ChatColor t = gimmicks.teams.get(event.getPlayer().getName().toString());
			if (gimmicks.teamSpawn.get(t) != null) {
				Location loc = gimmicks.teamSpawn.get(t);
				event.setRespawnLocation(loc);
			}
		}
	}
	
	
	/*
	 * Player mute	
	 */
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		if (gimmicks.muteAll) {
			if (!event.getPlayer().hasPermission("gimmicks.admin")) {
				event.setCancelled(true);
			}
		}
	}
	
}
