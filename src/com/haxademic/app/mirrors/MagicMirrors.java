package com.haxademic.app.mirrors;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.data.constants.PRenderers;
import com.haxademic.core.draw.filters.pgraphics.GPUParticlesLauncher;
import com.haxademic.core.draw.filters.pgraphics.SmokeFeedback;
import com.haxademic.core.draw.filters.pgraphics.shared.BaseVideoFilter;
import com.haxademic.core.draw.image.ImageUtil;
import com.haxademic.core.hardware.webcam.IWebCamCallback;

import processing.core.PGraphics;
import processing.core.PImage;

public class MagicMirrors 
extends PAppletHax
implements IWebCamCallback {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected float w = 1280;
	protected float h = 720;
	protected BaseVideoFilter vfxPre;
	protected BaseVideoFilter vfx;
	
	protected int webcamW = 1920;
	protected int webcamH = 1080;
	protected PGraphics webcamBuffer;

	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.WIDTH, (int) w);
		p.appConfig.setProperty(AppSettings.HEIGHT, (int) h);
		p.appConfig.setProperty(AppSettings.WEBCAM_INDEX, 12);
		p.appConfig.setProperty(AppSettings.FULLSCREEN, true);
		p.appConfig.setProperty(AppSettings.ALWAYS_ON_TOP, false);
	}

	protected void setupFirstFrame() {
		p.webCamWrapper.setDelegate(this);
//		vfx = new GPUParticlesSheetDisplacer(p.width, p.height, 0.5f);
//		vfx = new PixelTriFilter(p.width, p.height, 20);
//		vfx = new ColorDiff8BitRows(p.width, p.height, 20);
//		vfx = new TileRepeat(p.width, p.height);
//		vfx = new BlobLinesFeedback(p.width, p.height);
//		vfx = new HalftoneCamo(p.width, p.height);
//		vfx = new RadialHistory(p.width, p.height);
		vfx = new GPUParticlesLauncher(p.width, p.height);
		vfxPre = new SmokeFeedback(p.width, p.height);
		webcamBuffer = p.createGraphics(webcamW, webcamH, PRenderers.P2D);
	}

	public void drawApp() {
		background(0);
		if(vfxPre != null) {
			vfxPre.update();
			vfx.newFrame(vfxPre.image());
			vfx.update();
		} else {
			vfx.update();
		}
		
		// bloom
		pg.beginDraw();
		pg.image(vfx.image(), 0, 0);
		pg.endDraw();
//		BloomFilter.instance(p).setStrength(1f);
//		BloomFilter.instance(p).applyTo(pg);
		
		// draw to screen
		p.image(pg, 0, 0);
	}

	@Override
	public void newFrame(PImage frame) {
		// copy webcam and create motion detection at size of cropped webcam (and downscaling)
		ImageUtil.cropFillCopyImage(frame, webcamBuffer, true);
		ImageUtil.flipH(webcamBuffer);
		
		if(vfxPre != null) {
			// chain it up
			vfxPre.newFrame(webcamBuffer);
		} else {
			// send new webcam mirror frame to vfx
			vfx.newFrame(webcamBuffer);
		}
	}

		
}



































