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
package cn.weaponry.api.serialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;

import cn.weaponry.core.AutoSerializer;
import cn.weaponry.core.SideHelper;
import cn.weaponry.core.Weaponry;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Global serialization manager.
 * @author WeAthFolD
 */
public enum WSerialization {
	INSTANCE;
	
	static final String AUTO_ID = "@auto", NULL_ID = "@null";
	
	private Map<String, Serializer> table = new HashMap();
	private Map<String, AutoSerializer> cachedAuto = new HashMap();
	
	/**
	 * Adds a serializer.
	 * @param klass The base class that this serializer handles. (Will also handle any subtype if no more 'direct' serializer was found)
	 * @param serializer
	 */
	public <T> void addSerializer(Class<? extends T> klass, Serializer<T> serializer) {
		String id = toClassIdentifier(klass);
		if(table.containsKey(id))
			throw new RuntimeException("Serializer for " + klass + " already exists.");
		
		table.put(id, serializer);
	}
	
	/**
	 * Serialize an object and write the data to the given buf.
	 * If the Serializer of the object is not present, will try to create an auto serializer for it.
	 * @param buf
	 * @param object
	 */
	public <T> void serialize(ByteBuf buf, T object) {
		if(object == null) {
			ByteBufUtils.writeUTF8String(buf, NULL_ID);
			return;
		}
		
		Pair<String, Serializer<T>> pair = getSerializer(object);
		if(pair == null) {
			String id = toClassIdentifier(object.getClass());
			pair = Pair.of(id, getAutoSerializer(id));
			ByteBufUtils.writeUTF8String(buf, AUTO_ID);
		}
		
		ByteBufUtils.writeUTF8String(buf, pair.getLeft());
		try {
			pair.getRight().write(buf, object);
		} catch (IOException e) {
			throw new RuntimeException("Serialization failed on object " + object, e);
		}
	}
	
	/**
	 * Deserialize an object from the given buf.
	 * If the Serializer of the object is not present, will try to create an auto serializer for it.
	 * @param buf the buf to read from
	 * @return null when deserialization failed, <code>Optional.absent()</code> value when the object is serialized with null, 
	 * 	and the <code>Optional</code> with the given object when successful.
	 */
	public <T> Optional<T> deserialize(ByteBuf buf) {
		String id = ByteBufUtils.readUTF8String(buf);
		if(id.equals(NULL_ID))
			return Optional.absent();
		
		Serializer<T> ser = null;
		if(id.equals(AUTO_ID)) {
			id = ByteBufUtils.readUTF8String(buf);
			ser = getAutoSerializer(id);
		}
		
		if(ser == null)
			ser = table.get(id);
		if(ser == null)
			throw new RuntimeException("Serializer with id " + id + " doesn't exist.");
		T ret = null;
		try {
			ret = ser.read(buf);
		} catch(Exception e) {
			Weaponry.log.warn("Serialization failed on " + id + ".");
		}
		return ret == null ? null : Optional.of(ret);
	}
	
	private <T> Pair<String, Serializer<T>> getSerializer(T object) {
		Class klass = object.getClass();
		while(klass != null) {
			String id = toClassIdentifier(klass);
			Serializer<T> ser = table.get(id);
			if(ser != null)
				return Pair.of(id, ser);
			klass = klass.getSuperclass();
		}
		return null;
	}
	
	private <T> AutoSerializer<T> getAutoSerializer(String id) {
		if(cachedAuto.containsKey(id))
			return cachedAuto.get(id);
		
		try {
			Class<T> klass = (Class<T>) Class.forName(id);
			AutoSerializer<T> ser = new AutoSerializer(klass);
			cachedAuto.put(id, ser);
			Weaponry.log.info("Creating auto serializer for class " + id);
			return ser;
		} catch(Exception e) {
			Weaponry.log.error("Creating auto serializer for class " + id + " failed.", e);
			return null;
		}
	}
	
	private String toClassIdentifier(Class c) {
		return c.getName();
	}
	
	private Class toClass(String id) throws Exception {
		return Class.forName(id);
	}
	
