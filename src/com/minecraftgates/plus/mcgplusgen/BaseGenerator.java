package com.minecraftgates.plus.mcgplusgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import com.minecraftgates.plus.MCGPLuSUtil;

public class BaseGenerator extends ChunkGenerator {

	List<BlockPopulator> populators;

	private static long lutA[] = { 1, 47, 5, 41, 11, 31, 17, 23, 19, 29, 13,
			37, 7, 43, 3, 53 };
	private static long lutB[] = { 13, 11, 19, 17, 29, 23, 37, 31, 43, 41, 53,
			47, 3, 1, 7, 5 };

	//
	// Transition Tables
	//
	// The tables below are used to transition from one chunk 
	// group (4-chunks) to another chunk group (4-chunks) in 
	// a somewhat smoother manner.
	//
	// Notes:
	//
	// (1) The tables are organized as a two dimension array 
	// with the X coordinate across the top and Z coordinate
	// up and down.  This makes North the left side of the 
	// table, South the right side of the table, East the 
	// bottom of the table and West the top of the table.
	//
	// (2) This is done as pre-generated tables to improve the
	// efficency at chunk generation time.
	//
	private static int flatC[][] = {
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 } };

	private static int curveW[][] = {
			{ 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 },
			{ 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 },
			{ 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	private static int curveE[][] = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
			{ 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 },
			{ 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 } };

	private static int curveN[][] = {
			{ 4, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 4, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	private static int curveS[][] = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4 } };
	
	private int divisor[][] = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	private MCGPLuSWorldConfig worldConfig;

	public BaseGenerator(MCGPLuSWorldConfig arg0) {

		//
		// Go ahead and save or create any classes that we will need in
		// order to generate the chunks when called.
		//
		worldConfig = arg0;

		//
		// Go through and fill the divisor table based on the sum of the entries
		// in the other five tables.
		//
		for (int dX = 0; dX < 16; ++dX) {
			for (int dZ = 0; dZ < 16; ++dZ) {
				divisor[dX][dZ] = flatC[dX][dZ] + curveN[dX][dZ]
						+ curveS[dX][dZ] + curveE[dX][dZ] + curveW[dX][dZ];
			}
		}
		//
		// Go through and build the list of populators that are run after the
		// base generator has filled the chunk with its initial blocks.
		//
		populators = new ArrayList<BlockPopulator>();
		populators.add(new OrePopulator(worldConfig));
		populators.add(new TreePopulator(worldConfig));
	}

	public String getName() {
		return "MCGPLuSGen";
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void setBlock(short[][] result, int x, int y, int z, Material block) {
		setBlock(result, x, y, z, block.getId());
	}

	private void setBlock(short[][] result, int x, int y, int z, int blockID) {
		setBlock(result, x, y, z, (short) blockID);
	}

	private void setBlock(short[][] result, int x, int y, int z, short blockID) {
		int sectionID = (y >> 4);
		if (result[sectionID] == null) {
			result[sectionID] = new short[4096];
		}
		result[sectionID][((y & 0xF) << 8) | (z << 4) | x] = blockID;
	}

	private int chunkY(int cX, int cZ) {
		return (int) ((lutA[(cX >> 1) & 0x0f] * lutB[(cZ >> 1) & 0x0f]) & worldConfig.maskSurface)
				+ worldConfig.minSurface;
	}

	private int heightY(int cX, int cZ, int posX, int posZ) {

		//
		// Calculate the base height of this chunk
		//
		int thisY = chunkY(cX, cZ);

		//
		// Calculate the height of the Northern Chunk
		//
		int northY = chunkY(cX, (cZ - 1));
		int southY = chunkY(cX, (cZ + 1));
		int eastY = chunkY((cX + 1), cZ);
		int westY = chunkY((cX - 1), cZ);

		//
		// Smooth the results using the tables above and return the final value
		// back to the caller
		//
		return ((thisY * flatC[posX][posZ]) + (northY * curveN[posX][posZ])
				+ (southY * curveS[posX][posZ]) + (eastY * curveE[posX][posZ]) 
				+ (westY * curveW[posX][posZ]))
				/ divisor[posX][posZ];
	}

	private void generateSurfaceBlocks(short[][] buffer, World world,
			Random rnd, int x, int z) {

		int bX, bY, bZ, surfaceY;
		rnd.setSeed(lutA[(x >> 1) & 0x0f] * lutB[(z >> 1) & 0x0f]
				* world.getSeed());
		surfaceY = rnd.nextInt(worldConfig.deltaSurface)
				+ worldConfig.minSurface;

		for (bX = 0; bX < 16; ++bX) {
			for (bZ = 0; bZ < 16; ++bZ) {
				surfaceY = heightY(x, z, bX, bZ);
				setBlock(buffer, bX, surfaceY, bZ, worldConfig.matSurfaceId);
				for (bY = worldConfig.minSurface; bY < surfaceY; ++bY) {
					setBlock(buffer, bX, bY, bZ, worldConfig.matSubSurfaceId);
				}
			}
		}
	}

	//
	// This replaces the generate(...) method and handles extended materials. If
	// the server locates the generateExtBlockSections(...) method it will
	// ignore this method only enter through the generateExtBlockSections(...)
	// method when generating a chunk.
	//

	public short[][] generateExtBlockSections(World world, Random random,
			int x, int z, BiomeGrid biomes) {

		//
		// MV huh?
		//
		MCGPLuSUtil.logdebug("generating world..." + world.getName() + "[config=" + worldConfig.worldName + "]");
		
		//
		// Allocate storage for the chunk buffer.  
		//
		// Notes:
		//
		// (1) Even if nothing is placed into the buffer, this buffer must be
		// allocated or the system will crash.
		//
		short[][] buffer = new short[worldConfig.numSections][];
		
		//
		// Before we do anything else, should we return an empty chunk buffer
		// because this buffer is outside the maximum allowable X and Z range.
		//
		if (((worldConfig.limitXDistance == -1) || (Math.abs(x) <= worldConfig.limitXDistance)) &&
				((worldConfig.limitZDistance == -1) || (Math.abs(z) <= worldConfig.limitXDistance))) {
			
			//
			// First fill everything below the minimum surface level
			// (minSurface) with the default block for that level
			//
			int bX, bY, bZ;
			
			for (bX = 0; bX < 16; ++bX) {
				for (bZ = 0; bZ < 16; ++bZ) {
					biomes.setBiome(bX, bZ, Biome.FOREST);
					for (bY = 0; bY < worldConfig.minSurface; ++bY) {
						setBlock(buffer, bX, bY, bZ, worldConfig.defaultBlockID[bY]);
					}
				}
			}
	
			//
			// Now fill the surface blocks
			//
			generateSurfaceBlocks(buffer, world, random, x, z);
		}
		
		return buffer;
	}

	//
	// The method below is deprecated. If the server locates the
	// generateExtBlockSections(...) method it will ignore this method
	// only enter through the generateExtBlockSections(...) method
	// when generating a chunk.
	//
	@Override
	public byte[] generate(World world, Random random, int x_in, int z_in) {
		return null;
	}

	public List<BlockPopulator> getDefaultPopulators(World world) {
		return populators;
	}

	//
	// By returning NULL, we are allowing the world creator to attempt to find a
	// valid spawn point. This may cause additional block generation as it
	// expands out to find something that is safe.
	//
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return null;
	}
}
