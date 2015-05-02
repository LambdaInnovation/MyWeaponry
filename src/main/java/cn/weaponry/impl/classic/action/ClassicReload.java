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
package cn.weaponry.impl.classic.action;

import cn.weaponry.api.action.Action;
import cn.weaponry.api.ammo.ReloadStrategy;
import cn.weaponry.core.blob.SoundUtils;
import cn.weaponry.impl.classic.WeaponClassic;

/**
 * @author WeAthFolD
 *
 */
public class ClassicReload extends Action {
	
	public String
		startSound,
		failSound,
		endSound;
	
	public int reloadTime = 20;
	
	ReloadStrategy rs;

	//TODO Animation
	public void onStart() {
		rs = getWeapon().reloadStrategy;
		
		if(rs.canReload(getPlayer(), getStack())) {
			SoundUtils.playBothSideSound(getPlayer(), startSound);
		} else {
			SoundUtils.playBothSideSound(getPlayer(), failSound);
			disposed = true;
		}
	}
	
	public void tick(int tick) {
		if(tick == reloadTime) {
			SoundUtils.playBothSideSound(getPlayer(), endSound);
			rs.doReload(getPlayer(), getStack());
			disposed = true;
		}
	}
	
	private WeaponClassic getWeapon() {
		return (WeaponClassic) itemInfo.getStack().getItem();
	}
	
	@Override
	public String getName() {
		return "ClassicReload";
	}

}
