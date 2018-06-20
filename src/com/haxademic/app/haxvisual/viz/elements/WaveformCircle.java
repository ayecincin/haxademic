package com.haxademic.app.haxvisual.viz.elements;

import com.haxademic.app.haxvisual.viz.ElementBase;
import com.haxademic.app.haxvisual.viz.IVizElement;
import com.haxademic.core.app.P;

import processing.core.PApplet;
import toxi.processing.ToxiclibsSupport;

public class WaveformCircle
extends ElementBase 
implements IVizElement {
	
//	protected WaveformData _waveformData;
	protected float _circleInc;
	protected float _baseRadius;
	protected float _amp;
	protected float _strokeWeight;
	
	public WaveformCircle( PApplet p, ToxiclibsSupport toxi ) {
		super( p, toxi );
		init();
	}

	public void init() {
		// grab reference to Haxademic waveform data array object and split circle up into number of segments
//		_waveformData = ((PAppletHax)p)._waveformData;
		_circleInc = ( (float)Math.PI * 2.0f ) / P.p.audioData.waveform().length;
		
		// set some defaults
		_baseRadius = 100;
		_amp = 20;
		_strokeWeight = 3;
	}
	
	public void setDrawProps(float strokeWeight, float baseRadius, float amp) {
		_strokeWeight = strokeWeight;
		_baseRadius = baseRadius;
		_amp = amp;
	}

	public void update() {
		int numPoints = P.p.audioData.waveform().length;
		p.strokeWeight( _strokeWeight );
		p.strokeCap( p.ROUND );
		p.beginShape();
//		int iNext = 0;
		float radius;//, radiusNext;
		for (int i = 0; i < numPoints; i++ ) {
//			iNext = (i == numPoints - 1) ? 0 : i+1;	// makes sure we wrap around at the end
			radius =     _baseRadius + P.p.audioData.waveform()[i] * _amp;
//			radiusNext = _baseRadius + P.p.audioData.waveform()[iNext] * _amp;
//			p.line( p.sin( _circleInc * i ) * radius , p.cos( _circleInc * i ) * radius, p.sin( _circleInc * iNext ) * radiusNext, p.cos( _circleInc * iNext ) * radiusNext );
			p.vertex( p.sin( _circleInc * i ) * radius , p.cos( _circleInc * i ) * radius );
		}
		// connect 1st and last points
		radius = _baseRadius + P.p.audioData.waveform()[0] * _amp;
		p.vertex( p.sin( _circleInc * 0 ) * radius , p.cos( _circleInc * 0 ) * radius );
		p.endShape();

	}

	public void reset() {
		
	}

	public void dispose() {
//		_waveformData = null;
	}

}
