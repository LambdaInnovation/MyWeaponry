package cn.weaponry.core.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import cn.annoreg.core.Registrant;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.liutils.util.helper.KeyHandler;
import cn.weaponry.api.config.KeyConfig;
import cn.weaponry.api.config.ModKeyConfig;
import cn.weaponry.api.ctrl.IControllableItem;
import cn.weaponry.api.ctrl.KeyPhase;
import cn.weaponry.api.event.ItemInfoCreateEvent;
import cn.weaponry.api.runtime.Action;
import cn.weaponry.api.runtime.ItemInfo;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
@Registrant
public class WController extends Action {
	
	List<Key> keys = new ArrayList();
	
	private WController() {
		super("Weaponry_controller");
	}
	
	@Override
	public void onStart() {
		ItemInfo info = getInfo();
		ItemStack stack = info.getStack();
		KeyConfig config = ModKeyConfig.getConfig(stack.getItem());
		for(Entry<Integer, Integer> entry : config.enumeration()) {
			Key k = new Key(entry.getKey(), entry.getValue());
			WeaponryClient.dynKeyManager.addKeyHandler(getKeyName(entry.getValue()), entry.getKey(), k);
			keys.add(k);
		}
	}
	
	@Override
	public void onTick() {}
	
	@Override
	public void onFinalize() {
		for(Key k : keys) {
			k.onKeyAbort();
			WeaponryClient.dynKeyManager.removeKeyHandler(getKeyName(k.keyid));
		}
	}
	
	private String getKeyName(int id) {
		return "rawctrl_" + id;
	}
	
	private void sendEvent(int keyid, KeyPhase phase) {
		ItemInfo info = getInfo();
		ItemStack stack = info.getStack();
		((IControllableItem) stack.getItem()).onKeyEvent(info, keyid, phase);
	}
	
	private class Key extends KeyHandler {
		
		boolean state = false;
		final int key;
		final int keyid;
		
		public Key(int _key, int _keyid) {
			key = _key;
			keyid = _keyid;
		}
		
		@Override
		public void onKeyDown() {
			if(!state)
				sendEvent(KeyPhase.DOWN);
			state = true;
		}
		
		@Override
		public void onKeyUp() {
			if(state)
				sendEvent(KeyPhase.UP);
			state = false;
		}
		
		/**
		 * This happens when the KeyBinding is a non-global one, 
		 * and player opens any GUI or jumps out of the game.
		 */
		@Override
		public void onKeyAbort() {
			if(state)
				sendEvent(KeyPhase.ABORT);
			state = false;
		}
		
		@Override
		public void onKeyTick() {
			if(state)
				sendEvent(KeyPhase.TICK);
		}
		
		private void sendEvent(KeyPhase phase) {
			WController.this.sendEvent(keyid, phase);
		}
	}
	
	@RegEventHandler(Bus.Forge)
	public static class Events {
		@SubscribeEvent
		public void onInfoCreate(ItemInfoCreateEvent event) {
			if(!event.info.isRemote())
				return;
			
			System.out.println("OnInfoCreate");
			ItemStack stack = event.info.getStack();
			if(stack.getItem() instanceof IControllableItem) {
				event.info.addAction(new WController());
			}
		}
	}
	
}
