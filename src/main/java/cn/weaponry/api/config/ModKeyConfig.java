package cn.weaponry.api.config;

import java.util.HashMap;
import java.util.Map;

import cn.liutils.util.helper.KeyManager;
import cn.weaponry.api.event.KeyConfigUpdateEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

/**
 * This class storages per-mod key config. You can register a key config either at init or at runtime 
 *  (usually for changing key binding).
 * @author WeAthFolD
 */
public class ModKeyConfig {
	
	private static Map<String, KeyConfig> modConfig = new HashMap();
	private static KeyConfig defaultConfig = new KeyConfigBuilder()
			.add(KeyManager.MOUSE_LEFT, 0).add(KeyManager.MOUSE_RIGHT, 1).toConfig();
	
	/**
	 * Update the key config of current loaded mod. Should be called in loading phase.
	 */
	public static void update(KeyConfig config) {
		ModContainer container = Loader.instance().activeModContainer();
		if(container == null)
			throw new IllegalStateException("No mod is currently being loaded");
		
		update(container.getModId(), config);
	}
	
	public static void update(String modid, KeyConfig config) {
		KeyConfig old = getConfig(modid);
		modConfig.put(modid, config);
		if(old != defaultConfig)
			MinecraftForge.EVENT_BUS.post(new KeyConfigUpdateEvent(modid, old, config));
	}
	
	public static KeyConfig getConfig(Item item) {
		String name = GameData.getItemRegistry().getNameForObject(item);
		String modid = name.substring(name.indexOf(':'));
		return getConfig(modid);
	}
	
	public static KeyConfig getConfig(String modid) {
		KeyConfig conf = modConfig.get(modid);
		if(conf == null)
			return defaultConfig;
		return conf;
	}
	
}
