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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
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
	
	private double oldX, oldY, oldZ;
	 
	public void onStart() {
		WeaponClassic weapon = (WeaponClassic) itemInfo.getItemType();
		this.currentScatter = weapon.shootScatterMin;
		this.shootScatterMin = weapon.shootScatterMin;
		this.shootScatterMax = weapon.shootScatterMax;
		this.shootScatterStability = weapon.shootScatterStability;
		if(this.shootScatterStability < 0)
			this.shootScatterStability = 0;
		this.count = 0;
		EntityPlayer player = itemInfo.getPlayer();
		this.oldX = player.posX;
		this.oldY = player.posY;
		this.oldZ = player.posZ;
	}

	public void onTick(int tick) {
		//Reference:1-11, 2-8, 3-6
		WeaponStateMachine machine = (WeaponStateMachine) itemInfo.getAction("StateMachine");
		EntityPlayer player = itemInfo.getPlayer();
		//System.out.println(moving);
		if(machine != null){
			if(machine.getCurrentState() != null && machine.getCurrentState() instanceof StateShoot){
				cooldown = 5;
			}else{
				double downMulti = 10;
				Vec3 motion = Vec3.createVectorHelper(player.posX - oldX, player.posY - oldY, player.posZ - oldZ);
				if(motion.lengthVector() > 0)
					System.out.println(motion.lengthVector());
				boolean moving = motion.lengthVector() > 0.1;
				
				if(moving){
					cooldown = 5;
					downMulti = 100;
					
					int multi = 3;
					if(player.isSprinting())
						multi = 2;
					if(player.isSneaking())
						multi = 10;
					double maxScatter = shootScatterMin + (shootScatterMax - shootScatterMin)/multi,
							phase = (shootScatterMax - shootScatterMin)/(multi * 10);
					//System.out.println(currentScatter);
					//System.out.println(maxScatter);
					if(currentScatter < maxScatter ) {
						currentScatter += phase;
						if(currentScatter > maxScatter)
							currentScatter = maxScatter;
					}
					oldX = player.posX;
					oldY = player.posY;
					oldZ = player.posZ;
				}
				
				if(cooldown != 0)
					cooldown--;
				else{
					count = 0;
					if(currentScatter > shootScatterMin){
						currentScatter -= (shootScatterMax - shootScatterMin)/downMulti;
						if(currentScatter < shootScatterMin)
							currentScatter = shootScatterMin;	
					}		
				}
				
			}	
		}
	}
	
	public void callShoot(){
		if(currentScatter < shootScatterMax){
			currentScatter += 0.01 + (1.25 * (Math.pow(2, shootScatterStability) - 1))/1000 * count * count;
			if(currentScatter > shootScatterMax)
				currentScatter = shootScatterMax;
		}	
		count++;
	}
	
	public double getCurrentScatter(){
		return currentScatter;
	}
	
	@Override
	public String getName() {
		return "ScatterUpdater";
	}

}
