package cn.weaponry.api.event;

import cn.annoreg.mc.network.TargetPointHelper;
import cn.weaponry.api.runtime.ItemInfo;
import cn.weaponry.core.Weaponry;
import cn.weaponry.core.network.ItemEventMessage;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Weaponry's event system. Everything affects both sides should be sent as events
 *  and processed in local event subscribers.
 * @author WeAthFolD
 */
public class EventSystem {
	
	// CLIENT INTERFACE
	public static void sendToServer(ItemInfo info, Object event) {
		Weaponry.network.sendToServer(new ItemEventMessage(info, event));
	}
	
	/**
	 * Send the event to server and 'Rebroadcast' the event back to all clients. 
	 */
	public static void sendToServerRebroadcast(ItemInfo info, Object event)  {
		Weaponry.network.sendToServer(new ItemEventMessage(info, event).setRebroadcast());
	}
	
	// SERVER INTERFACE
	public static void sendToClient(ItemInfo info, Object event) {
		Weaponry.network.sendTo(new ItemEventMessage(info, event), (EntityPlayerMP) info.getPlayer());
	}
	
	public static void sendToNearbyClient(ItemInfo info, Object event) {
		sendToNearbyClient(info, ItemEventMessage.RANGE, event);
	}
	
	public static void sendToNearbyClient(ItemInfo info, double range, Object event) {
		Weaponry.network.sendToAllAround(new ItemEventMessage(info, event), TargetPointHelper.convert(info.getPlayer(), range));
	}
	
}
