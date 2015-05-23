package com.minecraftgates.plus.mcgplusgen;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class TreePopulator extends BlockPopulator {

	MCGPLuSWorldConfig worldConfig;

	private static long lutA[] = { 19, 53, 1, 47, 5, 41, 11, 31, 17, 23, 13,
			29, 37, 7, 43, 3 };
	private static long lutB[] = { 13, 11, 19, 17, 29, 23, 37, 31, 43, 41, 53,
			47, 3, 1, 7, 5 };
	private static long lutC[] = { 11, 17, 23, 19, 31, 29, 43, 37, 41, 47, 3,
			53, 1, 5, 13, 7 };

	public TreePopulator(MCGPLuSWorldConfig arg0) {
		worldConfig = arg0;
	}

	@Override
	public void populate(World world, Random rnd, Chunk chunk) {

		int density, bX, bY, bZ;

		//
		// Are we within range?
		//
		// If the creator has specified a maximum range to create chunks, then
		// make sure that we are within range before we go any further.
		//
		
		bX = chunk.getX();
		bZ = chunk.getZ();
		
		if (((worldConfig.limitXDistance == -1) || (Math.abs(bX) <= worldConfig.limitXDistance))
				&& ((worldConfig.limitZDistance == -1) || (Math.abs(bZ) <= worldConfig.limitXDistance))) {

			//
			// Update the random seed based on this chunk.
			//
			// Note:
			//
			// (1) This should allow us to completely replicate the tree
			// placement
			// each and every time we regenerate this chunk with this world key.
			//
			rnd.setSeed(lutA[(int) (bX & 0x0f)]
					* lutB[(int) (bZ & 0x0f)]
					* lutC[(int) (world.getSeed() & 0x0f)]);

			//
			// Step through the number of tree the user wants per chunk
			// (treeDenisity) and place as many as we can in blank blocks.
			//
			for (density = 0; density <= worldConfig.treeDensity; ++density) {

				//
				// Select a random bX and bZ position
				//
				bX = rnd.nextInt(16);
				bZ = rnd.nextInt(16);

				//
				// Now step from the minSurface y-level to the maxSurface
				// y-level
				// and see if we can find an empty space to place the tree
				//
				for (bY = worldConfig.minSurface; bY < world.getMaxHeight(); ++bY) {
					Block b = chunk.getBlock(bX, bY, bZ);
					if (b.isEmpty() || (b.getType() == Material.AIR)) {
						world.generateTree(b.getLocation(), TreeType.TREE);
						break;
					}
				}
			}
		}
	}
}
