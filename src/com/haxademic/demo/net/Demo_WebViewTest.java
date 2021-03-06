package com.haxademic.demo.net;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;

public class Demo_WebViewTest
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected void overridePropsFile() {
		int FRAMES = 360;
		p.appConfig.setProperty(AppSettings.LOOP_FRAMES, FRAMES);
	}

	public void setupFirstFrame() {
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(Demo_WebViewTestApp.class);
            }
        }.start();
	}
	

	public void drawApp() {
		p.background(0);
		DrawUtil.setCenterScreen(p);
		
		if(Demo_WebViewTestApp.instance != null) {
			p.background(0,255,0);
			//	WebViewApplicationTest.instance.printSomething();
		}
	}

}

