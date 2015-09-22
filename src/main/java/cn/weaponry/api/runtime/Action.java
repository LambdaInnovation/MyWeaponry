package cn.weaponry.api.runtime;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Action {
	
	public final String id;
	
	// Assigned when added into an ItemInfo.
	ItemInfo info;
	
	// Disposed actions will be removed next tick if alive.
	boolean disposed;
	
	public Action() {
		this("_default");
	}
	
	public Action(String _id) {
		id = _id;
	}
	
	/**
	 * Called when added into an ItemInfo.
	 */
	public void onStart() {
		
	}
	
	/**
	 * Called when active in an ItemInfo every tick.
	 */
	public void onTick() {
		
	}
	
	/**
	 * Called when removed from an ItemInfo.
	 */
	public void onEnd() {
		
	}
	
	/**
	 * Called when an ItemInfo is being reset and the action is not disposed.
	 */
	public void onAbort() {
		
	}
	
	/**
	 * Called after both onEnd() and onAbort().
	 */
	public void onFinalize() {
		
	}
	
	/**
	 * Dispose a running action. Will get removed next tick.
	 */
	public void dispose() {
		disposed = true;
	}
	
	public ItemInfo getInfo() {
		return this.info;
	}
	
	public EntityPlayer getPlayer() {
		return info.getPlayer();
	}
	
	public World getWorld() {
		return info.getPlayer().worldObj;
	}
	
}
