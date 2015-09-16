package cn.weaponry.test;

import com.google.common.base.Optional;

import cn.weaponry.api.serialization.SerOption;
import cn.weaponry.api.serialization.WSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import net.minecraft.util.Vec3;

public class SerializationTest {
	
	public static void run() {
		TestCase tc = new TestCase();
		tc.a = 1;
		tc.b = 2;
		tc.c = 3;
		tc.d = 4;
		tc.e = "233333";
		tc.f = null;
		System.out.println("Pre serialization: " + tc);
		
		ByteBuf buf = PooledByteBufAllocator.DEFAULT.ioBuffer();
		try {
			WSerialization.INSTANCE.serialize(buf, tc);
			System.out.println("Serialzation completed");
			
			buf.readerIndex(0);
			Optional<TestCase> tc2 = WSerialization.INSTANCE.deserialize(buf);
			System.out.println("Deserialization completed.");
			System.out.println(tc2);
			System.out.println(tc2 == null ? "WTF!" : tc2.get());
		} catch (Exception e) {
			System.err.println("Serialization failed.");
			e.printStackTrace();
		}
	}
	
	public static class TestCase {
		public int a, b, c;
		public double d;
		public String e;
		@SerOption(nullable = true)
		public Vec3 f;
		
		@Override public String toString() {
			return "" + a + "/" + b + "/" + c 
					+ "/" + d + "/" + e + "/" + f;
		}
	}
	
}
