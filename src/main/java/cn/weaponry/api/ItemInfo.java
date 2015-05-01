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
package cn.weaponry.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cn.weaponry.api.action.Action;

/**
 * RUNTIME item info. A instance of an ItemInfo represents some certain info provided by the
 * Flyweight instance of an ItemStack currently holding by an EntityPlayer. 
 * It will automatically get created when player changes to a ItemStack that is of type <code>IItemInfoProvider</code>.
 * When player changes slot or drop the stack, the info is disposed right away. <br/>
 * ItemInfo used Component pattern to handle actions provided by Items(see <code>Action</code>). You can provide action either
 * at the init of the ItemInfo or at any dynamic time. You can see this class as a driven system of Action.
 * @author WeAthFolD
 */
public class ItemInfo {
	
	private final EntityPlayer player;
	private final int slot;
	
	private ItemStack lastStack;
	
	private NBTTagCompound dataTag;
	
	/**
	 * For internal usage. DONT touch it!
	 */
	public boolean disposed;
	
	private boolean firstUpdate = true;
	
	List<Action> actions = new ArrayList();
	
	ItemInfo(EntityPlayer _player) {
		player = _player;
		slot = player.inventory.currentItem;
		
		lastStack = player.getCurrentEquippedItem();
		if(lastStack == null) {
			disposed = true;
		}
		
		System.out.println("Created ItemInfo");
	}
	
	public void tick() {
		checkStack();
		
		if(disposed)
			return;
		
		if(firstUpdate) {
			firstUpdate = false;
			((IItemInfoProvider)lastStack.getItem()).onInfoStart(this);
		}
		
		//Send action events
		Iterator<Action> iter = actions.iterator();
		while(iter.hasNext()) {
			Action act = iter.next();
			if(act.disposed) {
				iter.remove();
			} else {
				act.onTick();
			}
		}
	}
	
	public void onDisposed() {
		for(Action a : actions) {
			if(!a.disposed) {
				a.abortAction();
			}
		}
	}
	
	void checkStack() {
		ItemStack cur = player.getCurrentEquippedItem();
		if(player.inventory.currentItem != slot || cur == null || cur.getItem() != lastStack.getItem()) {
			disposed = true;
			return;
		}
		
		lastStack = cur;
	}
	
	public NBTTagCompound dataTag() {
		if(dataTag == null)
			dataTag = new NBTTagCompound();
		return dataTag;
	}
	
	public void addAction(Action action) {
		if(action.itemInfo != null) {
			throw new RuntimeException("Trying to add one action into multiple instances");
		}
		
		action.itemInfo = this;
		action.onStart();
		actions.add(action);
	}
	
	/**
	 * Find the first occurrence of the name.
	 */
	public <T extends Action> T getAction(String name) {
		for(Action a : actions) {
			if(a.getName().equals(name))
				return (T) a;
		}
		return null;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
}