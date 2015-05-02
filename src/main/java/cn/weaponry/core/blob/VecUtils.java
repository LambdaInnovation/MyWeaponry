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
package cn.weaponry.core.blob;

import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

/**
 * @author WeAthFolD
 *
 */
public class VecUtils {

	public static void glTranslate(Vec3 v) {
		GL11.glTranslated(v.xCoord, v.yCoord, v.zCoord);
	}
	
	public static void glScale(Vec3 v) {
		GL11.glScaled(v.xCoord, v.yCoord, v.zCoord);
	}
	
	public static void glRotate(double angle, Vec3 axis) {
		GL11.glRotated(angle, axis.xCoord, axis.yCoord, axis.zCoord);
	}
	
	public static Vec3 neg(Vec3 v) {
		return Vec3.createVectorHelper(-v.xCoord, -v.yCoord, -v.zCoord);
	}
	
	public static Vec3 add(Vec3 a, Vec3 b) {
		return Vec3.createVectorHelper(a.xCoord + b.xCoord, a.yCoord + b.yCoord, a.zCoord + b.zCoord);
	}
	
	public static Vec3 copy(Vec3 v) {
		return Vec3.createVectorHelper(v.xCoord, v.yCoord, v.zCoord);
	}
	
}
