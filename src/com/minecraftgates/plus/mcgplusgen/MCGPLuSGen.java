package com.minecraftgates.plus.mcgplusgen;

import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import com.minecraftgates.plus.*;

public class MCGPLuSGen extends JavaPlugin {
	//
	// Some objects that might be requested by other objects
	//
	MCGPLuSWorlds worlds;

	@Override
	public void onEnable() {
		//
		// Before anything else, get a reference to the Minecraft
		// server object.
		//
		MCGPLuSUtil.loginfo("Enabling MCGPLuSGen!");

		//
		// Create the world configuration collection
		//
		worlds = new MCGPLuSWorlds(this);
		
		//
		// If necessary, copy the default config.yml 
		// file from the jar file
		//
		getConfig().options().copyDefaults(true);
		getConfig().options().copyHeader(true);
		saveDefaultConfig();
	}

	@Override
	public void onDisable() {
		MCGPLuSUtil.loginfo("Disabling MCGPLuSGen!");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String CommandLabel, String[] args) {
		//
		// Command: MCGDIST
		//

		if (CommandLabel.equals("mcgdist")) {

			if ((sender != null) && (sender instanceof Player)) {

				Player p = (Player) sender;
				Location loc = p.getLocation();

				sender.sendMessage("--- Block Distribution in Chunk ---");

				Chunk c = loc.getChunk();
				int maxHeight = loc.getWorld().getMaxHeight();
				CounterMap cm = new CounterMap();

				for (int dY = 0; dY < maxHeight; ++dY) {
					for (int dX = 0; dX < 16; ++dX) {
						for (int dZ = 0; dZ < 16; ++dZ) {
							Block b = c.getBlock(dX, dY, dZ);
							cm.put(b.getType().toString() + ":" + b.getData());
						}
					}
				}

				for (Map.Entry<String, Object> entry : cm.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					sender.sendMessage(key + " = " + value);
				}

			} else {

				//
				// Error message to show the console user they are
				// prohibited from using this command.
				//
				sender.sendMessage("Sorry, this command may not be issued from the console!");
			}

			return true;
		}

		//
		// Command: MCGSTATS
		//

		if (CommandLabel.equals("mcgconfig")) {
			worlds.configDisplay(sender);
			return true;
		}

		//
		// If none of the commands above are executed, then go ahead and return
		// false back so that other plugin modules may have an opportunity to
		// process this command.
		//
		return false;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		
		MCGPLuSWorldConfig worldConfig = (MCGPLuSWorldConfig) worlds
				.get(worldName);
		
		if (worldConfig.getChunkGenerator() == null) {
			MCGPLuSUtil.logerror("Unable to instantiate default chunk generator!");
		}
		
		return worldConfig.getChunkGenerator();
	}
}
