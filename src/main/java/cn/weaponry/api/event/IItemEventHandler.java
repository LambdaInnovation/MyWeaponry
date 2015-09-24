package cn.weaponry.api.event;

import cn.weaponry.api.runtime.ItemInfo;

/**
 * implemented by Item subclasses to handle events ent by EventSystem.
 * For fast implementation check out ItemEventBus.
 */
public interface IItemEventHandler<T> {
	
	void handleEvent(ItemInfo info, T event);
	
}
