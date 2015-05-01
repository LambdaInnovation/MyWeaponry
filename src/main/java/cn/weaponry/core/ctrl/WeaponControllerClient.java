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
package cn.weaponry.core.ctrl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.annoreg.mc.RegSubmoduleInit;
import cn.annoreg.mc.network.RegNetworkCall;
import cn.annoreg.mc.s11n.StorageOption;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.core.LIUtils;
import cn.weaponry.api.ctrl.IItemCtrlListener;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeAthFolD
 */
@RegistrationClass
@RegSubmoduleInit
@SideOnly(Side.CLIENT)
@RegEventHandler(Bus.FML)
public class WeaponControllerClient {
	
	public static int KEYS[] = { LIKeyProcess.MOUSE_LEFT, LIKeyProcess.MOUSE_RIGHT, Keyboard.KEY_R };
	
	private static KeyHandler handlers[] = new KeyHandler[KEYS.length];
	
	public static void init() {
		for(int i = 0; i < 3; ++i) {
			LIKeyProcess.instance.addKey("weaponry_" + i, KEYS[i], false, handlers[i] = new KeyHandler(i));
		}
	}
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if(event.phase == Phase.END) return;
		for(KeyHandler kh : handlers) {
			kh.tick();
		}
	}
	
	@RegNetworkCall(side = Side.SERVER)
	public static void serverSendEvent(
		@StorageOption.Instance EntityPlayer player,
		@StorageOption.Data Byte keyID,
		@StorageOption.Instance SyncEventType type) {
		WeaponControllerServer.sendEvent(player, keyID, type);
	}
	
	private static void localSendEvent(EntityPlayer player, int keyID, SyncEventType type) {
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() instanceof IItemCtrlListener) {
			((IItemCtrlListener)stack.getItem()).onKeyEvent(player, keyID, type.keyEvent);
		}
	}
	
	private static class KeyHandler implements IKeyHandler {
		
		final int virtualKey;
		
		boolean keyDown;
		
		int ticker;
		
		public KeyHandler(int i) {
			virtualKey = i;
		}
		
		@Override
		public void onKeyDown(int keyCode, boolean tickEnd) {
			if(tickEnd || !acceptsInput()) return;
			doKeyDown();
		}

		@Override
		public void onKeyUp(int keyCode, boolean tickEnd) {
			if(tickEnd || !acceptsInput()) return;
			doKeyUp();
		}

		@Override
		public void onKeyTick(int keyCode, boolean tickEnd) {
			if(tickEnd || !acceptsInput()) return;
			doKeyTick();
		}
		
		public void tick() {
			if(keyDown) {
				if(!acceptsInput()) {
					sendEvent(SyncEventType.ABORT);
				} else {
					if(++ticker > 5) {
						ticker = 0;
						doKeyAbort();
					}
				}
			}
		}
		
		private void doKeyDown() {
			if(!keyDown) {
				sendEvent(SyncEventType.DOWN);
				keyDown = true;
			}
		}
		
		private void doKeyAbort() {
			if(keyDown) {
				sendEvent(SyncEventType.ABORT);
				keyDown = false;
			}
		}
		
		private void doKeyUp() {
			if(keyDown) {
				sendEvent(SyncEventType.UP);
				keyDown = false;
			}
		}
		
		private void doKeyTick() {
			if(keyDown) {
				sendEvent(SyncEventType.KEEPALIVE);
				ticker = 0;
			}
		}
		
		int tickSendCounter = 0;
		
		private void sendEvent(SyncEventType type) {
			//Check player
			if(Minecraft.getMinecraft().thePlayer == null)
				return;
			
			localSendEvent(Minecraft.getMinecraft().thePlayer, virtualKey, type);
			
			if(type == SyncEventType.KEEPALIVE) {
				if(++tickSendCounter == 5) { //Only send once per 5 ticks~
					tickSendCounter = 0;
					serverSendEvent(Minecraft.getMinecraft().thePlayer, (byte) virtualKey, SyncEventType.KEEPALIVE);
				}
			} else {
				serverSendEvent(Minecraft.getMinecraft().thePlayer, (byte) virtualKey, type);
			}
		}
		
		private boolean acceptsInput() {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			return player != null && Minecraft.getMinecraft().currentScreen == null;
		}
		
	}
	
}
