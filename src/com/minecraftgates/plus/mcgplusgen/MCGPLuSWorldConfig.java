package com.minecraftgates.plus.mcgplusgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import com.minecraftgates.plus.*;

public class MCGPLuSWorldConfig {

	// -------------------------------------------------------
	// User Configurable Values
	// -------------------------------------------------------

	//
	// The following values are set by the user in the MCGPLuSGen configuration
	// file (config.yml) for a given world.
	//
	// rngSurface .... Binary exponent representing the distance between the
	// starting surface layer (minSurface) and the top surface layer.
	//
	// minSurface .... The minimum Y-coordinate for surface blocks.
	//
	// matSurface .... The material to use for surface blocks
	//
	// matSubSurface .... The material to use for blocks between the actual
	// (random) surface block y-level and the minimum surface block y-level.
	//
	// Notes:
	//
	// (1) Until such time that Bukkit/Spigot comes up with a better chunk
	// generation method, surface blocks must be base blocks and may not be
	// extended with item data (e.g. stone but not andesite is allowed).
	//
	public int rngSurface, minSurface;
	public Material matSurface, matSubSurface;

	//
	// These values can be used to limit the range (in max X and Z of the chunk)
	// of the generator so that the world is generated as flat with an edge that
	// players can fall off. A (-1) value indicates that there is no limit to
	// the
	// chunk generation range.
	//
	public int limitXDistance, limitZDistance;

	//
	// TODO: More comments needed here!
	//
	public String worldName;

	// -------------------------------------------------------
	// Hierarchical Values
	//
	// These values are required in order to accomplish certain methods after
	// class instantiation.
	// -------------------------------------------------------

	private MCGPLuSWorlds classWorlds;

	// -------------------------------------------------------
	// Derived values
	//
	// maxSurface ..... The maximum Y-coordinate for surface blocks.
	//
	// matSurfaceId ... The base Id of the material to use for the surface
	// blocks.
	//
	// matSubSurfaceId ... The base Id of the material to use for the
	// sub-surface blocks.
	//
	// Note:
	//
	// (1) The following values are derived or obtained from
	// other sources for speed of execution where performance
	// is critical.
	//
	// (2) They may also be derived so that they
	// cannot be made to conflict with other variables that
	// are actually set by the user.
	// -------------------------------------------------------

	public int deltaSurface;

	//
	// Objects created at instantiation time
	//
	public BaseGenerator chunkGenerator;

	List<MCGPLuSMaterial> materials;
	List<MCGPLuSMaterial> default_materials;

	//
	// TODO: Other stuff that still needs to be cleaned up...
	//
	public int topBlocksY;

	//
	// The following values are extracted from the Minecraft/Bukkit world
	// configuration and included here for efficiency.
	//
	public short matSurfaceId, matSubSurfaceId;
	public int maxHeight, maskSurface, maxSurface;

	//
	// The following values are used by the Tree Populator
	//
	// treeDensity ... determines how close a tree may be to another tree in
	// order for it to populate. Otherwise, it is skipped.
	//
	public int treeDensity;

	public int worldMaxHeight;
	public int numSections;

	public long seed;

	public int[] distroTotals;
	public int[] defaultBlockID;

