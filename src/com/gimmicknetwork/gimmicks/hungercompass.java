package com.gimmicknetwork.gimmicks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;

public class HungerCompass {

	private Gimmick gimmicks;
	public HungerCompass(Gimmick gimmicks) {
		this.gimmicks = gimmicks;
	}
	
	
	public Player getNearest(Player p, Double range) {
		double distance = Double.POSITIVE_INFINITY; // To make sure the first
		// player checked is closest
		Player target = null;
		for (Entity e : p.getNearbyEntities(range, range, range)) {
			if (!(e instanceof Player)) continue;
			if(e.equals(p)) continue; //excludes the player
			double distanceto = p.getLocation().distanceSquared(e.getLocation());
			if (distanceto > distance) continue;
			distance = distanceto;
			target = (Player) e;
		}
		return target;
	}
	 	 
	 
	 /*
	  * Spread Players
	  */
	 
	public void spreadPlayer(World world, Player player, int x, int z, int radius) {
		Location loc = new Location(world, (double) x, (double) 0d, z);
		Location l = getSafe(world, randomLocation(world, loc, radius));
		while (l == null) {
			gimmicks.getLogger().info("[Gimmicks] random location for " + player.getName() + " failed safety check, trying again.");
			l = getSafe(world, randomLocation(world, loc, radius));
		}
		player.teleport(l);
	}
	 
	public Location randomLocation(World world, Location start, int radius) {
		Location output = start;
		int x = nextInt(0, radius);
		int secondmax = (radius ^ 2) - (x ^ 2);
		int z = nextInt(0, secondmax);
		if (nextInt(0,100) < 50) {
			output.setX((double) -x-0.5);
		} else {
			output.setX((double) x+0.5);
		}
		if(nextInt(0,100) < 50) {
			output.setZ((double) -z-0.5);
		} else {
			output.setZ((double) z+0.5);
		}
		return output;
	 }
	 
	public int nextInt(int min, int max) {
		    Random rnd = new Random();
		    return rnd.nextInt(max - min) + min;
		}
	 
	public Location getSafe (World world, Location loc) {
		if (loc != null) {
			double topBlock = world.getHighestBlockYAt(loc);
			loc.setY(topBlock);
			if (loc.getBlock().getRelative(BlockFace.DOWN).getType().isOccluding()) {
				loc.add(0d, 1d, 0d);
				if (loc.getBlock().getRelative(BlockFace.UP).isEmpty()){
					return loc;
				}
			}
		}
		return null;	
	 }
	 
	 /*
	  * Magic Chests
	  */
	 
	public void remakeChest(Location loc) {
		if(!loc.getBlock().getType().equals(Material.CHEST)) {
			loc.getBlock().setType(Material.CHEST);
		}
		Chest c = (Chest) loc.getBlock().getState();
		c.getBlockInventory().setContents(randomItemStack());
	}
	
