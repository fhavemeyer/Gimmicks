package com.gimmicknetwork.gimmicks.killstreaks;

import java.awt.image.BufferedImage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.gimmicknetwork.gimmicks.Gimmick;

public class KillStreaksEventListener implements Listener {
	private KillStreaksManager killStreakManager;
	private Gimmick gimmickPlugin;
	private boolean faceMessagesEnabled;
	
	public KillStreaksEventListener(KillStreaksManager manager, Gimmick gimmickPlugin, boolean faceMessagesEnabled) { 
		this.killStreakManager = manager;
		this.gimmickPlugin = gimmickPlugin;
		this.faceMessagesEnabled = faceMessagesEnabled;
	}
	//show death message
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			
			//if (gimmicks.getConfig().getBoolean("facedeathmessages.enabled", true)) {
			if (faceMessagesEnabled) {
				//THUNDER STRUCK, JUST LOOK AT THE MESS YOU MADE
				p.getWorld().strikeLightningEffect(p.getLocation().add(0, 100, 0));
				
				//start making our fancy death message
				if (gimmickPlugin.faceCacheManager().hasFaceCached(p)){
					BufferedImage imageToSend = gimmickPlugin.faceCacheManager().getCachedFace(p);
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
}
