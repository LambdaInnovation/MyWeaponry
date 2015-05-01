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
import cn.weaponry.api.item.WeaponBase;
import cn.weaponry.api.state.WeaponState;
import cn.weaponry.api.state.WeaponStateMachine;

/**
 * @author WeAthFolD
 *
 */
public class WeaponTest extends WeaponBase {
	
	public WeaponTest() {
		setCreativeTab(AcademyCraft.cct);
		setUnlocalizedName("ttt");
	}

	@Override
	public void initStates(WeaponStateMachine machine) {
		machine.addState("state1", new State1());
		machine.addState("state2", new State2());
		machine.setInitState("state1");
	}
	
	static class State1 extends WeaponState {
		int ticker = 0;
		
		public void enterState() {
			//System.out.println("Enter state1 #" + isRemote());
		}
		
		@Override
		public void onCtrl(int key, KeyEventType type) {
			System.out.println(key + " " + type + " via " + isRemote());
		}
		
		public void tickState() {
			if(++ticker == 20) {
				ticker = 0;
				//transitState("state2");
			}
		}
		
		public void leaveState() {
			//System.out.println("Leave state1 #" + isRemote());
		}
	}
	
	static class State2 extends WeaponState {
		int ticker = 0;
		
		public void enterState() {
			//System.out.println("Enter state2 #" + isRemote());
		}
		
		public void tickState() {
			if(++ticker == 20) {
				ticker = 0;
				//transitState("state1");
			}
		}
		
		public void leaveState() {
			//System.out.println("Leave state2 #" + isRemote());
		}
	}

}
