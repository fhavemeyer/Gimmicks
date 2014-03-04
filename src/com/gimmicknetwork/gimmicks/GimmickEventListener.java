package com.gimmicknetwork.gimmicks;

import java.awt.image.BufferedImage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.gimmicknetwork.gimmicks.Gimmick;
import com.gimmicknetwork.gimmicks.teams.Teams;

public class GimmickEventListener implements Listener {
	
	private Gimmick gimmicks; // pointer to your main class, unrequired if you don't need methods from the main class
	private FaceManager faceCacheManager;
	private KillStreakManager killStreakManager;
	 
	public GimmickEventListener(Gimmick gimmicks) {
		faceCacheManager = new FaceManager(gimmicks);
		killStreakManager = new KillStreakManager();
		this.gimmicks = gimmicks;
	}
	
	/*
	 * Teams
	 */
	@EventHandler(priority=EventPriority.NORMAL)
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
		String playerName = event.getNamedPlayer().getName();
		ChatColor cc = Teams.teamManager().getTeamColor(playerName);
		if (cc != null) {
			event.setTag(cc + playerName);
		} else {
			event.setTag(playerName);
		}
	}
	


	
	//load player faces into memory on join
	@EventHandler
	public void onAsyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
		faceCacheManager.cacheFace(event.getName());
	}
	
	
	//show death message
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			// Teams - check if resetondeath is enabled and make the player leave their team
			if (gimmicks.getConfig().getBoolean("teams.resetondeath")) {
				Teams.teamManager().removeFromTeam(p);
			}
			
			if (gimmicks.getConfig().getBoolean("facedeathmessages.enabled", true)) {
				//THUNDER STRUCK, JUST LOOK AT THE MESS YOU MADE
				p.getWorld().strikeLightningEffect(p.getLocation().add(0, 100, 0));
				
				//start making our fancy death message
				if (faceCacheManager.hasFaceCached(p)){
					BufferedImage imageToSend = faceCacheManager.getCachedFace(p);
					String slainMsg = p.getName().toString() + " was slain.";
					String killerMsg = "";
					String extraMsg = "Good night, sweet prince.";
					
					//slain messages
					if (p.getKiller() != null && p.getKiller() instanceof Player) {
						slainMsg = p.getName().toString() + " was slain by " + p.getKiller().getName() + ".";
						Player killer = (Player) p.getKiller();
						
						//kill streak of killer
						if (killStreakManager.hasKillStreak(killer)) {
							killStreakManager.incrementKillStreak(killer);
							int killerKillStreak = killStreakManager.getKillStreak(killer);
							killerMsg = killer.getName() + " has a killstreak of " + Integer.toString(killerKillStreak);
							if (killerKillStreak >= 5) { extraMsg = killer.getName() + " is " + ChatColor.RED + "DOMINATING!"; }
							if (killerKillStreak >= 10) { extraMsg = killer.getName() + " is on a " + ChatColor.RED + ChatColor.BOLD + "RAMPAGE!"; }
							if (killerKillStreak >= 15) { extraMsg = killer.getName() + " is " + ChatColor.AQUA + ChatColor.BOLD + "GOD-LIKE!"; }
						} else {
							killStreakManager.incrementKillStreak(killer);
							killerMsg = killer.getName() + " now has a killstreak of 1";
						}
					}
					
					//remove killstreak
					killStreakManager.clearKillStreakForPlayer(p);
					
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
	}
	
	//respawn at team spawn if set
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		//check if teams are enabled
		Player p = event.getPlayer();
		if(!gimmicks.getConfig().getBoolean("teams.enabled")) {
			return;
		}
		if (Teams.teamManager().isInTeam(p)) {
			ChatColor teamColor = Teams.teamManager().getTeamColor(p);
			if (Teams.teamManager().hasTeamSpawn(teamColor)) {
				event.setRespawnLocation(Teams.teamManager().getTeamSpawn(teamColor));
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
