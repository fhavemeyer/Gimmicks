package com.gimmicknetwork.gimmicks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

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
}
