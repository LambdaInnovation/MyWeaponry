package cn.weaponry.api.ctrl;

import cn.weaponry.api.runtime.ItemInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Item subclasses that implements this will receive ctrl delegations from Weaponry.
 */
public interface IControllableItem {
	
	/**
	 * Called at client only when player is holding this item 
	 *  and the corresponding key event is triggered.
	 * @param info
	 * @param keyID
	 * @param phase
	 */
	@SideOnly(Side.CLIENT)
	void onKeyEvent(ItemInfo info, int keyID, KeyPhase phase);
	
}	
