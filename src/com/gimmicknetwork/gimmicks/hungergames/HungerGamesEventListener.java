package com.gimmicknetwork.gimmicks.hungergames;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gimmicknetwork.gimmicks.Utilities;

public class HungerGamesEventListener implements Listener {
	private HungerGames hungerGames;
	private HashMap<String, Integer> compassLastUse = new HashMap<String, Integer>();
	
	private double compassRange;
	private boolean compassShowsPlayer;
	
	public HungerGamesEventListener(HungerGames hg, double compassRange, boolean compassShowsPlayer) {
		hungerGames = hg;
		this.compassRange = compassRange;
		this.compassShowsPlayer = compassShowsPlayer;
	}
	
	//Right click from compass
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (hungerGames.isTrackingCompass()) {
			if (p.getItemInHand().getType().equals(Material.COMPASS) && event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				if(compassLastUse.containsKey(p.getName())) {
					int lastUse = compassLastUse.get(p.getName().toString());
					if(lastUse < 30) {
						int timeLeft = 30 - lastUse;
						p.sendMessage(ChatColor.RED + "You need to wait " + ChatColor.WHITE + Integer.toString(timeLeft) + " seconds before you can track again");
						event.setCancelled(true);
						return;
					}
				}
				Player target = null;
				target = Utilities.getNearest(p, compassRange);
				if(target != null) {
					p.setCompassTarget(target.getLocation());
					if (compassShowsPlayer) {
						p.sendMessage(ChatColor.GREEN + "Tracking " + target.getDisplayName().toString());
					} else {
						p.sendMessage(ChatColor.GREEN + "Tracking player...");
					}
				} else {
					p.sendMessage(ChatColor.RED + "There is nobody in range.");
				}
				compassLastUse.put(event.getPlayer().getName().toString(), 0);
				event.setCancelled(true);
			}
		}		
	}
}
