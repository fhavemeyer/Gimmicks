package com.gimmicknetwork.gimmicks.hungergames;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.gimmicknetwork.gimmicks.Gimmick;

public class HungerCompass {

	private Gimmick gimmickPlugin;
	private FileConfiguration configManager;
	private HashMap<Location, Integer> magicChests = new HashMap<Location, Integer>();
	
	
	public HungerCompass(Gimmick gimmicks) {
		this.gimmickPlugin = gimmicks;
		
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
