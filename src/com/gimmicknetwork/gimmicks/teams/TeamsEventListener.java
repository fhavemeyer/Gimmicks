package com.gimmicknetwork.gimmicks.teams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

public class TeamsEventListener implements Listener {
	private TeamManager teamManager;
	private boolean resetOnDeath;
	
	public TeamsEventListener(TeamManager t, boolean resetOnDeath) {
		this.teamManager = t;
		this.resetOnDeath = resetOnDeath;
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
		String playerName = event.getNamedPlayer().getName();
		ChatColor cc = teamManager.getTeamColor(playerName);
		if (cc != null) {
			event.setTag(cc + playerName);
		} else {
			event.setTag(playerName);
		}
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (resetOnDeath) {
				teamManager.removeFromTeam(p);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		if (teamManager.isInTeam(p)) {
			ChatColor teamColor = teamManager.getTeamColor(p);
			if (teamManager.hasTeamSpawn(teamColor)) {
				event.setRespawnLocation(teamManager.getTeamSpawn(teamColor));
			}
		}
	}
}
