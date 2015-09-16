package cn.weaponry.api.util;

import java.util.ArrayList;
import java.util.List;

import cn.weaponry.api.util.EntitySelectors.SelectorList;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WorldUtils {
	
	public static AxisAlignedBB getBoundingBox(Vec3 vec1, Vec3 vec2) {
		double minX = 0.0, minY = 0.0, minZ = 0.0, maxX = 0.0, maxY = 0.0, maxZ = 0.0;
		if(vec1.xCoord < vec2.xCoord) {
			minX = vec1.xCoord;
			maxX = vec2.xCoord;
		} else {
			minX = vec2.xCoord;
			maxX = vec1.xCoord;
		}
		if(vec1.yCoord < vec2.yCoord) {
			minY = vec1.yCoord;
			maxY = vec2.yCoord;
		} else {
			minY = vec2.yCoord;
			maxY = vec1.yCoord;
		}
		if(vec1.zCoord < vec2.zCoord) {
			minZ = vec1.zCoord;
			maxZ = vec2.zCoord;
		} else {
			minZ = vec2.zCoord;
			maxZ = vec1.zCoord;
		}
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * Return a minimum AABB that can hold the points given.
	 */
	public static AxisAlignedBB ofPoints(Vec3 ...points) {
		if(points.length == 0) {
			throw new RuntimeException("Invalid call: too few vectors");
		}
		AxisAlignedBB ret = AxisAlignedBB.getBoundingBox(
			points[0].xCoord, points[0].yCoord, points[0].zCoord, 
			points[0].xCoord, points[0].yCoord, points[0].zCoord);
		
		for(int i = 1; i < points.length; ++i) {
			if(ret.minX > points[i].xCoord)
				ret.minX = points[i].xCoord;
			if(ret.maxX < points[i].xCoord)
				ret.maxX = points[i].xCoord;
			
			if(ret.minY > points[i].yCoord)
				ret.minY = points[i].yCoord;
			if(ret.maxY < points[i].yCoord)
				ret.maxY = points[i].yCoord;
			
			if(ret.minZ > points[i].zCoord)
				ret.minZ = points[i].zCoord;
			if(ret.maxZ < points[i].zCoord)
				ret.maxZ = points[i].zCoord;
		}
		
		return ret;
	}
	
	public static List<Entity> getEntities(TileEntity te, double range, IEntitySelector filter) {
		return getEntities(te.getWorldObj(), te.xCoord + 0.5, te.yCoord + 0.5, te.zCoord + 0.5, range, filter);
	}
	
	public static List<Entity> getEntities(Entity ent, double range, IEntitySelector filter) {
		return getEntities(ent.worldObj, ent.posX, ent.posY, ent.posZ, range, filter);
	}
	
	public static List<Entity> getEntities(World world, double x, double y, double z, double range, IEntitySelector filter) {
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
			x - range, y - range, z - range, 
			x + range, y + range, z + range);
		SelectorList list = new SelectorList(filter, new EntitySelectors.RestrictRange(x, y, z, range));
		return getEntities(world, box, list);
	}
	
	public static List<Entity> getEntities(World world, AxisAlignedBB box, IEntitySelector filter) {
		return world.getEntitiesWithinAABBExcludingEntity(null, box, filter);
	}
	
}
