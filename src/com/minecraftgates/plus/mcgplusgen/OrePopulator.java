package com.minecraftgates.plus.mcgplusgen;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import com.minecraftgates.plus.*;

public class OrePopulator extends BlockPopulator {

	MCGPLuSWorldConfig worldConfig;

	private static int lutA[] = { 1, 47, 5, 41, 11, 31, 17, 23, 19, 29, 13, 37,
			7, 43, 3, 53 };
	private static int lutB[] = { 13, 11, 19, 17, 29, 23, 37, 31, 43, 41, 53,
			47, 3, 1, 7, 5 };

	public OrePopulator(MCGPLuSWorldConfig arg0) {
		worldConfig = arg0;
	}

	@Override
	public void populate(World world, Random rnd, Chunk chunk) {

		int bX, bY, bZ, lutX, lutZ;

		//
		// Now, let's start filling in the ores
		//
		bX = chunk.getX();
		bZ = chunk.getZ();

		if (((worldConfig.limitXDistance == -1) || (Math.abs(bX) <= worldConfig.limitXDistance))
				&& ((worldConfig.limitZDistance == -1) || (Math.abs(bZ) <= worldConfig.limitXDistance))) {

			lutX = (int) (lutA[bX & 0x0f] * lutB[bZ & 0x0f] * worldConfig.seed);
			lutZ = (int) (lutA[bZ & 0x0f] * lutB[bX & 0x0f] * worldConfig.seed);

			//
			// Before we start we should setup the seed for the random number
			// generator so that this chunk will always generate the same
			// way each time it regenerates
			//
			rnd.setSeed(lutX * lutZ);

			//
			// Now, let's start filling in the ores
			//
			for (MCGPLuSMaterial ma : worldConfig.materials) {
				int delta = ma.maxY - ma.minY;
				for (int i = 0; i < ma.maxPerChunk; ++i) {
					bY = rnd.nextInt(delta) + ma.minY;
					bX = rnd.nextInt(16);
					bZ = rnd.nextInt(16);
					ma.SetBlock(chunk, bX, bY, bZ);
				}
			}
		}
	}
}
