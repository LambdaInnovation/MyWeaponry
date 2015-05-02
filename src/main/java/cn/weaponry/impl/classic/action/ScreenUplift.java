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

import javax.vecmath.Vector2d;

import net.minecraft.entity.player.EntityPlayer;
import cn.liutils.util.GenericUtils;
import cn.weaponry.api.action.Action;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents a single time of uplift.
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
public class ScreenUplift extends Action {
	
	//TODO: Open up the rad limit?
	public double upliftRadius = 10; //Expected uplift radius. For better experience use RangeRandom(rad*0.8, rad*1.2).
	public double recoverSpeed = 4; //Angle recover speed, unit: deg/tick
	
	public double degreeFrom = 30, degreeTo = 150; //The allowed "uplift" vector scatter range, in degrees.
	
	private Vector2d dir;
	private double uplift;
	
	private int tick = 0, maxTick;

	@Override
	public String getName() {
		return "Uplift";
	}
	
	@Override
	public void onStart() {
		double rad = GenericUtils.randIntv(degreeFrom, degreeTo) * Math.PI / 180;
		dir = new Vector2d(Math.sin(rad), Math.cos(rad));
		
		uplift = GenericUtils.randIntv(0.8, 1.2) * upliftRadius;
		
		EntityPlayer player = getPlayer();
		player.rotationYaw += dir.y * uplift;
		player.rotationPitch += dir.x * uplift;
		
		maxTick = (int) Math.round(uplift / recoverSpeed);
	}
	
	@Override
	public void onTick(int tick) {
		EntityPlayer player = getPlayer();
		player.rotationYaw -= dir.y * recoverSpeed;
		player.rotationPitch -= dir.x * recoverSpeed;
		
		if(++tick == maxTick) {
			this.disposed = true;
		}
	}

}
