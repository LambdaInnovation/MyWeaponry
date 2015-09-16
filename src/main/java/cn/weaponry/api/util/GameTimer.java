package cn.weaponry.api.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

/**
 * A simple timer wrapup to handle paused timing situations.
 * @author WeAthFolD
 */
public enum GameTimer {
	INSTANCE;
	
	GameTimer() {
		FMLCommonHandler.instance().bus().register(this);
	}
	
	static long storedTime, timeLag;
	
	public static long getTime() {
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? getTimeClient() : getTimeServer();
	}
	
	public static long getAbsTime() {
		return MinecraftServer.getSystemTimeMillis();
	}
	
	@SideOnly(Side.CLIENT)
	private static long getTimeClient() {
		long time = Minecraft.getSystemTime();
		if(Minecraft.getMinecraft().isGamePaused()) {
			timeLag = time - storedTime;
		} else {
			storedTime = time;
		}
		return time - timeLag;
	}
	
	private static long getTimeServer() {
		return MinecraftServer.getSystemTimeMillis() - timeLag;
	}
	
}
