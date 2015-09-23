package cn.weaponry.api.config;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

/**
 * KeyConfig is an **immutable** class handling the key config of an mod.
 * You can create a KeyConfig use KeyConfigBuilder.
 * @author WeAthFolD
 */
public class KeyConfig {
	
	// Keyboard Key => Virtual Key ID
	Map<Integer, Integer> mapping;
	
	KeyConfig(Map<Integer, Integer> init) {
		mapping = ImmutableMap.copyOf(init);
	}
	
	public Collection<Integer> availableKeys() {
		return mapping.keySet();
	}
	
	public Collection<Integer> availableKeyIDs() {
		return mapping.values();
	}
	
	public Collection<Entry<Integer, Integer>> enumeration() {
		return mapping.entrySet();
	}
	
	public int size() {
		return mapping.size();
	}
	
	public int mappingFor(int keyID) {
		Integer res = mapping.get(keyID);
		return res == null ? - 1 : res;
	}
	
	public KeyConfig clone() {
		return new KeyConfig(ImmutableMap.copyOf(mapping));
	}
	
}
