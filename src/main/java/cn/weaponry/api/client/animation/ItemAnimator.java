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
package cn.weaponry.api.client.animation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.liutils.util.helper.GameTimer;
import cn.liutils.vis.animation.Animation;
import cn.weaponry.api.runtime.Action;
import cn.weaponry.api.runtime.ItemInfo;
import cn.weaponry.core.Weaponry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
public class ItemAnimator extends Action {
	
	public static ItemAnimator get(ItemInfo info) {
		return info.findAction("Animator");
	}
	
	public ItemAnimator() {
		super("Animator");
	}
	
	private List<AnimStatus> active = new LinkedList();
	
	public void addAnimation(Animation a) {
		addAnimation(a, -1);
	}
	
	public void addAnimation(Animation a, double life) {
		a.disposed = false;
		a.onStarted();
		Weaponry.log.info("Added anim " + a);
		active.add(new AnimStatus(a, life));
	}
	
	public <T extends Animation> T getByClass(Class<T> type) {
		for(AnimStatus a : active)
			if(!a.anim.disposed && type.isInstance(a.anim))
				return (T) a.anim;
		return null;
	}
	
	/**
	 * Additional callback, called via renderer to perform the actions.
	 */
	public void onRenderFrame() {
		for(AnimStatus as : active) {
			if(!as.anim.disposed) {
				as.anim.perform(as.deltaTime());
			}
		}
	}
	
	/**
	 * Do lifetime update in ticks
	 */
	@Override
	public void onTick() {
		Iterator<AnimStatus> iter = active.iterator();
		while(iter.hasNext()) {
			AnimStatus as = iter.next();
			if(as.anim.disposed || (as.life > 0 && as.deltaTime() > as.life)) {
				Weaponry.log.info("Removed anim " + as.anim);
				iter.remove();
			}
		}
	}
	
	private class AnimStatus {
		final Animation anim;
		final long creationTime = GameTimer.getTime();
		final double life;
		
		AnimStatus(Animation _anim, double _life) {
			anim = _anim;
			life = _life;
		}
		
		double deltaTime() {
			return (GameTimer.getTime() - creationTime) / 1000.0;
		}
	}
	
}
