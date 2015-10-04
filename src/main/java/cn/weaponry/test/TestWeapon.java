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
package cn.weaponry.test;

import cn.annoreg.core.Registrant;
import cn.annoreg.mc.RegInit;
import cn.annoreg.mc.RegItem;
import cn.liutils.util.generic.RandUtils;
import cn.liutils.util.raytrace.Raytrace;
import cn.liutils.vis.animation.presets.CompTransformAnim;
import cn.liutils.vis.animation.presets.PartedModelAnim;
import cn.liutils.vis.animation.presets.Vec3Anim;
import cn.liutils.vis.curve.CubicSplineCurve;
import cn.liutils.vis.curve.LineInterpCurve;
import cn.liutils.vis.model.CustomPartedModel;
import cn.liutils.vis.model.PartedModel;
import cn.liutils.vis.model.PartedModelHelper;
import cn.weaponry.api.client.animation.ItemAnimator;
import cn.weaponry.api.client.renderer.RenderAnimatedItem;
import cn.weaponry.api.ctrl.KeyPhase;
import cn.weaponry.api.event.ItemCallback;
import cn.weaponry.api.runtime.ControlState;
import cn.weaponry.api.runtime.ItemInfo;
import cn.weaponry.api.runtime.StateMachine;
import cn.weaponry.api.weapon.WeaponBase;
import cn.weaponry.core.Weaponry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.obj.WavefrontObject;

/**
 * @author WeAthFolD
 */
@Registrant
@RegInit
public class TestWeapon extends WeaponBase {
	
	public static void init() {
		WavefrontObject obj = new WavefrontObject(new ResourceLocation("weaponry:models/shotgun.obj"));
		PartedModel main = new CustomPartedModel(obj, "Main");
		main.addChild("Bolt", new CustomPartedModel(obj, "Bolt"));
		
		r = new RenderAnimatedItem(main, new ResourceLocation("weaponry:textures/shotgun.png"));
		
		r.stdTransform.setTransform(0, 0.05, 0.2);
		r.fpTransform.setTransform(-0.55, 0.0, -0.8);
		
		MinecraftForgeClient.registerItemRenderer(instance, r);
	}
	
	@RegItem
	public static TestWeapon instance = new TestWeapon();
	
	private static RenderAnimatedItem r;
	
	public TestWeapon() {
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("missing_texture_canon");
		this.bFull3D = true;
	}
	
	@SideOnly(Side.CLIENT)
	@ItemCallback(Side.CLIENT)
	public void onReload_c(ItemInfo info, ReloadEvent event) {
		Weaponry.log.info("OnReload(CLIENT)");
		PartedModelAnim anim = new PartedModelAnim(r.model);
		{
			CompTransformAnim bolt = new CompTransformAnim();
			Vec3Anim trans = (bolt.animTransform = new Vec3Anim());
			trans.curveX = new LineInterpCurve();
			trans.curveX.addPoint(-1, 0);
			trans.curveX.addPoint(0.1, 0);
			trans.curveX.addPoint(0.3, -0.15);
			trans.curveX.addPoint(0.45, 0);
			trans.curveX.addPoint(1, 0);
			
			anim.init("Bolt", bolt);
		}
		{
			CompTransformAnim main = new CompTransformAnim();
			Vec3Anim rot = (main.animRotation = new Vec3Anim());
			
			rot.curveZ = new LineInterpCurve();
			rot.curveZ.addPoint(0.0, 0);
			rot.curveZ.addPoint(0.2, -7);
			rot.curveZ.addPoint(0.5, 0);
			rot.curveZ.addPoint(0.6, 0);
			
			rot.curveY = new LineInterpCurve();
			rot.curveY.addPoint(0, 0);
			rot.curveY.addPoint(0.2, 6);
			rot.curveY.addPoint(0.5, 0);
			rot.curveY.addPoint(0.6, 0);
			
			Vec3Anim pos = (main.animTransform = new Vec3Anim());
			pos.curveX = new CubicSplineCurve();
			pos.curveX.addPoint(0, 0);
			pos.curveX.addPoint(0.2, -0.1);
			pos.curveX.addPoint(0.6, 0);
			
			anim.init(main);
		}
		
		ItemAnimator.get(info).addAnimation(anim, 0.6);
	}
	
	@SideOnly(Side.CLIENT)
	@ItemCallback(Side.CLIENT)
	public void onShoot_c(ItemInfo info, ShootEvent event) {
		Weaponry.log.info("OnShoot(CLIENT)");
		EntityPlayer player = info.getPlayer();
		double x = player.posX, y = player.posY, z = player.posZ;
		double vx = RandUtils.ranged(-.03, .03), vy = RandUtils.ranged(-.03, .03), vz = RandUtils.ranged(-.03, .03);
		EntitySmokeFX smoke = new EntitySmokeFX(info.getPlayer().worldObj, x, y, z, vx, vy, vz);
		Minecraft.getMinecraft().effectRenderer.addEffect(smoke);
		player.worldObj.playSound(x, y, z, "random.bowhit", .5f, 1f, false);
	}
	
	@ItemCallback(Side.SERVER)
	public void onShoot_s(ItemInfo info, ShootEvent event) {
		Weaponry.log.info("OnShoot(SERVER)");
		MovingObjectPosition result = Raytrace.traceLiving(info.getPlayer(), 20);
		if(result != null && result.typeOfHit == MovingObjectType.ENTITY) {
			result.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(info.getPlayer()), 5);
		}
	}
	
	@Override
	public void initStates(StateMachine fsm) {
		fsm.addState("idle", 
		new ControlState() {
			@Override
			public void onKey(int keyID, KeyPhase phase) {
				if(keyID == 0 && phase == KeyPhase.DOWN) {
					fsm.transitState("shoot"); // Or this.getStateMachine().transitState
				} else if(keyID == 2 && phase == KeyPhase.DOWN) {
					postEvent(getInfo(), new ReloadEvent());
				}
			}
		});
		
		fsm.addState("shoot",
		new ControlState() {
			int ticks;
			
			@Override
			public void onEnter() {
				ticks = 0;
				postEvent(getInfo(), new ShootEvent());
			}
			
			@Override
			public void onTick() {
				if(++ticks == 5)
					fsm.transitState("idle");
			}
		});
		
		fsm.setInitState("idle");
	}
	
	public static class ShootEvent {}
	
	public static class ReloadEvent {}
	
}
