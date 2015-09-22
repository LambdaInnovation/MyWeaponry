package cn.weaponry.api.ctrl;

import cn.weaponry.api.runtime.ItemInfo;

/**
 * Item subclasses that implements this will receive ctrl delegations from Weaponry.
 */
public interface IControllableItem {
	
	void onKeyEvent(ItemInfo info, int keyID, KeyPhase phase);
	
}	
