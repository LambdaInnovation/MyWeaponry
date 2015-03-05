/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.api.inf;

import java.util.HashMap;
import java.util.Map;

import cn.weaponry.api.item.IRequiresInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

/**
 * Represents per-player current ItemStack runtime information.
 * The data is created once per player and is cleared each time player changes item.
 * This class only stores RUNTIME data, don't sync, and clears its information each time
 * item changes.
 * Currently used by Action, Render, and weapon class itself to store information.
 * 
 * The handling class of InfWeapon is InfWeaponHandler. Use it to retrieve the weapon instance.
 * @author WeathFolD
 */
public final class InfWeapon {
	
	final EntityPlayer player;
	
	static Map<String, Class<? extends InfWeaponPart>> regTable = new HashMap();
	
	Map<String, InfWeaponPart> alive = new HashMap(); //Current alive parts, or empty if item doesn't use info.
	
	// Last judge data to see if item was changed
	int lastSlot = -1;
	ItemStack lastStack;

	public InfWeapon(EntityPlayer _player) {
		player = _player;
	}
	
	//APIs
	
	/**
	 * TODO unfinished
	 * @param name
	 * @return
	 */
	public InfWeaponPart getPart(String name) {
		return null;
	}
	
	public boolean isActive() {
		checkValidity();
		return lastStack != null && lastStack.getItem() instanceof IRequiresInfo;
	}
	
	//---Internal Process
	/**
	 * Called by tickHandler to do update.
	 */
	public void onTick() {
		
	}
	
	/**
	 * Called per tick&per retrieval. Update the holding item state.
	 */
	void checkValidity() {
		InventoryPlayer inv = player.inventory;
		ItemStack curStack = inv.getCurrentItem();
		boolean needReset = false;
		if(lastSlot != inv.currentItem)
			needReset = true;
		if((curStack == null || lastStack == null) ||
			(lastStack.getItem() != curStack.getItem()))
			needReset = true;
		
	}
	
	private void reset(int slot, ItemStack stack) {
		//reset the "last" data to current info.
		lastSlot = slot;
		lastStack = stack;
		
		
	}
	
	/**
	 * Just for notification purpose.
	 */
	public void onDisposed() {}

}