	@SuppressWarnings("deprecation")
	public MCGPLuSWorldConfig(MCGPLuSWorlds arg1, String arg0) {

		int bY, totBlocks;
		MCGPLuSMaterial m;

		// -------------------------------------------------------
		// Save the pointer to the MCGPLuSWorlds object for
		// later use.
		// -------------------------------------------------------

		classWorlds = arg1;
		worldName = arg0;

		// -------------------------------------------------------
		// Instantiate certain objects that are used later
		// -------------------------------------------------------

		//
		// Create a chunk generator object
		//
		// This is instantiated here to save time during the actual
		// chunk generation and population phase. In theory this would
		// also permit a configuration to support different basic
		// types of chunk generators.
		//
		chunkGenerator = new BaseGenerator(this);

		//
		// Create lists are also used later to maintain the
		// collection of default layer material and ore seed
		// material.
		//
		default_materials = new ArrayList<MCGPLuSMaterial>();
		materials = new ArrayList<MCGPLuSMaterial>();

		// -------------------------------------------------------
		// Load any configuration available from our YAML file(s)
		//
		// Notes:
		//
		// (1) This will also default values if they do not exist
		// in the configuration file.
		// -------------------------------------------------------

		configLoad();

		//
		// If the user did not setup any default materials,
		// go ahead and configure them now.
		//

		if (materials.isEmpty()) {

			m = new MCGPLuSMaterial();
			m.matMaterial = Material.STONE;
			m.extMaterial = 5;
			m.pctGenerate = 5;
			m.pctAdjacent = 5;
			m.maxPerChunk = 2000;
			m.minY = 10;
			m.maxY = minSurface;

			materials.add(m);

			m = new MCGPLuSMaterial();
			m.matMaterial = Material.GOLD_ORE;
			m.extMaterial = 0;
			m.pctGenerate = 5;
			m.pctAdjacent = 5;
			m.maxPerChunk = 500;
			m.minY = 10;
			m.maxY = minSurface;

			materials.add(m);

			m = new MCGPLuSMaterial();
			m.matMaterial = Material.COAL_ORE;
			m.extMaterial = 0;
			m.pctGenerate = 5;
			m.pctAdjacent = 5;
			m.maxPerChunk = 500;
			m.minY = 10;
			m.maxY = minSurface;

			materials.add(m);

			m = new MCGPLuSMaterial();
			m.matMaterial = Material.STONE;
			m.extMaterial = 1;
			m.pctGenerate = 5;
			m.pctAdjacent = 5;
			m.maxPerChunk = 1500;
			m.minY = 10;
			m.maxY = minSurface;

			materials.add(m);

			m = new MCGPLuSMaterial();
			m.matMaterial = Material.STONE;
			m.extMaterial = 3;
			m.pctGenerate = 5;
			m.pctAdjacent = 5;
			m.maxPerChunk = 1500;
			m.minY = 10;
			m.maxY = minSurface;

			materials.add(m);
		}

		//
		// Define the default materials by layer
		//

		if (default_materials.isEmpty()) {

			m = new MCGPLuSMaterial();
			m.matMaterial = Material.DIRT;
			m.extMaterial = 0;
			m.pctGenerate = -1;
			m.pctAdjacent = -1;
			m.maxPerChunk = -1;
			m.minY = 32;
			m.maxY = minSurface;

			default_materials.add(m);

			m = new MCGPLuSMaterial();
			m.matMaterial = Material.STONE;
			m.extMaterial = 0;
			m.pctGenerate = -1;
			m.pctAdjacent = -1;
			m.maxPerChunk = -1;
			m.minY = 4;
			m.maxY = 31;

			default_materials.add(m);

			m = new MCGPLuSMaterial();
			m.matMaterial = Material.BEDROCK;
			m.extMaterial = 0;
			m.pctGenerate = -1;
			m.pctAdjacent = -1;
			m.maxPerChunk = -1;
			m.minY = 0;
			m.maxY = 3;

			default_materials.add(m);
		}

		// -------------------------------------------------------
		// Derive Values based on the User Configuration
		// -------------------------------------------------------

		//
		// TODO: More comments needed here!
		//
		matSurfaceId = (short) matSurface.getId();
		matSubSurfaceId = (short) matSubSurface.getId();
		maskSurface = (1 << rngSurface) - 1;
		maxSurface = minSurface + maskSurface;
		deltaSurface = maxSurface - minSurface;

		//
		// Using the number core layers from base to the lower surface layer and
		// the percentage of each ore/mineral resource to make available, go
		// through and calculate the maximum number of each per chunk
		//
		totBlocks = minSurface * 16 * 16;
		for (MCGPLuSMaterial ma : materials) {
			ma.maxPerChunk = (totBlocks * ma.pctGenerate) / 100;
		}
		
		//
		// The following values are obtained from the server world class
		//
		worldMaxHeight = 256;
		numSections = worldMaxHeight / 16;

		//
		// Create a table of default blocks by level
		//

		topBlocksY = 0;

		defaultBlockID = new int[minSurface + 1];

		for (MCGPLuSMaterial ma : default_materials) {
			for (bY = ma.minY; bY <= ma.maxY; ++bY) {
				defaultBlockID[bY] = ma.matMaterial.getId();
			}
		}

		for (bY = 0; bY < minSurface; ++bY) {
			MCGPLuSUtil.logdebug("default material bY[ " + bY + " ] = "
					+ defaultBlockID[bY]);
		}

		//
		// Calculate generation percentages
		//
		// Notes:
		// Since the layer definition is inclusive, the number of layers is
		// equal to the difference between the max layer and bottom layer plus
		// one. Their are then 16 x 16 blocks (256) per layer.
		//
		for (MCGPLuSMaterial ma : materials) {
			int blocks = (ma.maxY - ma.minY + 1) * 256;
			MCGPLuSUtil.logdebug("ore = " + ma.matMaterial.toString() + ":"
					+ ma.extMaterial + "dist="
					+ ((ma.maxPerChunk * 100.0) / blocks) + "% ("
					+ ma.maxPerChunk + "/" + blocks);
		}
	}

