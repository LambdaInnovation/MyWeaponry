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
package cn.weaponry.test;

import cn.annoreg.core.Registrant;
import cn.annoreg.mc.RegItem;
import cn.weaponry.api.ctrl.IControllableItem;
import cn.weaponry.api.ctrl.KeyPhase;
import cn.weaponry.api.runtime.IRequiresInfo;
import cn.weaponry.api.runtime.ItemInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author WeAthFolD
 */
@Registrant
public class TestItem extends Item implements IControllableItem, IRequiresInfo {
	
	@RegItem
	public static TestItem instance;
	
	public TestItem() {
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public void onInfoCreated(ItemInfo info) {
		System.out.println("InfoCreate " + info.isRemote());
	}

	@Override
	public void onInfoDestroyed(ItemInfo info) {
		System.out.println("InfoDestroy " + info.isRemote());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onKeyEvent(ItemInfo info, int keyID, KeyPhase phase) {
		System.out.println("KeyEvent " + keyID + "/" + phase + "/" + info.isRemote());
	}
	
}
