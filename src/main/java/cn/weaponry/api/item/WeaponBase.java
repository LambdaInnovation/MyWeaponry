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
package cn.weaponry.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import cn.weaponry.api.IItemInfoProvider;
import cn.weaponry.api.ItemInfo;
import cn.weaponry.api.ItemInfoProxy;
import cn.weaponry.api.ctrl.IItemCtrlListener;
import cn.weaponry.api.ctrl.KeyEventType;
import cn.weaponry.api.state.WeaponStateMachine;

/**
 * @author WeAthFolD
 *
 */
public abstract class WeaponBase extends ItemSword implements IItemInfoProvider, IItemCtrlListener {

	public WeaponBase() {
		super(ToolMaterial.IRON);
	}

	@Override
	public void onInfoStart(ItemInfo info) {
		WeaponStateMachine wsm = new WeaponStateMachine();
		initStates(wsm);
		info.addAction(wsm);
	}
	
	public abstract void initStates(WeaponStateMachine machine);

	@Override
	public void onKeyEvent(EntityPlayer player, int key, KeyEventType type) {
		ItemInfo info = ItemInfoProxy.getInfo(player);
		WeaponStateMachine wsm = info.getAction("StateMachine");
		if(wsm != null) {
			wsm.onCtrl(key, type);
		}
	}
	
}