	//
	// Method: getChunkGenerator(...)
	//
	public ChunkGenerator getChunkGenerator() {
		return chunkGenerator;
	}

	//
	// Method: configDisplay(...)
	//
	public void configDisplay() {

		if (chunkGenerator != null) {
			MCGPLuSUtil
					.loginfo("Chunk Generator = " + chunkGenerator.getName());
		} else {
			MCGPLuSUtil.loginfo("Chunk Generator = <null>");
		}
	}

	//
	// Method: getPlugin(...)
	//
	private JavaPlugin getPlugin() {
		if (classWorlds == null) {
			MCGPLuSUtil
					.logerror("JavaPlugin class requested before being initialized!");
		}

		return classWorlds.getPlugin();
	}

	//
	// Method:configLoad(...)
	//
	private void configLoad() {

		FileConfiguration pcobj;
		HashMap<String, Object> hm, hm2;

		//
		// The configuration file is one of the most unforgiving items, so we
		// definitely want to place a try/catch around the code that will
		// attempt to read the formatted YAML text from disk.
		//

		try {

			limitXDistance = getPlugin().getConfig().getInt(
					worldName + ".limitXDistance", -1);
			limitZDistance = getPlugin().getConfig().getInt(
					worldName + ".limitZDistance", -1);

			seed = getPlugin().getConfig().getLong(worldName + ".seed",
					-2924375);
			rngSurface = getPlugin().getConfig().getInt(
					worldName + ".rngSurface", 3);
			minSurface = getPlugin().getConfig().getInt(
					worldName + ".minSurface", 48);

			matSurface = Material.valueOf(getPlugin().getConfig().getString(
					worldName + ".matSurface", Material.GRASS.toString()));
			matSubSurface = Material.valueOf(getPlugin().getConfig().getString(
					worldName + ".matSubSurface", Material.DIRT.toString()));

			treeDensity = getPlugin().getConfig().getInt(
					worldName + ".treeDensity", treeDensity);

			//
			// Load the default layers that need to be generated
			// from the config.yml file
			//
			pcobj = getPlugin().getConfig();

			hm = new HashMap<String, Object>();
			hm2 = new HashMap<String, Object>();

			hm = (HashMap<String, Object>) pcobj.getConfigurationSection(
					worldName + ".layers").getValues(false);

			for (Entry<String, Object> es : hm.entrySet()) {

				MCGPLuSUtil.logdebug("************** es = {" + es.getKey()
						+ "}");

				hm2 = (HashMap<String, Object>) pcobj.getConfigurationSection(
						worldName + ".layers." + es.getKey()).getValues(false);

				MCGPLuSMaterial m = MCGPLuSMaterial.configRead(hm2);
				default_materials.add(m);
			}

			//
			// Load the resources/ores that need to be populated
			// from the config.yml file
			//
			pcobj = getPlugin().getConfig();

			hm = new HashMap<String, Object>();
			hm2 = new HashMap<String, Object>();

			hm = (HashMap<String, Object>) pcobj.getConfigurationSection(
					worldName + ".resources").getValues(false);

			for (Entry<String, Object> es : hm.entrySet()) {

				MCGPLuSUtil.logdebug("************** es = {" + es.getKey()
						+ "}");

				hm2 = (HashMap<String, Object>) pcobj.getConfigurationSection(
						worldName + ".resources." + es.getKey()).getValues(
						false);

				MCGPLuSMaterial m = MCGPLuSMaterial.configRead(hm2);
				materials.add(m);
			}

			//
			// Save any deltas back out to the config.yml file
			//
			configSave();

		} catch (Exception e) {

			//
			// This is where we end up if the above code crashes. Right now we
			// simply leave the configuration the way it is, but more extensive
			// recovery code could be placed here in the future.
			//

			MCGPLuSUtil
					.logerror("Error while attempting to read config.yml -- using standard defaults for <"
							+ worldName + ">");
			if (e.getMessage() != null) {
				MCGPLuSUtil.logerror(e.getMessage());
			}
		}
	}

