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
package cn.weaponry.impl.classic.client.animation;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.loading.Loader.ObjectNamespace;
import cn.liutils.render.material.SimpleMaterial;
import cn.liutils.render.mesh.Mesh;
import cn.liutils.render.mesh.MeshUtils;
import cn.liutils.util.client.RenderUtils;
import cn.weaponry.api.ItemInfo;
import cn.weaponry.api.client.render.PartedModel;
import cn.weaponry.api.client.render.RenderInfo.Animation;

/**
 * @author WeAthFolD
 *
 */
public class Muzzleflash extends Animation {
	
	//TODO: If need animation sequence, change it
	private static ResourceLocation MISSING = new ResourceLocation("missing");
	public ResourceLocation texture;
	public double x = 0.7, y = 0.3, z = 0;
	public double size = 0.45;
	public long time = 160;
	
	static Mesh mesh;
	static SimpleMaterial material;
	static {
		//drawObject.addHandler(DisableCullFace.instance());
		mesh = MeshUtils.createBillboard(null, -.5, -.5, .5, .5);
		material = new SimpleMaterial(null).setIgnoreLight();
	}
	
	public Muzzleflash() {
		this.setLifetime(time);
	}
	
	public void load(ObjectNamespace ns) {
		String str = ns.getString("render", "muzzleflash", "texture");
		if(str != null) {
			System.out.println("Update muzzleflash path for " + ns.name);
			texture = new ResourceLocation(str);
		}
		
		Double d = ns.getDouble("render", "muzzleflash", "offset", 0);
		if(d != null) {
			x = d;
		}
		d = ns.getDouble("render", "muzzleflash", "offset", 1);
		if(d != null) {
			y = d;
		}
		d = ns.getDouble("render", "muzzleflash", "offset", 2);
		if(d != null) {
			z = d;
		}
		d = ns.getDouble("render", "muzzleflash", "size");
		if(d != null) {
			size = d;
		}
		
		Integer t = ns.getInt("render", "muzzleflash", "time");
		if(t != null) {
			time = t;
		}
	}
	
	@Override
	public void render(ItemInfo info, PartedModel model, boolean firstPerson) {
		double alpha = 1.0;
		
		//Blend in
		long dt = getTime();
		if(dt < 40) {
			alpha = dt / 40.0;
		} else if(dt > lifeTime - 40.0) {
			alpha = (lifeTime - dt) / 40.0;
		}
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		RenderUtils.loadTexture(texture == null ? MISSING : texture);
		GL11.glTranslated(x, y, z);
		
		material.setTexture(texture);
		GL11.glScaled(size, size, size);
		material.color.a = alpha * 0.8;
		GL11.glRotatef(90, 0, 1, 0);
		mesh.draw(material);
		GL11.glColor4d(1, 1, 1, 1);
		
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	public <T extends Animation> T copy() {
		T copy = super.copy();
		return copy;
	}
	
}
