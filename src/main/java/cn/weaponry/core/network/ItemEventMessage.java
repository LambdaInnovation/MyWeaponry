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
package cn.weaponry.core.network;

import com.google.common.base.Optional;

import cn.annoreg.mc.network.TargetPointHelper;
import cn.annoreg.mc.network.TargetPointHelper.TargetPointConverter;
import cn.weaponry.api.event.IItemEventHandler;
import cn.weaponry.api.runtime.ItemInfo;
import cn.weaponry.api.serialization.WSerialization;
import cn.weaponry.core.Weaponry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author WeAthFolD
 */
public class ItemEventMessage implements IMessage {
	
	public static final float RANGE = 20;
	
	boolean available;
	boolean rebroadcast = false;
	ItemInfo info;
	Object event;
	
	public ItemEventMessage(ItemInfo _info, Object _event) {
		info = _info;
		event = _event;
	}
	
	public ItemEventMessage() {}
	
	public ItemEventMessage setRebroadcast() {
		rebroadcast = true;
		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		Optional<EntityPlayer> oplayer = WSerialization.INSTANCE.deserialize(buf);
		Optional<Object> oevent = WSerialization.INSTANCE.deserialize(buf);
		rebroadcast = buf.readBoolean();
		if(oplayer == null || !oplayer.isPresent() || oevent == null || !oevent.isPresent()) {
			available = false;
			Weaponry.log.error("Failed deserializing event message " + "player: " + oplayer + "/event: " + oevent);
		} else {
			available = true;
			info = ItemInfo.get(oplayer.get());
			event = oevent.get();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		WSerialization.INSTANCE.serialize(buf, info.getPlayer());
		WSerialization.INSTANCE.serialize(buf, event);
		buf.writeBoolean(rebroadcast);
	}
	
	public static class Handler implements IMessageHandler<ItemEventMessage, IMessage> {

		@Override
		public IMessage onMessage(ItemEventMessage msg, MessageContext ctx) {
			if(msg.available && (ctx.side == Side.SERVER || checkClient(msg))) {
				ItemStack stack = msg.info.getStack();
				if(stack != null && stack.getItem() instanceof IItemEventHandler) {
					((IItemEventHandler) stack.getItem()).handleEvent(msg.info, msg.event);
				} else {
					Weaponry.log.warn("Didn't find item handler. stack: " + stack);
				}
				
				if(msg.rebroadcast && ctx.side == Side.SERVER) {
					Weaponry.network.sendToAllAround(msg, TargetPointHelper.convert(msg.info.getPlayer(), RANGE));
				}
			}
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		private boolean checkClient(ItemEventMessage msg) {
			return Minecraft.getMinecraft().thePlayer != msg.info.getPlayer() || !msg.rebroadcast;
		}
		
	}

}
