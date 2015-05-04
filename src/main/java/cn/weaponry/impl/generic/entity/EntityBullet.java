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
package cn.weaponry.impl.generic.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEntity;
import cn.liutils.entityx.EntityAdvanced;
import cn.liutils.entityx.event.CollideEvent;
import cn.liutils.entityx.event.CollideEvent.CollideHandler;
import cn.liutils.entityx.handlers.Rigidbody;
import cn.liutils.util.space.Motion3D;

/**
 * @author WeAthFolD
 *
 */
@RegistrationClass
@RegEntity
public class EntityBullet extends EntityAdvanced {
	
	static final double VELOCITY = 40.0;
	
	float damage;
	
	EntityPlayer spawner;

	public EntityBullet(EntityPlayer player, int scatter, float dmg) {
		this(player.worldObj);
		
		spawner = player;
		damage = dmg;
		Motion3D m3d = new Motion3D(player, scatter, true);
		
		m3d.normalize();
		double vel = 3;
		m3d.applyToEntity(this);
		motionX = m3d.motionX * vel;
		motionY = m3d.motionY * vel;
		motionZ = m3d.motionZ * vel;
		
		
		this.regEventHandler(new CollideHandler() {
			@Override
			public void onEvent(CollideEvent event) {
				if(event.result.typeOfHit == MovingObjectType.ENTITY) {
					event.result.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(spawner), damage);
				}
				EntityBullet.this.setDead();
			}
		});
	}
	
	public EntityBullet(World world) {
		super(world);
		
		Rigidbody rb = new Rigidbody();
		this.addMotionHandler(rb);
		rb.filter = new IEntitySelector() {
			@Override
			public boolean isEntityApplicable(Entity e) {
				return spawner == null || !e.equals(spawner);
			}
		};
		
		setSize(0.2f, 0.2f);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		setDead();
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {}

}
