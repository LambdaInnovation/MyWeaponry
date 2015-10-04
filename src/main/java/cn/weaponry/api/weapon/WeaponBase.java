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
package cn.weaponry.api.weapon;

import cn.weaponry.api.client.animation.ItemAnimator;
import cn.weaponry.api.ctrl.IControllableItem;
import cn.weaponry.api.ctrl.KeyPhase;
import cn.weaponry.api.event.EventSystem;
import cn.weaponry.api.event.IItemEventHandler;
import cn.weaponry.api.event.ItemEventBus;
import cn.weaponry.api.runtime.IRequiresInfo;
import cn.weaponry.api.runtime.ItemInfo;
import cn.weaponry.api.runtime.StateMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;

/**
 * @author WeAthFolD
 */
public abstract class WeaponBase extends Item implements IRequiresInfo, IItemEventHandler, IControllableItem {
	
	// API INTERFACES
	protected boolean useStateMachine = true;
	
	protected void initStates(StateMachine fsm) {}

	// UTILS
	/**
	 * Post this event to the item event bus, at the same time send it to server and all nearby clients. 
	 * CLIENT only.
	 */
	public void postEvent(ItemInfo info, Object event) {
		postEvent(info, event, true, true);
	}
	
	/**
	 * Post this event to the item event bus.
	 * @param send Whether we also send this event to server
	 */
	public void postEvent(ItemInfo info, Object event, boolean send, boolean rebroadcast) {
		eventBus.handleEvent(info, event);
		if(send) {
			if(rebroadcast)
				EventSystem.sendToServerRebroadcast(info, event);
			else
				EventSystem.sendToServer(info, event);
		}
	}
	
	// HANDLED PROCESSES
	protected final ItemEventBus eventBus;
	
	public WeaponBase() {
		eventBus = new ItemEventBus();
		eventBus.load(this);
	}
	
	@Override
	public void onKeyEvent(ItemInfo info, int keyID, KeyPhase phase) {
		if(useStateMachine) {
			StateMachine fsm = info.findAction(StateMachine.ID);
			fsm.delegateControl(keyID, phase);
		}
	}

	@Override
	public void handleEvent(ItemInfo info, Object event) {
		eventBus.handleEvent(info, event);
	}

	@Override
	public void onInfoCreated(ItemInfo info) {
		if(useStateMachine) {
			StateMachine fsm = new StateMachine();
			initStates(fsm);
			info.addAction(fsm);
		}
		if(info.isRemote()) {
			onInfoCreatedClient(info);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void onInfoCreatedClient(ItemInfo info) {
		info.addAction(new ItemAnimator());
	}

	@Override
	public void onInfoDestroyed(ItemInfo info) {
		// NOPE
	}

}
