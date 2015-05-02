package cn.weaponry.api.ammo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ReloadStrategy {
	
	boolean canReload(EntityPlayer player, ItemStack stack);
	
	void doReload(EntityPlayer player, ItemStack stack);
	
}
