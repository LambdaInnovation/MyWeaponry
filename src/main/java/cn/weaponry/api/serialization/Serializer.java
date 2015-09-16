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
 * You should use {@link WSerialization#addSerializer(Class, Serializer)} to register a Serializer.
 * @author WeAthFolD
 */
public interface Serializer<T> {
	
	public abstract void write(ByteBuf buf, T obj) throws IOException;
	
	public abstract T read(ByteBuf buf) throws IOException;
	
}
