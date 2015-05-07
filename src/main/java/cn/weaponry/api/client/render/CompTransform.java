package cn.weaponry.api.client.render;

import org.lwjgl.opengl.GL11;

import cn.weaponry.core.blob.VecUtils;
import net.minecraft.util.Vec3;

public class CompTransform {
	
	public Vec3 transform = Vec3.createVectorHelper(0, 0, 0);
	
	public Vec3 pivotPt = Vec3.createVectorHelper(0, 0, 0);
	
	public Vec3 rotation = Vec3.createVectorHelper(0, 0, 0);
	
	public double scale = 1.0;
	
	private CompTransform stack;
	
	public void setPivotPt(double x, double y, double z) {
		pivotPt.xCoord = x;
		pivotPt.yCoord = y;
		pivotPt.zCoord = z;
	}
	
	public void doTransform() {
		VecUtils.glTranslate(VecUtils.add(transform, pivotPt));
		
		GL11.glRotated(rotation.zCoord, 0, 0, 1);
		GL11.glRotated(rotation.yCoord, 0, 1, 0);
		GL11.glRotated(rotation.xCoord, 1, 0, 0);
		
		GL11.glScaled(scale, scale, scale);
		
		VecUtils.glTranslate(VecUtils.neg(pivotPt));
	}
	
	public void store() {
		if(stack == null) 
			stack = new CompTransform();
		stack.transform = VecUtils.copy(transform);
		stack.pivotPt = VecUtils.copy(pivotPt);
		stack.rotation = VecUtils.copy(rotation);
		stack.scale = scale;
	}
	
	public void restore() {
		transform = VecUtils.copy(stack.transform);
		pivotPt = VecUtils.copy(stack.pivotPt);
		rotation = VecUtils.copy(stack.rotation);
		scale = stack.scale;
	}
	
}
