/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.api.forgeevent;

import cn.weaponry.api.config.KeyConfig;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * Fired whenever a mod's key config is changed in CLIENT. (Not fired when normally added, so old is always available)
 * @author WeAthFolD
 */
public class KeyConfigUpdateEvent extends Event {
	
	public final String modid;
	public final KeyConfig old;
	public final KeyConfig replace;
	
	public KeyConfigUpdateEvent(String _modid, KeyConfig _old, KeyConfig _replace) {
		modid = _modid;
		old = _old;
		replace = _replace;
	}
	
}
