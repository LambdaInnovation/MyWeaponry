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
package cn.weaponry.api.runtime;

import java.util.Iterator;
import java.util.LinkedList;

import cn.annoreg.core.Registrant;
import cn.liutils.registry.RegDataPart;
import cn.liutils.util.helper.DataPart;
import cn.liutils.util.helper.PlayerData;
import cn.weaponry.api.event.ItemInfoCreateEvent;
import cn.weaponry.core.IterativeList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author WeAthFolD
 */
@Registrant
@RegDataPart("WMItem")
public class ItemInfo extends DataPart {
	
	IterativeList<Action> actions = IterativeList.of(new LinkedList());
	
	ItemStack stack;
	boolean created = false;
	
	public ItemInfo() {}
	
	@Override
	public void tick() {
		checkStack();
		//System.out.println("Ticking WMItem");
		
		actions.startIterating();
		Iterator<Action> iter = actions.iterator();
		while(iter.hasNext()) {
			Action a = iter.next();
			if(a.disposed) {
				iter.remove();
			} else {
				a.onTick();
			}
		}
		actions.endIterating();
	}

	@Override
	public void fromNBT(NBTTagCompound tag) {}

	@Override
	public NBTTagCompound toNBT() {
		return null;
	}
	
	public void addAction(Action action) {
		action.info = this;
		actions.add(action);
		action.onStart();
	}
	
	/**
	 * @return The action first found with given ID, or null if nothing was found.
	 */
	public Action findAction(String id) {
		for(Action a : actions) {
			if(a.id.equals(id))
				return a;
		}
		return null;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	private void resetActions() {
		actions.startIterating();
		Iterator<Action> iter = actions.iterator();
		while(iter.hasNext()) {
			Action a = iter.next();
			a.onAbort();
			a.onFinalize();
		}
		actions.endIterating();
		actions.clear();
	}
	
	private void checkStack() {
		EntityPlayer player = getPlayer();
		ItemStack current = player.getCurrentEquippedItem();
		//System.out.println("created: " + created);
		
		if(!stackEquals(current)) {
			if(created) {
				((IRequiresInfo) stack.getItem()).onInfoDestroyed(this);
			}
			resetActions();
			stack = null;
			created = false;
		}
		
		if(stack != null) {
			if(requiresInfo()) {
				if(!created) {
					created = true;
					resetActions();
					((IRequiresInfo) stack.getItem()).onInfoCreated(this);
					MinecraftForge.EVENT_BUS.post(new ItemInfoCreateEvent(this));
				}
			}
		}
		
		stack = current;
	}

	private boolean stackEquals(ItemStack other) {
		return stack != null && other != null && stack.getItem() == other.getItem();
	}
	
	private boolean requiresInfo() {
		return stack != null && stack.getItem() instanceof IRequiresInfo;
	}
	
	public static ItemInfo get(EntityPlayer player) {
		ItemInfo ret = PlayerData.get(player).getPart(ItemInfo.class);
		ret.checkStack();
		return ret;
	}
	
}
