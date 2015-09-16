package cn.weaponry.api.util;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class VecUtils {
	private static Random rand = new Random();
	
	public static Vec3 vec(double x, double y, double z) {
		return Vec3.createVectorHelper(x, y, z);
	}
	
	public static Vec3 random() {
		return vec(-1 + 2 * rand.nextDouble(), -1 + 2 * rand.nextDouble(), -1 + 2 * rand.nextDouble());
	}
	
	/**
	 * Convert the yaw and pitch angle to the looking direction vector
	 * @param yaw in mc entity angle space
	 * @param pitch in mc entity angle space
	 * @return the looking direction vec, normalized
	 */
	public static Vec3 toDirVector(float yaw, float pitch) {
		float var3 = 1.0f;
		float vx, vy, vz;
		vx = MathHelper.sin(yaw / 180.0F
				* (float) Math.PI)
				* MathHelper.cos(yaw / 180.0F
						* (float) Math.PI) * var3;
		vz = MathHelper.cos(yaw / 180.0F
				* (float) Math.PI)
				* MathHelper.cos(pitch / 180.0F
						* (float) Math.PI) * var3;
		vy = -MathHelper.sin((pitch)
				/ 180.0F * (float) Math.PI)
				* var3;
		return vec(vx, vy, vz);
	}
	
	public static Vec3 multiply(Vec3 v, double scale) {
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
	
	public static Vec3 entityPos(Entity e) {
		return vec(e.posX, e.posY, e.posZ);
	}
	
	public static Vec3 entityMotion(Entity e) {
		return vec(e.motionX, e.motionY, e.motionZ);
	}
}
