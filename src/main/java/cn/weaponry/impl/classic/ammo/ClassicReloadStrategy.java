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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cn.weaponry.impl.classic.WeaponClassic;

/**
 * @author WeAthFolD
 *
 */
public class ClassicReloadStrategy implements ReloadStrategy {
	
	final Item ammoType;
	
	public ClassicReloadStrategy(Item _ammoType) {
		ammoType = _ammoType;
	}

	@Override
	public boolean canReload(EntityPlayer player, ItemStack stack) {
		WeaponClassic weapon = (WeaponClassic) stack.getItem();
		AmmoStrategy ammoStrategy = weapon.ammoStrategy;
		if(ammoStrategy.getAmmo(stack) == ammoStrategy.getMaxAmmo(stack)) {
			return false;
		}
		
		for(ItemStack i : player.inventory.mainInventory) {
			if(i != null && i.getItem() == ammoType) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void doReload(EntityPlayer player, ItemStack stack) {
		WeaponClassic weapon = (WeaponClassic) stack.getItem();
		AmmoStrategy ammoStrategy = weapon.ammoStrategy;
		
		int need = ammoStrategy.getMaxAmmo(stack) - ammoStrategy.getAmmo(stack);
		
		int i = 0;
		while(need > 0 && i < player.inventory.mainInventory.length) {
			ItemStack s = player.inventory.mainInventory[i];
			if(s != null && s.getItem() == ammoType) {
				if(s.getMaxStackSize() == 1) {
					int con = Math.min(s.getMaxDamage() - s.getItemDamage(), need);
					need -= con;
					s.setItemDamage(s.getItemDamage() - con);
				} else {
					int con = Math.min(s.stackSize, need);
					need -= con;
					s.stackSize -= con;
				}
			}
		}
		
	}

}
