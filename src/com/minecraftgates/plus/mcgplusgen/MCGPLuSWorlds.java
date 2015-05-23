package com.minecraftgates.plus.mcgplusgen;

import java.util.HashMap;
import com.minecraftgates.plus.*;

import org.bukkit.command.CommandSender;

public class MCGPLuSWorlds extends HashMap<String,Object>{

	private static final long serialVersionUID = -1783944563357659772L;
	public MCGPLuSGen plugin = null;

	public MCGPLuSWorlds(MCGPLuSGen arg0) {
		super();
		
		MCGPLuSUtil.logdebug("Instantiated MCGPLuSWorlds class...");
		
		plugin = arg0;
	}
	
	public MCGPLuSGen getPlugin() {
		return plugin;
	}
	
	public Object get(String key) {
		
		Object worldConfig = super.get(key);
		
		if (worldConfig == null) {
			worldConfig = new MCGPLuSWorldConfig(this, key);
			super.put(key, worldConfig);
			MCGPLuSUtil.logdebug("MCGPLuSWorlds :: creating new world configuration <" + key + ">");
		} 
		
		return worldConfig;
	}
	
	public void configDisplay(CommandSender sender) {
		sender.sendMessage("processing configDisplay(...)");
		MCGPLuSWorldConfig wc = ((MCGPLuSWorldConfig)get("test"));
		wc.configDisplay();
		for (Object value : values()) {
			((MCGPLuSWorldConfig)value).configDisplay();
		}
	}

	public void configDisplay(CommandSender sender, String key) {
		sender.sendMessage("processing configDisplay(...)");
		((MCGPLuSWorldConfig)get(key)).configDisplay();
	}
}
