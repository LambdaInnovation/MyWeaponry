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

import org.lwjgl.opengl.GL11;

import cn.annoreg.core.Registrant;
import cn.liutils.api.gui.AuxGui;
import cn.liutils.registry.AuxGuiRegistry.RegAuxGui;
import cn.liutils.vis.animation.CubicSplineCurve;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author WeAthFolD
 */
@Registrant
public class SplineTest extends AuxGui {

	@RegAuxGui
	public static SplineTest instance;
	
	@Override
	public boolean isForeground() {
		return false;
	}
	
	CubicSplineCurve spline = new CubicSplineCurve();
	
	public SplineTest() {
		spline.addPoint(0, 0.0);
		spline.addPoint(120, 1);
		spline.addPoint(420, 1);
		spline.addPoint(500, 0);
	}

	@Override
	public void draw(ScaledResolution sr) {
		GL11.glPushMatrix();
		
		GL11.glTranslated(100, 100, 0);
		GL11.glScaled(20, -20, 100);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glColor4d(1, 1, 1, 1);
		for(double x = -2; x < 5.0; x += 0.1) {
			GL11.glVertex2d(x, spline.valueAt(x * 100));
		}
		GL11.glEnd();
		GL11.glColor4d(1, 0, 0, 1);
		GL11.glPointSize(4);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(1.2, 1);
		GL11.glVertex2d(4.2, 1);
		GL11.glVertex2d(5.0, 0);
		GL11.glEnd();
		GL11.glColor4d(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glPopMatrix();
	}

}
