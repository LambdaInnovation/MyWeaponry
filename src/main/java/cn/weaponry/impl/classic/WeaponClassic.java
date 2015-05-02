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
package cn.weaponry.impl.classic;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cn.weaponry.api.client.render.RenderInfo;
import cn.weaponry.api.ctrl.KeyEventType;
import cn.weaponry.api.item.WeaponBase;
import cn.weaponry.api.state.WeaponState;
import cn.weaponry.api.state.WeaponStateMachine;
import cn.weaponry.core.blob.SoundUtils;
import cn.weaponry.impl.classic.action.ClassicReload;
import cn.weaponry.impl.classic.action.ClassicShoot;
import cn.weaponry.impl.classic.ammo.ClassicAmmoStrategy;
import cn.weaponry.impl.classic.ammo.ClassicReloadStrategy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * WARNING: This is a data heavy class.
 * @author WeAthFolD
 */
public class WeaponClassic extends WeaponBase {
	
	public ClassicShoot shootAction = new ClassicShoot();
	public ClassicReload reloadAction = new ClassicReload();
	
	public String abortSound;
	public String shootSound;
	
	public int shootInterval = 5;
	
	public WeaponClassic(Item ammo) {
		this.ammoStrategy = new ClassicAmmoStrategy(30);
		this.reloadStrategy = new ClassicReloadStrategy(ammo);	
	}

	@Override
	public void initStates(WeaponStateMachine machine) {
		machine.addState("idle", new StateIdle());
		machine.addState("reload", new StateReload());
		machine.addState("shoot", new StateShoot());
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs cct, List list) {
    	ItemStack add = new ItemStack(item, 1, 0);
    	ammoStrategy.setAmmo(add, ammoStrategy.getMaxAmmo(add));
        list.add(add);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initDefaultAnims(RenderInfo render) {
		//TODO
	}
	
	public class StateIdle extends WeaponState {
		//Idle->(Hold ML)->Shoot
		//Idle->(R)->Reload
		
		@Override
		public void onCtrl(int key, KeyEventType type) {
			if(key == 0 && type == KeyEventType.DOWN) {
				if(ammoStrategy.getAmmo(stack()) > 0) {
					transitState("shoot");
				} else {
					SoundUtils.playBothSideSound(player(), abortSound);
				}
			} else if(key == 2 && type == KeyEventType.DOWN) {
				transitState("reload");
			}
		}
	}
	
	public class StateReload extends WeaponState {
		//Solely a handler of ClassicReload action.
		//TODO: Wasty code?
		//Reload->(Any)->Idle
		ClassicReload action;
		
		@Override
		public void enterState() {
			action = new ClassicReload();
			machine.itemInfo.addAction(action);
		}
		
		@Override
		public void leaveState() {
			action.disposed = true;
		}
		
		@Override
		public void tickState(int tick) {
			if(action.disposed) {
				transitState("idle");
			}
		}
		
		@Override
		public void onCtrl(int key, KeyEventType type) {
			if(key != 2 && type == KeyEventType.DOWN) {
				transitState("idle");
			}
		}
	}
	
	public class StateShoot extends WeaponState {
		//Shoot->(Release ML)->Idle
		
		@Override
		public void onCtrl(int key, KeyEventType type) {
			if(key == 0 && type == KeyEventType.UP) {
				transitState("idle");
			}
		}
		
		@Override
		public void enterState() {
			if(!tryShoot()) {
				transitState("idle");
			}
		}
		
		@Override
		public void tickState(int tick) {
			if(tick % shootInterval == 0) {
				if(!tryShoot()) {
					transitState("idle");
				}
			}
		}
		
		private boolean tryShoot() {
			if(ammoStrategy.getAmmo(stack()) == 0) {
				return false;
			}
			machine.itemInfo.addAction(shootAction.copy());
			SoundUtils.playBothSideSound(player(), shootSound);
			return true;
		}
	}

}
