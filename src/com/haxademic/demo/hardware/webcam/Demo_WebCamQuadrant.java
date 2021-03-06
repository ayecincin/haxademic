package com.haxademic.demo.hardware.webcam;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.hardware.webcam.IWebCamCallback;
import com.haxademic.core.math.MathUtil;

import processing.core.PImage;

public class Demo_WebCamQuadrant 
extends PAppletHax
implements IWebCamCallback {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.WEBCAM_INDEX, 12 );
		p.appConfig.setProperty(AppSettings.WEBCAM_THREADED, false );
		p.appConfig.setProperty(AppSettings.SHOW_DEBUG, true );
		p.appConfig.setProperty(AppSettings.FILLS_SCREEN, true );
	}
		
	public void setupFirstFrame () {
		p.webCamWrapper.setDelegate(this);
	}

	public void drawApp() {
		p.background( 0 );
		DrawUtil.setDrawCorner(p);
		DrawUtil.resetPImageAlpha(p);

		PImage camFrame = p.webCamWrapper.getImage();
		
		if(p.mousePercentX() < 0.333f) {
			
			DrawUtil.setDrawCenter(p);
			DrawUtil.setCenterScreen(p);
			p.image(p.webCamWrapper.getImage(), 0, 0);
		} else if(p.mousePercentX() < 0.666f) {
		
			// draw sequence
			int frameAdjusted = P.floor(p.frameCount / (p.mousePercentY() * 10f));
			int cameraIndex = frameAdjusted % 6;
			if(cameraIndex == 0) {
				p.copy(camFrame, 0, 0, 960, 540, 0, 0, p.width, p.height);
			} else if(cameraIndex == 1) {
				p.copy(camFrame, 0, 540, 960, 540, 0, 0, p.width, p.height);
			} else if(cameraIndex == 2) {
				p.copy(camFrame, 960, 0, 960, 540, 0, 0, p.width, p.height);
			} else if(cameraIndex == 3) {
				p.copy(camFrame, 960, 540, 960, 540, 0, 0, p.width, p.height);
			} else if(cameraIndex == 4) {
				p.copy(camFrame, 960, 0, 960, 540, 0, 0, p.width, p.height);
			} else if(cameraIndex == 5) {
				p.copy(camFrame, 0, 540, 960, 540, 0, 0, p.width, p.height);
			}
			
		} else {
			DrawUtil.setPImageAlpha(p, 0.25f);
			float imgScale = MathUtil.scaleToTarget(960, p.width);
			p.image(camFrame, 0, 0, camFrame.width * imgScale, camFrame.height * imgScale);
			p.image(camFrame, -p.width, 0, camFrame.width * imgScale, camFrame.height * imgScale);
			p.image(camFrame, 0, -p.height, camFrame.width * imgScale, camFrame.height * imgScale);
			p.image(camFrame, -p.width, -p.height, camFrame.width * imgScale, camFrame.height * imgScale);
			
		}
	}

	@Override
	public void newFrame(PImage frame) {
		p.debugView.setValue("Last WebCam frame", p.frameCount);
	}

}
