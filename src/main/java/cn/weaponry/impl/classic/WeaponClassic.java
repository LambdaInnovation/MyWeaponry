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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cn.weaponry.api.ItemInfo;
import cn.weaponry.api.action.Action;
import cn.weaponry.api.client.render.RenderInfo;
import cn.weaponry.api.ctrl.KeyEventType;
import cn.weaponry.api.event.WeaponCallback;
import cn.weaponry.api.event.WpnEventLoader;
import cn.weaponry.api.item.WeaponBase;
import cn.weaponry.api.state.WeaponState;
import cn.weaponry.api.state.WeaponStateMachine;
import cn.weaponry.core.blob.SoundUtils;
import cn.weaponry.impl.classic.ammo.AmmoStrategy;
import cn.weaponry.impl.classic.ammo.ClassicAmmoStrategy;
import cn.weaponry.impl.classic.ammo.ClassicReloadStrategy;
import cn.weaponry.impl.classic.ammo.ReloadStrategy;
import cn.weaponry.impl.classic.client.animation.Muzzleflash;
import cn.weaponry.impl.classic.event.ClassicEvents.CanReload;
import cn.weaponry.impl.classic.event.ClassicEvents.CanShoot;
import cn.weaponry.impl.classic.event.ClassicEvents.ReloadEvent;
import cn.weaponry.impl.classic.event.ClassicEvents.ShootEvent;
import cn.weaponry.impl.generic.action.ScreenUplift;
import cn.weaponry.impl.generic.action.SwingSilencer;
import cn.weaponry.impl.generic.entity.EntityBullet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * WeaponClassic provides a schema for a Half-Life like or CS-like weapon category. It has 3 fixed states:
 * shooting, reloading, idle, and a optional state: acting.(mouse right action)
 * 
 * State diagram is given in state classes' descriptions.
 * 
 * The ammoTyped field must be initialized before use, or it will crash MC when reloading.
 * 
 * WARNING: This is a data heavy class. You would probably want to use its json loader to load instances.
 * @author WeAthFolD
 */
public class WeaponClassic extends WeaponBase {
	
	//Weapon basic info
	public int maxAmmo = 30;
	public Item ammoType;
	
	//Shooting
	public int shootInterval = 5;
	public int shootDamage = 10; //Per bullet
	public int shootScatter = 10;
	public int shootBucks = 1; //How many bullets per shoot
	public String shootSound;
	public boolean isAutomatic = true;
	
	//Reloading
	public int reloadTime = 20;
	//Is the reloading state a 'reload one at a time' reloading style. (Used for stuffs like shotguns)
	public boolean isBuckReload;
	public String reloadStartSound;
	public String reloadEndSound;
	public String reloadAbortSound;
	
	//Misc
	public String jamSound;
	
	//Ammo strategies
	@LoaderExclude
	public AmmoStrategy ammoStrategy;
	@LoaderExclude
	public ReloadStrategy reloadStrategy;
	
	//Render data
	public ScreenUplift screenUplift = new ScreenUplift();
	
	public Muzzleflash animMuzzleflash;
	
	/**
	 * This ctor is used for item loader. When use this explicitly call finishInit().
	 */
	public WeaponClassic() {
		WpnEventLoader.load(this);
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			animMuzzleflash = new Muzzleflash();
		}
	}
	
	public WeaponClassic(Item ammoType, int maxAmmo) {
		this.ammoType = ammoType;
		this.maxAmmo = maxAmmo;
		finishInit();
	}
	
	public void finishInit() {
		ammoStrategy = new ClassicAmmoStrategy(maxAmmo);
		reloadStrategy = new ClassicReloadStrategy(ammoType);
	}
	
	@WeaponCallback
	@SideOnly(Side.CLIENT)
	public void onShoot(ItemInfo item, ShootEvent event) {
		item.addAction(screenUplift.copy());
	}
	
	@Override
	public void onInfoStart(ItemInfo info) {
		super.onInfoStart(info);
		info.addAction(new SwingSilencer());
		info.addAction(new Action() {

			@Override
			public void onTick(int tick) {
				int shootCount = itemInfo.dataTag().getInteger("shootCount");
				if(shootCount > 0)
					shootCount--;
				itemInfo.dataTag().setInteger("shootCount", shootCount);
			}
			
			@Override
			public String getName() {
				return "Miscs";
			}
			
		});
	}
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean idk) {
    	list.add(ammoStrategy.getDescription(stack));
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
	
	/**
	 * Create the shooting entity to be spawned. The spawn is only down in server side.
	 */
	public Entity createShootEntity(ItemInfo item) {
		return new EntityBullet(item.getPlayer(), shootScatter, shootDamage);
	}
	
	public class StateIdle extends WeaponState {
		//Idle->(Hold ML)->Shoot
		//Idle->(R)->Reload
		//Idle->(MR)->Action, if that state exists
		
		@Override
		public void onCtrl(int key, KeyEventType type) {
			if(key == 0 && type == KeyEventType.DOWN) {
				if(ammoStrategy.canConsume(getPlayer(), getStack(), 1)) {
					transitState("shoot");
				} else {
					SoundUtils.playBothSideSound(getPlayer(), jamSound);
				}
			} else if(key == 1 && type == KeyEventType.DOWN) {
				if(machine.hasState("action")) {
					transitState("action");
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
		
		@Override
		public void enterState() {
			WeaponClassic weapon = getWeapon();
			ReloadStrategy rs = weapon.reloadStrategy;
			
			if(post(getItem(), new CanReload())) {
				if(rs.canReload(getPlayer(), getStack())) {
					SoundUtils.playBothSideSound(getPlayer(), reloadStartSound);
				} else {
					SoundUtils.playBothSideSound(getPlayer(), reloadAbortSound);
					transitState("idle");
				}
			} else {
				transitState("idle");
			}
		}
		
		@Override
		public void tickState(int tick) {
			if(tick == reloadTime) {
				if(post(getItem(), new CanReload())) {
					post(getItem(), new ReloadEvent());
					reloadStrategy.doReload(getPlayer(), getStack());
					SoundUtils.playBothSideSound(getPlayer(), reloadEndSound);
				}
				transitState("idle");
			}
		}
		
		@Override
		public void leaveState() {}
		
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
		}
		
		@Override
		public void tickState(int tick) {
			int shootCount = getItem().dataTag().getInteger("shootCount");
			
			if(tick % shootInterval == 0 && shootCount == 0) {
				if(tryShoot()) {
					shootCount = shootInterval;
				} else {
					transitState("idle");
				}
				if(!isAutomatic) {
					transitState("idle");
				}
			}
			
			getItem().dataTag().setInteger("shootCount", shootCount);
		}
		
		private boolean tryShoot() {
			if(!post(getItem(), new CanShoot()) || !ammoStrategy.canConsume(getPlayer(), getStack(), 1)) {
				return false;
			}
			
			post(getItem(), new ShootEvent());
			
			SoundUtils.playBothSideSound(getPlayer(), shootSound);
			ammoStrategy.consumeAmmo(getPlayer(), getStack(), 1);
			
			if(!isRemote()) {
				World world = getWorld();
				for(int i = 0; i < shootBucks; ++i) {
					Entity spawn = createShootEntity(getItem());
					if(spawn != null) {
						world.spawnEntityInWorld(spawn);
					}
				}
			} else {
				RenderInfo.get(getItem()).addCallback(animMuzzleflash.copy());
			}
			
			return true;
		}
	}

}
