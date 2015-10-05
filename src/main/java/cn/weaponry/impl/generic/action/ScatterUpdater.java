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
package cn.weaponry.impl.generic.action;

import cn.weaponry.api.action.Action;
import cn.weaponry.api.state.WeaponStateMachine;
import cn.weaponry.impl.classic.WeaponClassic;
import cn.weaponry.impl.classic.WeaponClassic.StateShoot;

/**
 * Used to silence Minecraft swing action.
 * @author WeAthFolD
 */
public class ScatterUpdater extends Action {
	
	private double currentScatter;
	private double shootScatterMin;
	private double shootScatterMax;
	private double shootScatterStability;
	private int count;
	private int cooldown;
	
	public void onStart() {
		WeaponClassic weapon = (WeaponClassic) itemInfo.getItemType();
		this.currentScatter = weapon.shootScatterMin;
		this.shootScatterMin = weapon.shootScatterMin;
		this.shootScatterMax = weapon.shootScatterMax;
		this.shootScatterStability = weapon.shootScatterStability;
		if(this.shootScatterStability < 0)
			this.shootScatterStability = 0;
		this.count = 0;
	}

	public void onTick(int tick) {
		//Reference:1-11, 2-8, 3-6
		WeaponStateMachine machine = (WeaponStateMachine) itemInfo.getAction("StateMachine");
		if(machine != null){
			if(machine.getCurrentState() != null && !(machine.getCurrentState() instanceof StateShoot)){
				
				if(cooldown != 0)
					cooldown--;
				else{
					count = 0;
					//System.out.println("else");
					if(currentScatter > shootScatterMin)
						currentScatter -= (shootScatterMax - shootScatterMin)/20;
					else
						currentScatter = shootScatterMin;
				}
				
			}else
				cooldown = 10;
		}
	}
	
	public double callShoot( ){
		System.out.println(count + 1);
		return getCurrentScatter();
	}
	
	public double getCurrentScatter( ){
		if(currentScatter < shootScatterMax){
			currentScatter += (1.25 * (Math.pow(2, shootScatterStability) - 1))/1000 * count * count;
			if(currentScatter > shootScatterMax)
				currentScatter = shootScatterMax;
		}	
		else
			currentScatter = shootScatterMax;
		count++;
		return currentScatter;
	}
	
	@Override
	public String getName() {
		return "ScatterUpdater";
	}

}
