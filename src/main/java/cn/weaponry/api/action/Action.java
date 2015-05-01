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
package cn.weaponry.api.action;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cn.weaponry.api.ItemInfo;

/**
 * @author WeAthFolD
 */
public abstract class Action {
	
	/**
	 * The itemInfo instance this Action is attached to. DONT MODIFY THIS FIELD!
	 */
	public ItemInfo itemInfo;
	
	/**
	 * Whether this action is a 'raw'(Created-not-by-sync) action. DO NOT CHANGE THIS FIELD!
	 */
	public boolean isRaw = true;
	
	/**
	 * Whether this action is being disposed(Needs to get removed).
	 *  Do not directly change it, use finishAction() or abortAction().
	 */
	public boolean disposed = false;
	
	public Action() {}
	
	public abstract String getName();
	
	//---Events implemented by subclasses
	
	public void onStart() {}
	
	public void onTick() {}
	
	public void onNormalEnd() {}
	
	public void onAborted() {}
	
	/**
	 * onFinalize will be called when either onNormalEnd and onAborted is called.
	 */
	public void onFinalize() {}
	
	//---
	
	//---Events to drive this action
	
	public void startAction() {
		onStart();
	}
	
	public void finishAction() {
		disposed = true;
		onNormalEnd();
		onFinalize();
	}
	
	public void abortAction() {
		disposed = true;
		onAborted();
		onFinalize();
	}
	
	//---Sandbox utils
	public EntityPlayer getPlayer() {
		return itemInfo.getPlayer();
	}
	
	public NBTTagCompound getData() {
		return itemInfo.dataTag();
	}
	
	public boolean isRemote() {
		return getPlayer().worldObj.isRemote;
	}
}
