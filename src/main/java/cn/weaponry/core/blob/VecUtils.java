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


import java.util.Random;

import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author WeAthFolD
 *
 */
public class VecUtils {
	
	private static Random rand = new Random();
	
	// mat * [x, y, z, 1](Transpose)
	public static Vec3 applyMatrix(Matrix4f mat, Vec3 vec) {
		double x = vec.xCoord * mat.m00 + vec.yCoord * mat.m10 + vec.zCoord * mat.m20 + mat.m30,
				y = vec.xCoord * mat.m01 + vec.yCoord * mat.m11 + vec.zCoord * mat.m21 + mat.m31,
				z = vec.zCoord * mat.m02 + vec.yCoord * mat.m12 + vec.zCoord * mat.m22 + mat.m32 ;
		return Vec3.createVectorHelper(x, y, z);
	}
	
	public static Vector3f asV3f(Vec3 vec) {
		return new Vector3f((float)vec.xCoord, (float)vec.yCoord, (float)vec.zCoord);
	}
	
	public static Vec3 random() {
		return vec(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
	}
	
	public static Vec3 vec(double x, double y, double z) {
		return Vec3.createVectorHelper(x, y, z);
	}

	public static void glTranslate(Vec3 v) {
		GL11.glTranslated(v.xCoord, v.yCoord, v.zCoord);
	}
	
	public static void glScale(Vec3 v) {
		GL11.glScaled(v.xCoord, v.yCoord, v.zCoord);
	}
	
	public static void glRotate(double angle, Vec3 axis) {
		GL11.glRotated(angle, axis.xCoord, axis.yCoord, axis.zCoord);
	}
	
	public static Vec3 scalarMultiply(Vec3 v, double scale) {
		return Vec3.createVectorHelper(v.xCoord * scale, v.yCoord * scale, v.zCoord * scale);
	}
	
	public static Vec3 lerp(Vec3 a, Vec3 b, double lambda) {
		double ml = 1 - lambda;
		return Vec3.createVectorHelper(
			a.xCoord * ml + b.xCoord * lambda, 
			a.yCoord * ml + b.yCoord * lambda, 
			a.zCoord * ml + b.zCoord * lambda);
	}
	
	public static Vec3 neg(Vec3 v) {
		return Vec3.createVectorHelper(-v.xCoord, -v.yCoord, -v.zCoord);
	}
	
	public static Vec3 add(Vec3 a, Vec3 b) {
		return Vec3.createVectorHelper(a.xCoord + b.xCoord, a.yCoord + b.yCoord, a.zCoord + b.zCoord);
	}
	
	public static Vec3 subtract(Vec3 a, Vec3 b) {
		return add(a, neg(b));
	}
	
	public static double magnitudeSq(Vec3 a) {
		return a.xCoord * a.xCoord + a.yCoord * a.yCoord + a.zCoord * a.zCoord;
	}
	
	public static double magnitude(Vec3 a) {
		return Math.sqrt(magnitudeSq(a));
	}
	
	public static Vec3 copy(Vec3 v) {
		return Vec3.createVectorHelper(v.xCoord, v.yCoord, v.zCoord);
	}
	
	public static void copy(Vec3 from, Vec3 to) {
		to.xCoord = from.xCoord;
		to.yCoord = from.yCoord;
		to.zCoord = from.zCoord;
	}
	
	public static Vec3 crossProduct(Vec3 a, Vec3 b) {
		double 
			x0 = a.xCoord, y0 = a.yCoord, z0 = a.zCoord,
			x1 = b.xCoord, y1 = b.yCoord, z1 = b.zCoord;
		return Vec3.createVectorHelper(
			y0 * z1 - y1 * z0, 
			x1 * z0 - x0 * z1, 
			x0 * y1 - x1 * y0);
	}
	
}
