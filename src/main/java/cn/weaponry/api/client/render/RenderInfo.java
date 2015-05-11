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
package cn.weaponry.api.client.render;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;

import org.apache.commons.lang3.NotImplementedException;

import cn.liutils.loading.Loader.ObjectNamespace;
import cn.liutils.util.ReflectUtils;
import cn.weaponry.api.ItemInfo;
import cn.weaponry.api.action.Action;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Runtime weapon rendering info. Used to dispatch render events and enables cool stuffs such as muzzle flash + particle effects.
 * @author WeAthFolD
 */
public class RenderInfo extends Action {
	
	Map<String, Animation> callbacks = new HashMap();
	
	public void addAnimation(String name, Animation cb) {
		if(callbacks.containsKey(name)) {
			return;
			//throw new IllegalStateException("Duplicate name " + name);
		}
		callbacks.put(name, cb);
		cb.onStart(itemInfo);
	}
	
	public void addAnimation(Animation cb) {
		addAnimation(nextFreeName(), cb);
	}
	
	private String nextFreeName() {
		String str = "un";
		String res = str + 0;
		for(int i = 1; callbacks.containsKey(res);++i) {
			res = str + i;
		}
		return res;
	}
	
	public void removeCallback(String name) {
		Animation t = callbacks.get(name);
		if(t != null)
			t.disposed = true;
	}
	
	@Override
	public void onTick(int tick) {
		Iterator<Animation> iter = callbacks.values().iterator();
		while(iter.hasNext()) {
			Animation irc = iter.next();
			if(irc.disposed) {
				iter.remove();
			}
		}
	}
	
//	public Collection<Animation> getCallbacks() {
//		return callbacks.values();
//	}
	
	public void renderAll(PartedModel model, boolean firstPerson, int pass) {
		for(Animation a : callbacks.values()) {
			if(!a.disposed && a.shouldRenderInPass(pass)) {
				a.onRender(itemInfo, model, firstPerson);
			}
		}
	}
	
	private String nextName() {
		String n;
		int i = 0;
		do {
			n = String.valueOf("u" + i);
		} while(!callbacks.containsKey(n));
		return n;
	}

	@Override
	public String getName() {
		return "RenderInfo";
	}
	
	public static RenderInfo get(ItemInfo ii) {
		return ii.getAction("RenderInfo");
	}
	
	/**
	 * Each animation is simply a callback that is called
	 * each render tick when rendering weapon. You can either draw stuffs
	 * or do translation in it.
	 * When draw stuffs, use pass 1; When translating, use pass 0.
	 * @author WeAthFolD
	 *
	 */
	@SideOnly(Side.CLIENT)
	public static abstract class Animation {
		
		public boolean disposed = false;
		
		long beginTime;
		
		protected long lifeTime = Long.MAX_VALUE;
		
		public Animation() {}
		
		//Sets
		public Animation setLifetime(long time) {
			lifeTime = time;
			return this;
		}
		
		//Callback events
		public void start(ItemInfo info) {}
		
		public void render(ItemInfo info, PartedModel model, boolean firstPerson) {}
		
		//Driven events
		public final void onStart(ItemInfo info) {
			beginTime = Minecraft.getSystemTime();
			start(info);
		}
		
		public final void onRender(ItemInfo info, PartedModel model, boolean firstPerson) {
			if(getTime() > lifeTime) {
				disposed = true;
			} else {
				render(info, model, firstPerson);
			}
		}
		
		//Utils
		public long getTime() {
			return Minecraft.getSystemTime() - beginTime;
		}
		
		public boolean shouldRenderInPass(int pass) {
			return pass == 1;
		}
		
		public <T extends Animation> T copy() {
			try {
				return (T) ReflectUtils.copy(this);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public void load(ObjectNamespace ns) {
			throw new NotImplementedException("Can't load this animation");
		}
		
	}

}
