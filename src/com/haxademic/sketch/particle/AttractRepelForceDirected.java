package com.haxademic.sketch.particle;

import java.util.Iterator;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.particle.ForceDirectedLayout;
import com.haxademic.core.draw.particle.ForceDirectedLayout.ForceNode;

public class AttractRepelForceDirected
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected ForceDirectedLayout forceGraph; 
	
	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.WIDTH, 800 );
		p.appConfig.setProperty( AppSettings.HEIGHT, 800 );
		p.appConfig.setProperty( AppSettings.RETINA, false );
		p.appConfig.setProperty( AppSettings.SHOW_DEBUG, false);
		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE, false);
		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE_START_FRAME, 1);
		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE_STOP_FRAME, 830);
	}

	public void setup() {
		super.setup();
		p.background(0);
		forceGraph = new ForceDirectedLayout(100, 100, 600, 600, 140, 3);
		forceGraph.setFrameDelay(2);
		forceGraph.setFfFactor(0.9f);
		forceGraph.setKFactor(0.9f);
	}
	
	public void drawApp() {
		DrawUtil.fadeToBlack(p.g, 10f);
		p.fill(200);
		p.noStroke();
		DrawUtil.setDrawCenter(p);
		forceGraph.update();
		for(Iterator<ForceNode> it=forceGraph.nodes().iterator(); it.hasNext();){
			ForceNode node = it.next();
			p.ellipse(node.position().x, node.position().y, node.mass() * 4f, node.mass() * 4f);
		}
		if(p.frameCount == 600) {
			forceGraph.setTargetNodes(0);
			forceGraph.setFrameDelay(1);
		}
	}

	public void keyPressed(){
		if (key=='+'){
			for (int i = 0; i < 10; i++) forceGraph.addTargetNodes();
		} else if (key=='-'){
			for (int i = 0; i < 10; i++) forceGraph.removeTargetNodes();
		}
	}

}
