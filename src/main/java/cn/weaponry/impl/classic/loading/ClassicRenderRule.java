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
package cn.weaponry.impl.classic.loading;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.obj.WavefrontObject;
import cn.liutils.loading.Loader.ObjectNamespace;
import cn.liutils.loading.item.ItemLoadRule;
import cn.liutils.loading.item.ItemLoader;
import cn.weaponry.api.client.render.CompTransform;
import cn.weaponry.api.client.render.PartedObjModel;
import cn.weaponry.api.client.render.RendererWeapon;
import cn.weaponry.core.Weaponry;
import cn.weaponry.impl.classic.WeaponClassic;

/**
 * Provided a chance for subclasses to redirect the searching.
 * @author WeAthFolD
 */
public class ClassicRenderRule extends ItemLoadRule<WeaponClassic> {
	
	protected WeaponClassic item;
	protected ObjectNamespace ns;
	protected String name;

	@Override
	public void load(WeaponClassic item, ObjectNamespace ns, String name)
			throws Exception {
		this.item = item;
		this.ns = ns;
		this.name = name;
		
		WavefrontObject obj = loadModel(); //Currently just support the .obj
		if(obj != null) {
			//Create the renderer and bind it!
			RendererWeapon render = new RendererWeapon(new PartedObjModel(obj), loadTexture());
			//Load the comp transform
			lookComp(render, render.stdTransform, "t_std");
			lookComp(render, render.fpTransform, "t_firstPerson");
			lookComp(render, render.tpTransform, "t_thirdPerson");
			lookComp(render, render.entityItemTransform, "t_entityItem");
			
			MinecraftForgeClient.registerItemRenderer(item, render);
		} else {
			Weaponry.log.error("WeaponClassic Render Rule: Model lookup failed for " + name);
		}
	}
	
	protected WavefrontObject loadModel() {
		return new WavefrontObject(new ResourceLocation(ns.getString("render", "model")));
	}
	
	protected ResourceLocation loadTexture() {
		return new ResourceLocation(ns.getString("render", "texture"));
	}
	
	protected void lookComp(RendererWeapon render, CompTransform ct, String compName) {
		// name/render/<compName>/
		// transform
		// pivot->pivotPt
		// rotation
		Vec3 vec;
		
		vec = lookVector("render", compName, "transform");
		if(vec != null) ct.transform = vec;
		
		vec = lookVector("render", compName, "pivot");
		if(vec != null) ct.pivotPt = vec;
		
		vec = lookVector("render", compName, "rotation");
		if(vec != null) ct.rotation = vec;
		
		Double scale = ns.getDouble("render", "scale");
		if(scale != null) ct.scale = scale;
	}
	
	private Vec3 lookVector(String ...base) {
		Object[] look = new String[base.length + 1];
		System.arraycopy(base, 0, look, 0, base.length);
		Double x, y, z;
		
		look[base.length] = "x";
		x = ns.getDouble(look);
		
		look[base.length] = "y";
		y = ns.getDouble(look);
		
		look[base.length] = "z";
		z = ns.getDouble(look);
		
		if(x != null && y != null && z != null)
			return Vec3.createVectorHelper(x, y, z);
		return null;
	}

}
