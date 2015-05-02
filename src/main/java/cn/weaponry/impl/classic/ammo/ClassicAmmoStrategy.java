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
package cn.weaponry.impl.classic.ammo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cn.liutils.util.GenericUtils;
import cn.weaponry.api.ammo.AmmoStrategy;

/**
 * @author WeAthFolD
 */
public class ClassicAmmoStrategy implements AmmoStrategy {
	
	final int maxAmmo;
	
	public ClassicAmmoStrategy(int _maxAmmo) {
		maxAmmo = _maxAmmo;
	}

	@Override
	public int getAmmo(ItemStack stack) {
		return GenericUtils.loadCompound(stack).getInteger("ammo");
	}

	@Override
	public void setAmmo(ItemStack stack, int n) {
		GenericUtils.loadCompound(stack).setInteger("ammo", n);
	}
	
	@Override
	public int getMaxAmmo(ItemStack stack) {
		return maxAmmo;
	}

	@Override
	public boolean consumeAmmo(EntityPlayer player, ItemStack stack, int amt) {
		int ammo = getAmmo(stack);
		if(ammo > amt)
			return false;
		setAmmo(stack, ammo - amt);
		return true;
	}

	@Override
	public String getDescription(ItemStack stack) {
		return getAmmo(stack) + "/" + getMaxAmmo(stack);
	}

}
