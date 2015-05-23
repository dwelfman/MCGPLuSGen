package com.minecraftgates.plus;

import java.util.HashMap;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class MCGPLuSMaterial {

	public Material matMaterial;
	public byte extMaterial;
	public int pctGenerate, pctAdjacent, maxPerChunk, maxY, minY;

	public MCGPLuSMaterial() {

		matMaterial = Material.AIR;
		extMaterial = 0;
		pctGenerate = 25;
		pctAdjacent = 50;
		maxPerChunk = 25;
		maxY = -1;
		minY = -1;
	}

	@SuppressWarnings("deprecation")
	public void SetBlock(Chunk chunk, int bX, int bY, int bZ) {
		Block b = chunk.getBlock(bX, bY, bZ);
		b.setType(matMaterial);
		b.setData(extMaterial);
	}

	public static MCGPLuSMaterial configRead(HashMap<String, Object> ms) {
		
		MCGPLuSMaterial mcgplusmat = new MCGPLuSMaterial();
		
		Object wo;
		
		MCGPLuSUtil.logdebug("     ***** getting block...");
		wo = ms.get("block");
		if (wo != null) {
			MCGPLuSUtil.logdebug("     found = " + (String)wo);
			mcgplusmat.matMaterial = Material.getMaterial((String)wo);
		}

		MCGPLuSUtil.logdebug("     ***** getting data...");
		wo = ms.get("data");
		if (wo != null) {
			MCGPLuSUtil.logdebug("     found = " + (int)wo);
			mcgplusmat.extMaterial = (byte)((int)wo);
		}

		MCGPLuSUtil.logdebug("     ***** getting pctGenerate...");
		wo = ms.get("pctGenerate");
		if (wo != null) {
			MCGPLuSUtil.logdebug("     found = " + (int)wo);
			mcgplusmat.pctGenerate = (int)wo;
		}
		
		MCGPLuSUtil.logdebug("     ***** getting pctAdjacent...");
		wo = ms.get("pctAdjacent");
		if (wo != null) {
			MCGPLuSUtil.logdebug("     found = " + (int)wo);
			mcgplusmat.pctAdjacent = (int)wo;
		}
		
		MCGPLuSUtil.logdebug("     ***** getting maxY...");
		wo = ms.get("maxY");
		if (wo != null) {
			MCGPLuSUtil.logdebug("     found = " + (int)wo);
			mcgplusmat.maxY = (int)wo;
		}
		
		MCGPLuSUtil.logdebug("     ***** getting minY...");
		wo = ms.get("minY");
		if (wo != null) {
			MCGPLuSUtil.logdebug("     found = " + (int)wo);
			mcgplusmat.minY = (int)wo;
		}
		
		return mcgplusmat;
	}

	public void dump() {
		MCGPLuSUtil.loginfo("block ......... " + matMaterial.toString() + ":" + extMaterial);
		MCGPLuSUtil.loginfo("  pctGenerate ... " + pctGenerate);
		MCGPLuSUtil.loginfo("  pctAdjacent ... " + pctAdjacent);
		MCGPLuSUtil.loginfo("  maxY .......... " + maxY);
		MCGPLuSUtil.loginfo("  minY .......... " + minY);
	}
}