	//
	// Method:configSave(...)
	//
	private void configSave() {

		 int i;
		
		 getPlugin().getConfig().set(worldName + ".seed", seed);
		 getPlugin().getConfig().set(worldName + ".rngSurface", rngSurface);
		 getPlugin().getConfig().set(worldName + ".minSurface", minSurface);
		
		 getPlugin().getConfig().set(worldName + ".matSurface",
		 matSurface.toString());
		 getPlugin().getConfig().set(worldName + ".matSubSurface",
		 matSubSurface.toString());
		
		 getPlugin().getConfig().set(worldName + ".treeDensity", treeDensity);
		
		 getPlugin().getConfig().set(worldName + ".limitXDistance",
		 limitXDistance);
		 getPlugin().getConfig().set(worldName + ".limitZDistance",
		 limitZDistance);
		
		 //
		 // Go through and save the different materials/ores that are used as
		 // the default for each layer
		 //
		 i = 0;
		 for (MCGPLuSMaterial m : default_materials) {
		 getPlugin().getConfig().set(worldName + ".layers.item" + i +
		 ".block", m.matMaterial.name());
		 getPlugin().getConfig().set(worldName + ".layers.item" + i + ".data",
		 m.extMaterial);
		 getPlugin().getConfig().set(worldName + ".layers.item" + i +
		 ".pctGenerate", m.pctGenerate);
		 getPlugin().getConfig().set(worldName + ".layers.item" + i +
		 ".pctAdjacent", m.pctAdjacent);
		 getPlugin().getConfig().set(worldName + ".layers.item" + i + ".maxY",
		 m.maxY);
		 getPlugin().getConfig().set(worldName + ".layers.item" + i + ".minY",
		 m.minY);
		 i++;
		 }
		
		 //
		 // Go through and load the different materials/ores that we want to
		 // make available to the player in this world
		 //
		 i = 0;
		 for (MCGPLuSMaterial m : materials) {
		 getPlugin().getConfig().set(worldName + ".resources.item" + i +
		 ".block", m.matMaterial.name());
		 getPlugin().getConfig().set(worldName + ".resources.item" + i +
		 ".data", m.extMaterial);
		 getPlugin().getConfig().set(worldName + ".resources.item" + i +
		 ".pctGenerate", m.pctGenerate);
		 getPlugin().getConfig().set(worldName + ".resources.item" + i +
		 ".pctAdjacent", m.pctAdjacent);
		 getPlugin().getConfig().set(worldName + ".resources.item" + i +
		 ".maxY", m.maxY);
		 getPlugin().getConfig().set(worldName + ".resources.item" + i +
		 ".minY", m.minY);
		 i++;
		 }
		
		 //
		 // Now go ahead and save the changes out to
		 // the config.yml file
		 //
		 getPlugin().saveConfig();
	}
}
