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

import cn.weaponry.api.action.InstantAction;
import cn.weaponry.impl.classic.entity.EntityBullet;

/**
 * @author WeAthFolD
 */
public class ClassicShoot extends InstantAction {
	
	public int damage = 5;
	public int scatter = 10;

	@Override
	public String getName() {
		return "ClassicShoot";
	}

	@Override
	public void execute() {
		if(!isRemote()) {
			getWorld().spawnEntityInWorld(new EntityBullet(getPlayer(), scatter, damage));
		}
	}

}
