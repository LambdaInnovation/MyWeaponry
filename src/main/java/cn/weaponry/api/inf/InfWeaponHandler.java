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
package cn.weaponry.api.inf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * InfWeapon handler.
 * @author WeathFolD
 */
@RegistrationClass
public class InfWeaponHandler {
	
	@RegEventHandler(Bus.FML)
	private static InfWeaponHandler instance = new InfWeaponHandler();
	
	//Though client only has 1 InfWeapon instance, we still use map for consistency.
	Map<EntityPlayer, InfWeapon> 
		clientMap = new HashMap(),
		serverMap = new HashMap();
	
	public static InfWeaponHandler instance() {
		return instance;
	}
	
	/**
	 * Get the player's weapon inf data. Does lazy initialization.
	 */
	public InfWeapon get(EntityPlayer player) {
		Map<EntityPlayer, InfWeapon> map = getMapFor(player);
		InfWeapon ret = map.get(player);
		if(ret == null) {
			ret = new InfWeapon(player);
			map.put(player, ret);
		}
		ret.checkValidity();
		return ret;
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(PlayerTickEvent event) {
		onTick(true);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onServerTick(ServerTickEvent event) {
		onTick(false);
	}
	
	private void onTick(boolean remote) {
		Map<EntityPlayer, InfWeapon> map = getMapFor(remote);
		Iterator<Entry<EntityPlayer, InfWeapon>> iter = map.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<EntityPlayer, InfWeapon> ent = iter.next();
			if(ent.getKey().isDead) { //Validity check
				ent.getValue().onDisposed();
				iter.remove();
			} else {
				ent.getValue().onTick();
			}
		}
	}
	
	private Map<EntityPlayer, InfWeapon> getMapFor(EntityPlayer player) {
		return player.worldObj.isRemote ? clientMap : serverMap;
	}
	
	private Map<EntityPlayer, InfWeapon> getMapFor(boolean isRemote) {
		return isRemote ? clientMap : serverMap;
	}
}
