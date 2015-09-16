package cn.weaponry.api.util;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IBlockFilter {
	
	boolean accepts(World world, int x, int y, int z, Block block);
	
}