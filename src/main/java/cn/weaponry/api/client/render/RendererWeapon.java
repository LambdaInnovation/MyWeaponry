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

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cn.liutils.util.RenderUtils;
import cn.weaponry.api.ItemInfo;
import cn.weaponry.api.ItemInfoProxy;
import cn.weaponry.api.client.render.RenderInfo.ItemRenderCallback;

/**
 * @author WeAthFolD
 *
 */
public class RendererWeapon implements IItemRenderer {
	
	//TODO: Data heavy, use json to load those data
	CompTransform 
		stdTransform = new CompTransform(), 
		fpTransform = new CompTransform(), 
		tpTransform = new CompTransform(), 
		entityItemTransform = new CompTransform();
	
	final PartedModel model;
	final ResourceLocation texture;
	
	public RendererWeapon(PartedModel _model, ResourceLocation _texture) {
		model = _model;
		texture = _texture;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type == ItemRenderType.INVENTORY)
			return false;
		return true;
	}

	@Override
	public final boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public final void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.EQUIPPED) {
			ItemInfo info = ItemInfoProxy.getInfo(player);
			RenderInfo ri = (RenderInfo) (info == null ? null : info.getAction("RendererInfo"));
			if(ri != null) {
				handleHeldRender(ri, type == ItemRenderType.EQUIPPED_FIRST_PERSON);
			} else {
				handleSimpleRender(item);
			}
		} else {
			handleSimpleRender(item);
		}
	}
	
	private void handleHeldRender(RenderInfo info, boolean firstPerson) {
		GL11.glPushMatrix();
		{
			model.pushTransformState();
			
			for(ItemRenderCallback irc : info.getCallbacks()) {
				irc.onRender(info.itemInfo, model, firstPerson);
			}
			RenderUtils.loadTexture(texture);
			
			if(firstPerson) {
				doFirstPersonTansform();
				fpTransform.doTransform();
			} else {
				doThirdPersonTransform();
				tpTransform.doTransform();
			}
			stdTransform.doTransform();
			doFixedTransform();
			model.renderAll();
			
			model.popTransformState();
		}
		GL11.glPopMatrix();
	}
	
	private void handleSimpleRender(ItemStack stack) {
		GL11.glPushMatrix(); 
		{
			stdTransform.doTransform();
			doFixedTransform();
			RenderUtils.loadTexture(texture);
			model.renderAll();
		} 
		GL11.glPopMatrix();
	}
	
	private void doFirstPersonTansform() {
		GL11.glTranslated(-.3, 0, 0);
	}
//	
	private void doThirdPersonTransform() {
		
	}
	
	private void doFixedTransform() {
		GL11.glRotated(35, 0, 0, 1);
		GL11.glTranslated(0.8, -.12, 0);
	}

}
