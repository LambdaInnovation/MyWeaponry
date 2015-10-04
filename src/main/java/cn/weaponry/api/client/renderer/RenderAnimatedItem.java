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
package cn.weaponry.api.client.renderer;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import cn.liutils.vis.model.PartedModel;
import cn.liutils.vis.model.renderer.ItemModelRenderer;
import cn.weaponry.api.client.animation.ItemAnimator;
import cn.weaponry.api.runtime.ItemInfo;
import cn.weaponry.core.Weaponry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author WeAthFolD
 */
public class RenderAnimatedItem extends ItemModelRenderer {
	
	public RenderAnimatedItem() {
		this(null, null);
	}
	
	public RenderAnimatedItem(PartedModel _model) {
		this(_model, null);
	}
	
	public RenderAnimatedItem(PartedModel _model, ResourceLocation _texture) {
		super(_model, _texture);
	}
	
	@Override
	protected void renderFirstPerson(ItemStack stack, EntityLivingBase holder) {
		if(holder instanceof EntityPlayer)
			updateAnimStats((EntityPlayer) holder, stack);
		super.renderFirstPerson(stack, holder);
	}
	
	@Override
	protected void renderThirdPerson(ItemStack stack, EntityLivingBase holder) {
		if(holder instanceof EntityPlayer)
			updateAnimStats((EntityPlayer) holder, stack);
		super.renderThirdPerson(stack, holder);
	}
	
	private void updateAnimStats(EntityPlayer player, ItemStack stack) {
		ItemInfo info = ItemInfo.get(player);
		ItemAnimator animator = info.findAction("Animator");
		if(animator != null) {
			animator.onRenderFrame();
		}
	}
	
}
