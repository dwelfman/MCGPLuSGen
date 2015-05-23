package com.minecraftgates.plus;

import org.bukkit.Bukkit;

public class MCGPLuSUtil {

	static String pluginName = "MCGPLuSGen";
	static int loglevel = 2;
	
	// -----------------------------------------------------
	// Static Console Logging Methods...
	//
	// This isolates our other source code from
	// the console logging scheme that is in
	// use and allows for better clarity in the
	// other portions of the code.
	//
	// Valid Logging Levels
	//
	// 0 ... Error Messages Only
	// 1 ... Warning and Errors Messages
	// 2 ... Warning, Errors and Info Messages
	// 3 ... Warning, Errors, Info and Debug Messages
	// 4 ... Warning, Errors, Info, Debug and Trace Messages
	// (this is the most possible)
	//
	// -----------------------------------------------------

	public static void logerror(String text) {
		// Display the console message in bold bright red text
		Bukkit.getConsoleSender().sendMessage("§l§c[" + pluginName + "] " + text);
	}

	public static void logwarn(String text) {
		// Display the console message in bright blue text
		if (loglevel >= 1) {
			Bukkit.getConsoleSender().sendMessage(
					"§b[" + pluginName + "] " + text);
		}
	}

	public static void loginfo(String text) {
		// Display the console message in bright green text
		if (loglevel >= 2) {
			Bukkit.getConsoleSender().sendMessage("§a[" + pluginName + "] " + text);
		}
	}

	public static void logdebug(String text) {
		// Display the console message in bold bright yellow text
		if (loglevel >= 3) {
			Bukkit.getConsoleSender()
					.sendMessage("§l§e[" + pluginName + "] " + text);
		}
	}

	public static void logtrace(String text) {
		// Display the console message in the default white text
		if (loglevel >= 4) {
			Bukkit.getConsoleSender()
					.sendMessage("[" + pluginName + "] " + text);
		}
	}
}
