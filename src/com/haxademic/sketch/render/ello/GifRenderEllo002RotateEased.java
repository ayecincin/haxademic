package com.haxademic.sketch.render.ello;

import processing.core.PConstants;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.image.AnimatedGifEncoder;
import com.haxademic.core.math.easing.Penner;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class GifRenderEllo002RotateEased
extends PAppletHax{
	
	AnimatedGifEncoder encoder;
	PShape _logo;
	PShape _logoInverse;
	float _frames = 45;
	
	protected void overridePropsFile() {
		_appConfig.setProperty( "width", "140" );
		_appConfig.setProperty( "height", "140" );

		_appConfig.setProperty( "rendering", "false" );
		_appConfig.setProperty( "rendering_gif", "false" );
		_appConfig.setProperty( "rendering_gif_framerate", "40" );
		_appConfig.setProperty( "rendering_gif_quality", "15" );
		_appConfig.setProperty( "rendering_gif_startframe", "2" );
		_appConfig.setProperty( "rendering_gif_stopframe", ""+Math.round(_frames) );
	}
	
	public void setup() {
		super.setup();
		p.smooth(OpenGLUtil.SMOOTH_HIGH);
		_logo = p.loadShape(FileUtil.getHaxademicDataPath()+"svg/ello.svg");
		_logoInverse = p.loadShape(FileUtil.getHaxademicDataPath()+"svg/ello-inverse.svg");
	}

	public void drawApp() {
		p.background(255);
//		p.fill(255, 40);
//		p.rect(0, 0, p.width, p.height);
		p.noStroke();
		
		float frameRadians = PConstants.TWO_PI / _frames;
		float percentComplete = ((float)(p.frameCount%_frames)/_frames);
		float easedPercent = Penner.easeInOutQuart(percentComplete, 0, 1, 1);

		float frameOsc = P.sin( PConstants.TWO_PI * percentComplete);
//		float elloSize = (float)(p.width/1.5f + 7f * frameOsc);
		float elloSize = (float)(p.width);
		
		DrawUtil.setDrawCorner(p);
		
		p.translate(p.width/2, p.height/2);
//		p.rotate(frameRadians * p.frameCount);
		p.rotate(easedPercent * PConstants.TWO_PI);
		p.shape(_logo, 0, 0, elloSize, elloSize);
	}
}



