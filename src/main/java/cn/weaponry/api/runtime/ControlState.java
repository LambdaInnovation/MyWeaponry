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
package cn.weaponry.api.runtime;

import cn.weaponry.api.ctrl.KeyPhase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author WeAthFolD
 */
public abstract class ControlState {
	
	ItemInfo info;
	StateMachine machine;
	
	public void onEnter() {}
	
	public void onTick() {}
	
	public void onLeave() {}
	
	public void onKey(int keyID, KeyPhase phase) {}
	
	protected ItemInfo getInfo() {
		return info;
	}
	
	protected StateMachine getStateMachine() {
		return machine;
	}
	
	protected EntityPlayer getPlayer() {
		return getInfo().getPlayer();
	}
	
	protected World getWorld() {
		return getPlayer().worldObj;
	}
	
	protected ItemStack getStack() {
		return getInfo().getStack();
	}
	
}
