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
package cn.weaponry.impl.classic.loading;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import cn.liutils.loading.Loader.ObjectNamespace;
import cn.liutils.loading.item.ItemLoadRule;
import cn.liutils.loading.item.ItemLoader;
import cn.weaponry.impl.classic.WeaponClassic;
import cn.weaponry.impl.generic.action.ScreenUplift;

/**
 * Rules:Any field in WeaponClassic that is marked as public. Plus the uplift action.
 * 
 * @author WeAthFolD <br/>
 */
public class ClassicWeaponRule extends ItemLoadRule<WeaponClassic> {
	
	static List<Field>
		ints = new ArrayList(),
		strs = new ArrayList(),
		booleans = new ArrayList();
	
	static {
		for(Field f : WeaponClassic.class.getDeclaredFields()) {
			if((f.getModifiers() & Modifier.PUBLIC) != 0) {
				if(f.getType() == Integer.TYPE) {
					ints.add(f);
				} else if(f.getType() == String.class) {
					strs.add(f);
				} else if(f.getType() == Boolean.TYPE) {
					booleans.add(f);
				}
			}
		}
	}

	@Override
	public void load(WeaponClassic item, ObjectNamespace ns, String name) throws Exception {
		for(Field f : ints) {
			Integer i = ns.getInt("weapon", f.getName());
			if(i != null) {
				//System.out.println(String.format("[%s]Updated field %s", f.getName(), name));
				f.set(item, (int)i);
			}
		}
		
		for(Field f : strs) {
			String s = ns.getString("weapon", f.getName());
			if(s != null) {
				//System.out.println(String.format("[%s]Updated field %s", f.getName(), name));
				f.set(item, s);
			}
		}
		
		for(Field f : booleans) {
			Boolean b = ns.getBoolean("weapon", f.getName());
			if(b != null) {
				//System.out.println(String.format("[%s]Updated field %s", f.getName(), name));
				f.set(item, (boolean)b);
			}
		}
		
		String[] ul = { "upliftRadius", "upliftSpeed", "recoverSpeed", "degreeFrom", "degreeTo" };
		for(String prop : ul) {
			Double d = ns.getDouble("weapon", "uplift", prop);
			if(d != null) {
				//System.out.println(String.format("[%s]Updated field uplift.%s", prop, name));
				ScreenUplift.class.getField(prop).set(item.screenUplift, d);
			}
		}
		
		item.finishInit();
	}
	
	@Override
	public boolean applyFor(Item item, ItemLoader loader, String name) {
		return item instanceof WeaponClassic;
	}

}
