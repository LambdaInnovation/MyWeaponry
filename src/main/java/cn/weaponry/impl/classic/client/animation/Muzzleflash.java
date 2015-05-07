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

import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.prop.DisableCullFace;
import cn.liutils.api.draw.prop.DisableLight;
import cn.liutils.api.draw.tess.Rect;
import cn.liutils.loading.Loader.ObjectNamespace;
import cn.liutils.util.RenderUtils;
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
	public long time = 200;
	
	static DrawObject drawObject;
	static Rect rect;
	static {
		drawObject = new DrawObject();
		rect = new Rect();
		drawObject.addHandler(rect);
		drawObject.addHandler(DisableLight.instance());
		drawObject.addHandler(DisableCullFace.instance());
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
		
		Double d = ns.getDouble("render", "muzzleflash", "x");
		if(d != null) {
			x = d;
		}
		d = ns.getDouble("render", "muzzleflash", "y");
		if(d != null) {
			y = d;
		}
		d = ns.getDouble("render", "muzzleflash", "z");
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
		GL11.glPushMatrix();
		RenderUtils.loadTexture(texture == null ? MISSING : texture);
		GL11.glTranslated(x, y, z);
		
		rect.setSize(size, size);
		rect.setCentered();
		GL11.glColor4d(1, 1, 1, 0.7);
		drawObject.draw();
		GL11.glColor4d(1, 1, 1, 1);
		
		GL11.glPopMatrix();
	}
	
	public <T extends Animation> T copy() {
		
		T copy = super.copy();
		System.out.println(this.texture + " " + ((Muzzleflash)copy).texture);
		return copy;
	}
	
}
