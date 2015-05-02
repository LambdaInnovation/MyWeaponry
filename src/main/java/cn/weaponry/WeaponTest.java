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
package cn.weaponry;

import cn.academy.core.AcademyCraft;
import cn.weaponry.core.Weaponry;
import cn.weaponry.impl.classic.WeaponClassic;

/**
 * @author WeAthFolD
 *
 */
public class WeaponTest extends WeaponClassic {
	
	public WeaponTest() {
		super(Weaponry.ammoTest);
		
		setCreativeTab(AcademyCraft.cct);
		setUnlocalizedName("ttt");
		
		abortSound = "weaponry:rifle_jam";
		shootSound = "weaponry:rifle_fire";
		
		reloadAction.startSound = "weaponry:rifle_magout";
		reloadAction.failSound = "weaponry:rifle_jam";
		reloadAction.endSound = "weaponry:rifle_magin";
	}

}
