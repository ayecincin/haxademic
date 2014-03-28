package com.haxademic.core.hardware.kinect;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.math.MathUtil;

public class KinectRegion {
	
	protected PAppletHax p;

	protected int _left = 0;
	protected int _right = 0;
	protected int _near = 0;
	protected int _far = 0;
	protected int _top = 0;
	protected int _bottom = 0;
	protected int _resolution = 10;
	protected int _blockColor = -1;
	
	protected int _pixelCount = 0;
	protected float _controlX = 0.5f;
	protected float _controlZ = 0.5f;
	
	public KinectRegion( int left, int right, int near, int far, int top, int bottom, int resolution, int blockColor ) {
		p = P.p;
		_left = left;
		_right = right;
		_near = near;
		_far = far;
		_top = top;
		_bottom = bottom;
		_resolution = resolution;
		_blockColor = blockColor;
	}
	
	public int pixelCount() {
		return _pixelCount;
	}
	
	public float controlX() {
		return _controlX;
	}

	public float controlZ() {
		return _controlZ;
	}

	public void drawRect() {
		if( _blockColor == -1 ) return;
		p.stroke( _blockColor );
		p.fill( _blockColor, P.min(_pixelCount * 5, 255) );
		p.rect(_left, _near, _right - _left, _far - _near);
	}
	
	public void detect( boolean isDebugging ) {
		// find kinect readings in the region
		_pixelCount = 0;
		float controlXTotal = 0;
		float controlZTotal = 0;
		float pixelDepth = 0;
		if( p.kinectWrapper != null ) {
			for ( int x = _left; x < _right; x += _resolution ) {
				for ( int y = _top; y < _bottom; y += _resolution ) {
					pixelDepth = p.kinectWrapper.getMillimetersDepthForKinectPixel( x, y );
					if( pixelDepth != 0 && pixelDepth > _near && pixelDepth < _far ) {
						if( isDebugging == true ) {
							p.noStroke();
							p.fill( _blockColor, 200 );
							p.pushMatrix();
							p.translate(x, y, -pixelDepth);
							p.box(_resolution, _resolution, _resolution);
							p.popMatrix();
						}
						// add up for calculations
						_pixelCount++;
						controlXTotal += x;
						controlZTotal += pixelDepth;
					}
				}
			}
		}
		
		// if we have enough blocks in a region, update the player's joystick position
		 if( _pixelCount > 20 ) {
			// compute averages
			if( controlXTotal > 0 && controlZTotal > 0 ) {
				float avgX = controlXTotal / _pixelCount;
				_controlX = MathUtil.getPercentWithinRange(_left, _right, avgX) - 0.5f;
				float avgZ = controlZTotal / _pixelCount;
				_controlZ = MathUtil.getPercentWithinRange(_near, _far, avgZ) - 0.5f;

				// show debug
				if( isDebugging == true ) {
					p.fill( 255 );
					p.pushMatrix();
					p.translate(avgX, 220, -avgZ);
					p.box(40, 480, 40);
					p.popMatrix();
				}
			}
		 }
	}
}
