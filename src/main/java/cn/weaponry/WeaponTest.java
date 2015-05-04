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
import cn.weaponry.api.ctrl.KeyEventType;
import cn.weaponry.api.state.WeaponState;
import cn.weaponry.api.state.WeaponStateMachine;
import cn.weaponry.core.TestItems;
import cn.weaponry.impl.classic.WeaponClassic;
import cn.weaponry.impl.generic.action.SwingSilencer;

/**
 * @author WeAthFolD
 *
 */
public class WeaponTest extends WeaponClassic {
	
	public WeaponTest() {
		super(TestItems.ammoTest);
		
		setCreativeTab(AcademyCraft.cct);
		setUnlocalizedName("ttt");
		
		shootSound = "weaponry:rifle_fire";
		
		reloadStartSound = "weaponry:rifle_magout";
		reloadAbortSound = "weaponry:rifle_jam";
		reloadEndSound = "weaponry:rifle_magin";
		
		jamSound = "weaponry:rifle_jam";
		
		this.shootInterval = 3;
	}
	
	@Override
	public void initStates(WeaponStateMachine machine) {
		super.initStates(machine);
		machine.addState("action", new StateStockAttack());
	}
	
	public static class StateStockAttack extends WeaponState {
		public void enterState() {
			SwingSilencer silencer = getItem().getAction("SwingSilencer");
			if(silencer != null) {
				silencer.active = false;
			}
			getPlayer().swingItem();
		}
		
		@Override
		public void onCtrl(int key, KeyEventType type) {
			//transitState("idle");
		}
		
		@Override
		public void tickState(int ticks) {
			if(ticks == 15) {
				transitState("idle");
			}
		}
		
 		public void leaveState() {
			SwingSilencer silencer = getItem().getAction("SwingSilencer");
			if(silencer != null) {
				silencer.active = true;
			}
		}
	}

}
