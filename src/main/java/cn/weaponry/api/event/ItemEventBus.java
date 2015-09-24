package cn.weaponry.api.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.weaponry.api.runtime.ItemInfo;
import cn.weaponry.core.Weaponry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ItemEventBus implements IItemEventHandler {
	
	private Map<Class, List<IItemEventHandler>> firstClass = new HashMap();
	private Map<Class, List<IItemEventHandler>> cached = new HashMap();

	/**
	 * It should be guaranteed that all event handler is added BEFORE handleEvent is called,
	 * for the method uses caching to boost up calling.
	 */
	@Override
	public void handleEvent(ItemInfo info, Object event) {
		List<IItemEventHandler> list = cached.get(event.getClass());
		if(list == null) { // Build the cache
			list = new ArrayList();
			Class temp = event.getClass();
			while(temp != null) {
				List<IItemEventHandler> direct = firstClass.get(temp);
				if(direct != null)
					list.addAll(direct);
				temp = temp.getSuperclass();
			}
			cached.put(event.getClass(), list);
		}
		
		for(IItemEventHandler handler : list) {
			handler.handleEvent(info, event);
		}
	}
	
	/**
	 * Scan any method with 'ItemCallback' annotation and add them as event handler.
	 */
	public void load(Object callbackProvider) {
		for(Method m : callbackProvider.getClass().getMethods()) {
			if(m.isAnnotationPresent(ItemCallback.class)) {
				ItemCallback anno = m.getAnnotation(ItemCallback.class);
				addRawHandler(m.getParameterTypes()[1], new Wrapper(callbackProvider, m, anno.side() == Side.CLIENT));
			}
		}
	}
	
	public <T> void addRawHandler(Class<T> eventType, IItemEventHandler<T> handler) {
		// Lazy initialize the list
		List<IItemEventHandler> list = firstClass.get(eventType);
		if(list == null) {
			list = new ArrayList();
			firstClass.put(eventType, list);
		}
		list.add(handler);
	}
	
	private class Wrapper implements IItemEventHandler {
		
		final Object instance;
		final Method method;
		final boolean isRemote;
		
		public Wrapper(Object _instance, Method _method, boolean _isRemote) {
			instance = _instance;
			method = _method;
			isRemote = _isRemote;
		}

		@Override
		public void handleEvent(ItemInfo info, Object event) {
			if(info.isRemote() != isRemote)
				return;
			try {
				method.invoke(instance, info, event);
			} catch(Exception e) {
				Weaponry.log.error("An error occured while handling wrapped event calls", e);
			}
		}
		
	}
	
}
