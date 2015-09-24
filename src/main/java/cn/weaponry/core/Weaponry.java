/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.annoreg.core.RegistrationManager;
import cn.annoreg.core.RegistrationMod;
import cn.weaponry.core.network.ItemEventMessage;
import cn.weaponry.test.SerializationTest;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;

/**
 * Main class of MyWeaponry.
 * @author WeathFolD
 */
@Mod(modid = "weaponry", name = "MyWeaponry", version = Weaponry.VERSION)
@RegistrationMod(pkg = "cn.weaponry", res = "weaponry")
public class Weaponry {
	
	private static final String NET_CHANNEL = "weaponry";
	
	public static final String VERSION = "2.0";
	
	@Instance("weaponry")
	public static Weaponry instance;
	
	public static Logger log = LogManager.getLogger("Weaponry");
	
	public static Configuration config;
	
	private static final int ID_ITEMEVENT_CL = 0, ID_ITEMEVENT_SV = 1;
	
	public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(NET_CHANNEL);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		log.info("Starting MyWeaponry ver" + VERSION);
		log.info("Copyright (c) Lambda Innovation, 2015.");
		log.info("http://li-dev.cn/");
		
		network.registerMessage(ItemEventMessage.Handler.class, ItemEventMessage.class, ID_ITEMEVENT_CL, Side.CLIENT);
		network.registerMessage(ItemEventMessage.Handler.class, ItemEventMessage.class, ID_ITEMEVENT_CL, Side.SERVER);
		
		RegistrationManager.INSTANCE.registerAll(this, "PreInit");
	}
	
	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		
		RegistrationManager.INSTANCE.registerAll(this, "Init");
	}
	
	@EventHandler
	public void postInit(FMLPreInitializationEvent event) {
		SerializationTest.run();
		
		RegistrationManager.INSTANCE.registerAll(this, "PostInit");
	}
	
	@EventHandler()
	public void serverStarting(FMLServerStartingEvent event) {}

}
