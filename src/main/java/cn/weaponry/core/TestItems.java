package cn.weaponry.core;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegItem;
import cn.weaponry.WeaponTest;
import cn.weaponry.api.client.render.PartedObjModel;
import cn.weaponry.api.client.render.RendererWeapon;

@RegistrationClass
public class TestItems {
	@RegItem
	public static Item ammoTest = new Item();
	
	@RegItem
	@RegItem.HasRender
	public static WeaponTest test = new WeaponTest() {
		WavefrontObject obj = (WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation("weaponry:models/rifle.obj"));
		@RegItem.Render
		public RendererWeapon renderer = new RendererWeapon(new PartedObjModel(obj), new ResourceLocation("weaponry:textures/models/rifle.png"));
	};
}
