package cn.weaponry.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class SideHelper {
	
	public static World getWorld(int dimension) {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			return getWorldClient(dimension);
		} else {
			return MinecraftServer.getServer().worldServerForDimension(dimension);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static World getWorldClient(int dim) {
		if(dim == Minecraft.getMinecraft().theWorld.provider.dimensionId)
			return Minecraft.getMinecraft().theWorld;
		return null;	
	}
	
}
