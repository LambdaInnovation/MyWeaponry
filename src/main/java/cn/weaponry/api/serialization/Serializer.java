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

import io.netty.buffer.ByteBuf;

/**
 * Serializer handles serialization and deserialization of a certain kind of object.
 * It should be guaranteed that when using the same serializer to serialize the same object, 
 * 	the amt of bytes written is equal to the bytes read.
 * You should use {@link WSerialization#addSerializer(Class, Serializer)} to register a Serializer.
 * @author WeAthFolD
 */
public interface Serializer<T> {
	
	/**
	 * Write an object to the byte buffer.
	 * @param buf
	 * @param obj The object to serialize
	 * @throws IOException if serialization failed
	 */
	public abstract void write(ByteBuf buf, T obj) throws IOException;
	
	/**
	 * Read an object from the byte buffer.
	 * @param buf
	 * @return The deserialized object if successful
	 * @throws IOException if deserialization failed
	 */
	public abstract T read(ByteBuf buf) throws IOException;
	
}