	// DEFAULT SERIALIZERS
	{
		// Primitive types
		{
			Serializer ser = new Serializer() {
				@Override
				public void write(ByteBuf buf, Object obj) throws IOException {
					buf.writeInt((int) obj);
				}
				@Override
				public Object read(ByteBuf buf) throws IOException {
					return buf.readInt();
				}
			};
			addSerializer(Integer.TYPE, ser);
			addSerializer(Integer.class, ser);
		}
		{
			Serializer ser = new Serializer() {
				@Override
				public void write(ByteBuf buf, Object obj) throws IOException {
					buf.writeFloat((float) obj);
				}
				@Override
				public Object read(ByteBuf buf) throws IOException {
					return buf.readFloat();
				}
			};
			addSerializer(Float.TYPE, ser);
			addSerializer(Float.class, ser);
		}
		{
			Serializer ser = new Serializer() {
				@Override
				public void write(ByteBuf buf, Object obj) throws IOException {
					buf.writeDouble((double) obj);
				}
				@Override
				public Object read(ByteBuf buf) throws IOException {
					return buf.readDouble();
				}
			};
			addSerializer(Double.TYPE, ser);
			addSerializer(Double.class, ser);
		}
		{
			Serializer ser = new Serializer() {
				@Override
				public void write(ByteBuf buf, Object obj) throws IOException {
					buf.writeShort((short) obj);
				}
				@Override
				public Object read(ByteBuf buf) throws IOException {
					return buf.readShort();
				}
			};
			addSerializer(Short.TYPE, ser);
			addSerializer(Short.class, ser);
		}
		{
			Serializer ser = new Serializer() {
				@Override
				public void write(ByteBuf buf, Object obj) throws IOException {
					buf.writeByte((byte) obj);
				}
				@Override
				public Object read(ByteBuf buf) throws IOException {
					return buf.readByte();
				}
			};
			addSerializer(Byte.TYPE, ser);
			addSerializer(Byte.class, ser);
		}
		{
			Serializer ser = new Serializer<String>() {
				@Override
				public void write(ByteBuf buf, String obj) throws IOException {
					ByteBufUtils.writeUTF8String(buf, obj);
				}
				@Override
				public String read(ByteBuf buf) throws IOException {
					return ByteBufUtils.readUTF8String(buf);
				}
			};
			addSerializer(String.class, ser);
		}
		
		// Minecraft types
		{
			Serializer ser = new Serializer<Vec3>() {
				@Override
				public void write(ByteBuf buf, Vec3 obj) throws IOException {
					buf.writeDouble(obj.xCoord);
					buf.writeDouble(obj.yCoord);
					buf.writeDouble(obj.zCoord);
				}
				@Override
				public Vec3 read(ByteBuf buf) throws IOException {
					return Vec3.createVectorHelper(
						buf.readDouble(), buf.readDouble(), buf.readDouble());
				}
			};
			addSerializer(Vec3.class, ser);
		}
		{
			Serializer ser = new Serializer<NBTTagCompound>() {
				@Override
				public void write(ByteBuf buf, NBTTagCompound obj) throws IOException {
					ByteBufUtils.writeTag(buf, obj);
				}
				@Override
				public NBTTagCompound read(ByteBuf buf) throws IOException {
					return ByteBufUtils.readTag(buf);
				}
			};
			addSerializer(NBTTagCompound.class, ser);
		}
		{
			Serializer ser = new Serializer<ItemStack>() {
				@Override
				public void write(ByteBuf buf, ItemStack obj) throws IOException {
					ByteBufUtils.writeItemStack(buf, obj);
				}
				@Override
				public ItemStack read(ByteBuf buf) throws IOException {
					return ByteBufUtils.readItemStack(buf);
				}
			};
			addSerializer(ItemStack.class, ser);
		}
		{
			Serializer ser = new Serializer<Entity>() {
				@Override
				public void write(ByteBuf buf, Entity obj) throws IOException {
					buf.writeShort(obj.dimension);
					buf.writeInt(obj.getEntityId());
				}
				@Override
				public Entity read(ByteBuf buf) throws IOException {
					World world = SideHelper.getWorld(buf.readShort());
					if(world == null)
						return null;
					return world.getEntityByID(buf.readInt());
				}
			};
			addSerializer(Entity.class, ser);
		}
	}

}
