/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import cn.weaponry.api.ctrl.ControlEvent;

/**
 * The base class of all weapon classes. It receives raw control event and delegates them to
 *  the internal WeaponState system. You can then process the control in your WeaponState.\
 * Extended ItemSword to take advantage of Minecraft block-break judgement.
 * @author WeathFolD
 */
public abstract class WeaponBase extends ItemSword implements IWMControlListener, IRequiresInfo {

	public WeaponBase() {
		super(ToolMaterial.IRON);
	}
	
	public void onCtrlEvent(EntityPlayer player, ItemStack stack, int keyid, ControlEvent event) {
		
	}

}
