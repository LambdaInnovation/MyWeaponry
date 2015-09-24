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
import cn.weaponry.api.event.EventSystem;
import cn.weaponry.api.event.IItemEventHandler;
import cn.weaponry.api.event.ItemCallback;
import cn.weaponry.api.event.ItemEventBus;
import cn.weaponry.api.runtime.IRequiresInfo;
import cn.weaponry.api.runtime.ItemInfo;
import cn.weaponry.api.serialization.SerOption;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author WeAthFolD
 */
@Registrant
public class TestItem extends Item implements IControllableItem, IRequiresInfo, IItemEventHandler {
	
	@RegItem
	public static TestItem instance;
	
	ItemEventBus eventBus = new ItemEventBus();
	
	public TestItem() {
		setCreativeTab(CreativeTabs.tabMisc);
		eventBus.load(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onKeyEvent(ItemInfo info, int keyID, KeyPhase phase) {
		if(keyID == 0 && phase == KeyPhase.DOWN) {
			FuckEvent event = new FuckEvent();
			event.val = 2345;
			event.val2 = "hohohoho";
			eventBus.handleEvent(info, event);
			EventSystem.sendToServer(info, event);
		}
	}
	
	@ItemCallback(Side.CLIENT)
	public void onFuckClient(ItemInfo info, FuckEvent event) {
		System.out.println(event + "/CLIENT");
	}
	
	@ItemCallback(Side.SERVER)
	public void onFuckServer(ItemInfo info, FuckEvent event) {
		System.out.println(event + "/SERVER");
	}

	@Override
	public void handleEvent(ItemInfo info, Object event) {
		eventBus.handleEvent(info, event);
	}
	
	public static class FuckEvent {
		public int val;
		@SerOption(nullable = true)
		public String val2;
		
		@Override public String toString() {
			return "Fuck[" + val + "/" + val2 + "]";
		}
	}

	@Override
	public void onInfoCreated(ItemInfo info) {}

	@Override
	public void onInfoDestroyed(ItemInfo info) {}
	
}
