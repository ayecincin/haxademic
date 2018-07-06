package com.haxademic.core.draw.filters.shaders;

import com.haxademic.core.draw.filters.shaders.shared.BaseFilter;

import processing.core.PApplet;
import processing.core.PGraphics;

public class RotateFilter
extends BaseFilter {

	public static RotateFilter instance;
	
	public RotateFilter(PApplet p) {
		super(p, "haxademic/shaders/filters/rotate.glsl");
		setRotation(0);
		setZoom(1f);
		setOffset(0f, 0f);
	}
	
	public static RotateFilter instance(PApplet p) {
		if(instance != null) return instance;
		instance = new RotateFilter(p);
		return instance;
	}
	
	public void applyTo(PGraphics pg) {
		shader.set("textureDupe", pg);
		super.applyTo(pg);
	}
	
	public void applyTo(PApplet p) {
		shader.set("textureDupe", p.g);
		super.applyTo(p);
	}
	
	public void setRotation(float rotation) {
		shader.set("rotation", rotation);
	}
	
	public void setZoom(float zoom) {
		shader.set("zoom", zoom);
	}
	
	public void setOffset(float x, float y) {
		shader.set("offset", x, y);
	}
	
	
}
