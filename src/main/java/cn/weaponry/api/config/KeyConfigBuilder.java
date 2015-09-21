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
package cn.weaponry.api.config;

import java.util.Map;

/**
 * @author WeAthFolD
 */
public class KeyConfigBuilder {
	
	private Map<Integer, Integer> mapping;
	
	public KeyConfigBuilder() {}
	
	public KeyConfigBuilder add(int key, int keyID) {
		if(mapping.containsKey(key))
			throw new RuntimeException("Key " + key + " is already used");
		if(mapping.containsValue(keyID))
			throw new RuntimeException("Key ID " + key + " is already used");
		mapping.put(key, keyID);
		return this;
	}
	
	public KeyConfigBuilder add(int[][] arr) {
		for(int[] a : arr)
			add(a[0], a[1]);
		return this;
	}
	
	public KeyConfig toConfig() {
		return new KeyConfig(mapping);
	}
	
}
