package com.gimmicknetwork.gimmicks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import com.gimmicknetwork.gimmicks.Gimmick;

public class GimmickEventListener implements Listener {
	
	private Gimmick gimmickPlugin; // pointer to your main class, unrequired if you don't need methods from the main class
	 
	public GimmickEventListener(Gimmick gimmicks) {
		this.gimmickPlugin = gimmicks;
	}
	
	//load player faces into memory on join
	@EventHandler
	public void onAsyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
		gimmickPlugin.faceCacheManager().cacheFace(event.getName());
	}
	
	/*
	 * Player mute	
	 */
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		if (gimmickPlugin.muteAll) {
			if (!event.getPlayer().hasPermission("gimmicks.admin")) {
				event.setCancelled(true);
			}
		}
	}
	
}
