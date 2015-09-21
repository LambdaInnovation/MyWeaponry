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
package cn.weaponry.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Optional;

import cn.weaponry.api.serialization.SerOption;
import cn.weaponry.api.serialization.Serializer;
import cn.weaponry.api.serialization.WSerialization;
import io.netty.buffer.ByteBuf;

/**
 * AutoSerializer will attempt to serialize all the <code>public</code> fields within the object,
 *  by using the Serializer class.<br>
 *  
 * Note that you should not create a AutoSerializer of an object that contains itself or contains circular refs. 
 * It will cause infinite loop, which is not handled.
 * @author WeAthFolD
 */
public class AutoSerializer<T> implements Serializer<T> {
	
	final List<Wrapper> handledFields = new ArrayList();
	final Class<T> handledClass;

	public AutoSerializer(Class<T> _handledClass) { 
		handledClass = _handledClass;
		List<Field> list = new ArrayList(Arrays.asList(handledClass.getFields()));
		
		// Sort by field name
		Collections.sort(list, new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(Field f : list) {
			if((f.getModifiers() & Modifier.STATIC) == 0) {
				SerOption option = f.getAnnotation(SerOption.class);
				if(option != null && option.exclude())
					continue;
				handledFields.add(new Wrapper(f, option == null ? false : option.nullable()));
			}
		}
	}

	@Override
	public void write(ByteBuf buf, T obj) throws IOException {
		try {
			for(Wrapper w : handledFields) {
				Field f = w.field;
				WSerialization.INSTANCE.serialize(buf, f.get(obj));
			}
		} catch(Exception e) {
			throw new IOException("Failed to serialize the object " + obj, e);
		}
	}

	@Override
	public T read(ByteBuf buf) throws IOException {
		try {
			T ret = handledClass.newInstance();
			for(Wrapper w : handledFields) {
				Field f = w.field;
				Object obj;
				Optional<T> res = WSerialization.INSTANCE.deserialize(buf);
				if(res == null || !res.isPresent())
					obj = null;
				else 
					obj = res.get();
				
				if(obj == null && !w.nullable)
					throw new RuntimeException();
				
				f.set(ret, obj);
			}
			return ret;
		} catch(Exception e) {
			throw new IOException(e);
		}
	}
	
	private class Wrapper {
		final Field field;
		final boolean nullable;
		
		public Wrapper(Field _field, boolean _nullable) {
			field = _field;
			nullable = _nullable;
		}
	}

}