	@SuppressWarnings("unchecked")
	public void setChest(Location loc) {
		int t = gimmicks.getConfig().getInt("magicchests.respawn", 0);
		gimmicks.magicChests.put(loc, t);
		YamlConfiguration c = new YamlConfiguration();
		//load any current chests from MagicChests.yml
		try {
			c.load(gimmicks.getDataFolder() + "/MagicChests.yml");
		} catch (IOException e) {
			e.printStackTrace();
			
			//save empty config file for use
	        try {
				c.save(new File(gimmicks.getDataFolder() + "/MagicChests.yml"));
				gimmicks.getLogger().info("[Gimmicks] New MagicChests.yml generated.");
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			gimmicks.getLogger().info("[Gimmicks] MagicChests.yml corrupt, saving new file.");
			File f = new File(gimmicks.getDataFolder() + "/MagicChests.yml");
			f.renameTo(new File(gimmicks.getDataFolder() + "/MagicChests.yml.broken"));
		}
		
		//turn saved locations into a list and add the new loc
		List<String> locs = new ArrayList<String>();
		
		if ((List<String[]>) c.getList("chests") != null) {
			locs.addAll((List<String>) c.getList("chests"));
		}
		if (loc != null) {
			locs.add(serializeLoc(loc));
			c.set("chests", locs);
		}
		
		//save config
        try {
			c.save(new File(gimmicks.getDataFolder() + "/MagicChests.yml"));
			gimmicks.getLogger().info("[Gimmicks] Chest saved.");
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}
	
	@SuppressWarnings("unchecked")
	public void loadChests() {
		YamlConfiguration c = new YamlConfiguration();
		//load any current chests from MagicChests.yml
		try {
			c.load(gimmicks.getDataFolder() + "/MagicChests.yml");
		} catch (IOException e) {
			e.printStackTrace();
			//save empty config file for use
	        try {
				c.save(new File(gimmicks.getDataFolder() + "/MagicChests.yml"));
				gimmicks.getLogger().info("[Gimmicks] New MagicChests.yml generated.");
			} catch (IOException e2) {
				e2.printStackTrace();
			}	
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			gimmicks.getLogger().info("[Gimmicks] MagicChests.yml corrupt, saving new file.");
			File f = new File(gimmicks.getDataFolder() + "/MagicChests.yml");
			f.renameTo(new File(gimmicks.getDataFolder() + "/MagicChests.yml.broken"));
		}
		
		List<String> locs = new ArrayList<String>();
		
		if ((List<String[]>) c.getList("chests") != null) {
			locs.addAll((List<String>) c.getList("chests"));
		}
		int t = gimmicks.getConfig().getInt("magicchests.respawn", 0);
		//take each loc from config and add it to hashmap
		for (String loc : locs) {
			Location l = deserializeLoc(loc);
			gimmicks.magicChests.put(l, t);
		}
		gimmicks.getLogger().info("[Gimmicks] MagicChests.yml loaded");
	}
	
	public ItemStack[] randomItemStack() {
		String path = gimmicks.getDataFolder() + "/Loot/";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Random r = new Random();
		ItemStack[] i = loadItemStack(listOfFiles[r.nextInt(listOfFiles.length)]);
		return i;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ItemStack[] loadItemStack(File file) {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
		ItemStack[] content = (ItemStack[])((List)c.get("inventory.content")).toArray(new ItemStack[0]);
		return content;
	}
	
	public void saveItemStack(ItemStack[] inv) {
		String path = gimmicks.getDataFolder() + "/Loot/";
		String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), "yml");
        YamlConfiguration c = new YamlConfiguration();
        c.set("inventory.content", inv);
        try {
			c.save(new File(path, name));
		} catch (IOException e) {
			e.printStackTrace();
		}
        gimmicks.getLogger().info("[Gimmicks] Magic chest contents saved to " + name);
	}
	
	public String serializeLoc(Location loc){
	    return loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ();
	}
	 
	public Location deserializeLoc(String str){
	    String[] loc = str.split(",");
	    return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
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
		
		imageToSend = loadImage("images/hunger.png");
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
				
				BufferedImage imageToSend = gimmicks.hungercompass.loadImage("images/sword.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "It's a fight to the death.",
						 "",
						 ChatColor.RED + "PVP will be turned on in 5 minutes.",
						 "There are chests scattered around the map,",
						 "they contain random loot that might help you."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmicks, delay);
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				
				BufferedImage imageToSend = gimmicks.hungercompass.loadImage("images/compass.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "Compass Tracking",
						 "",
						 ChatColor.RED + "Enabled after 30 minutes.",
						 "You can right click with a compass to",
						 "track players. They can be found in chests."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmicks, delay);
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				
				BufferedImage imageToSend = gimmicks.hungercompass.loadImage("images/zombieface.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "You can run, but you can't hide!",
						 ChatColor.RED + "After an hour, the world border will shrink.",
						 "It will shrink further every 5 minutes.",
						 "Armed " + ChatColor.GREEN + "Zombies" + ChatColor.WHITE + " will spawn if you stay in one place",
						 "for too long."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmicks, delay);
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				
				BufferedImage imageToSend = gimmicks.hungercompass.loadImage("images/skeletonface.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "Once you're dead, you're out!",
						 "",
						 ChatColor.RED + "This server has a deathban.",
						 "Happy Hunger Games!",
						 "May the odds be ever in your favour."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmicks, delay);
		delay = delay + baseDelay;
		
		new BukkitRunnable() {
			@Override
			public void run()
			{
				BufferedImage imageToSend = gimmicks.hungercompass.loadImage("images/smile.png");
				new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(
						 "",
						 ChatColor.BOLD + "This event is brought to you by " + ChatColor.GOLD + "CivPVP" + ChatColor.WHITE + "!",
						 "",
						 ChatColor.RED + "We're raising funds for a better server.",
						 "If you can help by donating please go to",
						 ChatColor.BOLD + "/r/civpvp!" + ChatColor.RESET + " Thankyou," + ChatColor.GREEN + " GLHF."
				).sendToPlayers(Bukkit.getOnlinePlayers());
			}
		}.runTaskLater(gimmicks, delay);
		
		
	}

	public BufferedImage loadImage(String url) {
		BufferedImage imageToSend;			
		try {
			imageToSend = ImageIO.read(gimmicks.getResource(url));
		} catch (IOException e) {
			gimmicks.getLogger().warning("Unable to read image, play intro stopped.");
			e.printStackTrace();
			return null;
		}
		return imageToSend;
	}

}
