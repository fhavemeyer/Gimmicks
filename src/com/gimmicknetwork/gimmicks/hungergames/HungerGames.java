package com.gimmicknetwork.gimmicks.hungergames;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.gimmicknetwork.gimmicks.*;

public class HungerGames {
	private Gimmick gimmickPlugin;
	private MagicChestManager magicChestsManager;
	
	public int chestDelay;
	private boolean compassOn;
	private double compassRange;
	private boolean showPlayerOnCompass;
	
	public HashMap<Location, Integer> magicChests = new HashMap<Location, Integer>();
	
	public HungerGames(Gimmick gimmick) {
		gimmickPlugin = gimmick;
		
		chestDelay = gimmickPlugin.getConfig().getInt("magicchests.respawn");
		compassOn = gimmickPlugin.getConfig().getBoolean("hungercompass.enabled", false);
		compassRange = gimmickPlugin.getConfig().getDouble("hungercompass.range", 500d);
		showPlayerOnCompass = gimmickPlugin.getConfig().getBoolean("hungercompass.showplayer", false);
		
		magicChestsManager = new MagicChestManager(gimmickPlugin.getDataFolder(), gimmickPlugin.getLogger(), chestDelay);
		
		HungerGamesCommandHandler hgCommandHandler = new HungerGamesCommandHandler(this);
		gimmickPlugin.getCommand("setchest").setExecutor(hgCommandHandler);
		gimmickPlugin.getCommand("setloot").setExecutor(hgCommandHandler);
		gimmickPlugin.getCommand("playintro").setExecutor(hgCommandHandler);
		gimmickPlugin.getCommand("compasstracker").setExecutor(hgCommandHandler);
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(gimmickPlugin, new Runnable() {
			@Override
			public void run() {
				MagicChestManager chestManager = getChestManager();
				for (Entry<Location, Integer> entry : magicChests.entrySet()) {
					if (entry.getValue() >= chestDelay) {
						if (entry.getKey().getBlock().getType() == Material.CHEST) {
							Chest c = (Chest)entry.getKey().getBlock().getState();
							c.getInventory().setContents(chestManager.randomItemStack());
						} else {
							chestManager.remakeChest(entry.getKey());
							Chest c = (Chest)entry.getKey().getBlock().getState();
							c.getInventory().setContents(chestManager.randomItemStack());
						}
						entry.setValue(0);
					} else {
						entry.setValue(entry.getValue() + 1);
					}
				}
			}
		}, 0, 20);
	}
	
	public MagicChestManager getChestManager() {
		return magicChestsManager;
	}
	
	public void toggleCompassTracking() {
		compassOn = !compassOn;
	}
	
	public boolean isTrackingCompass() {
		return compassOn;
	}
	
	public void playIntro() {

		int playerCount = 0;
		for (Player p: Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("gimmicks.admin") && !p.isOp()) {
				playerCount = playerCount + 1;
			}
		}
		
		int baseDelay = 10*20;
		int delay = 0;
		
		BufferedImage imageToSend;
		
		imageToSend = gimmickPlugin.loadImage("images/hunger.png");
		new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
				 "",
				 ChatColor.BOLD + "Welcome to the CivPVP Hunger Games!",
				 "",
				 "There are " + Integer.toString(playerCount) + " players competing.",
				 "You have all been randomly spread around",
				 "the world."
				 ).sendToPlayers(Bukkit.getOnlinePlayers());
		
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				
				BufferedImage imageToSend = gimmickPlugin.loadImage("images/sword.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "It's a fight to the death.",
						 "",
						 ChatColor.RED + "PVP will be turned on in 5 minutes.",
						 "There are chests scattered around the map,",
						 "they contain random loot that might help you."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmickPlugin, delay);
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				
				BufferedImage imageToSend = gimmickPlugin.loadImage("images/compass.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "Compass Tracking",
						 "",
						 ChatColor.RED + "Enabled after 30 minutes.",
						 "You can right click with a compass to",
						 "track players. They can be found in chests."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmickPlugin, delay);
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				
				BufferedImage imageToSend = gimmickPlugin.loadImage("images/zombieface.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "You can run, but you can't hide!",
						 ChatColor.RED + "After an hour, the world border will shrink.",
						 "It will shrink further every 5 minutes.",
						 "Armed " + ChatColor.GREEN + "Zombies" + ChatColor.WHITE + " will spawn if you stay in one place",
						 "for too long."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmickPlugin, delay);
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				
				BufferedImage imageToSend = gimmickPlugin.loadImage("images/skeletonface.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "Once you're dead, you're out!",
						 "",
						 ChatColor.RED + "This server has a deathban.",
						 "Happy Hunger Games!",
						 "May the odds be ever in your favour."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmickPlugin, delay);
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				BufferedImage imageToSend = gimmickPlugin.loadImage("images/smile.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "This event is brought to you by " + ChatColor.GOLD + "CivPVP" + ChatColor.WHITE + "!",
						 "",
						 ChatColor.RED + "We're raising funds for a better server.",
						 "If you can help by donating please go to",
						 ChatColor.BOLD + "/r/civpvp!" + ChatColor.RESET + " Thankyou," + ChatColor.GREEN + " GLHF."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmickPlugin, delay);
		
		
	}
}
