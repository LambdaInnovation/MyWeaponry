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
package cn.weaponry.api.client.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import cn.weaponry.core.blob.VecUtils;

/**
 * @author WeAthFolD
 */
public abstract class PartedModel {

	Map<String, CompTransform> transformData = new HashMap();
	
	public PartedModel(String ...partNames) {
		for(String s : partNames) {
			regPart(s);
		}
	}
	
	protected void regPart(String name) {
		transformData.put(name, new CompTransform());
	}
	
	public void renderAll() {
		for(String s : transformData.keySet()) {
			renderPart(s);
		}
	}
	
	public void pushTransformState() {
		for(CompTransform t : transformData.values()) {
			t.store();
		}
	}
	
	public void popTransformState() {
		for(CompTransform t : transformData.values()) {
			t.restore();
		}
	}
	
	/**
	 * Render the part at the origin, then peform the following transform sequence:
	 * move to pivot->scale->rotate->transform->move back from pivot
	 * @throws NullPointerException if no such model part
	 * @param name The part name
	 */
	public void renderPart(String name) {
		CompTransform t = transformData.get(name);
		GL11.glPushMatrix();
		
		t.doTransform();
		renderAtCenter(name);
		
		GL11.glPopMatrix();
	}
	
	public CompTransform getTransform(String name) {
		return transformData.get(name);
	}
	
	protected abstract void renderAtCenter(String name);
	
	public static interface Callback {
		public void invoke();
	}
	
}