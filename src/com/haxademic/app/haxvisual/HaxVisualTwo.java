package com.haxademic.app.haxvisual;

import java.util.ArrayList;
import java.util.Collections;

import com.haxademic.app.haxmapper.dmxlights.RandomLightTiming;
import com.haxademic.app.haxvisual.pools.HaxVisualTexturesAll;
import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.constants.AppSettings;
import com.haxademic.core.constants.PBlendModes;
import com.haxademic.core.draw.color.ImageGradient;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.context.OpenGLUtil;
import com.haxademic.core.draw.filters.shaders.BadTVLinesFilter;
import com.haxademic.core.draw.filters.shaders.BlurHFilter;
import com.haxademic.core.draw.filters.shaders.BlurVFilter;
import com.haxademic.core.draw.filters.shaders.BrightnessFilter;
import com.haxademic.core.draw.filters.shaders.ColorDistortionFilter;
import com.haxademic.core.draw.filters.shaders.ColorizeFromTexture;
import com.haxademic.core.draw.filters.shaders.ContrastFilter;
import com.haxademic.core.draw.filters.shaders.CubicLensDistortionFilterOscillate;
import com.haxademic.core.draw.filters.shaders.DisplacementMapFilter;
import com.haxademic.core.draw.filters.shaders.EdgesFilter;
import com.haxademic.core.draw.filters.shaders.HalftoneFilter;
import com.haxademic.core.draw.filters.shaders.HueFilter;
import com.haxademic.core.draw.filters.shaders.InvertFilter;
import com.haxademic.core.draw.filters.shaders.KaleidoFilter;
import com.haxademic.core.draw.filters.shaders.LeaveBlackFilter;
import com.haxademic.core.draw.filters.shaders.LiquidWarpFilter;
import com.haxademic.core.draw.filters.shaders.MaskThreeTextureFilter;
import com.haxademic.core.draw.filters.shaders.MirrorFilter;
import com.haxademic.core.draw.filters.shaders.PixelateFilter;
import com.haxademic.core.draw.filters.shaders.RadialRipplesFilter;
import com.haxademic.core.draw.filters.shaders.SphereDistortionFilter;
import com.haxademic.core.draw.filters.shaders.VignetteAltFilter;
import com.haxademic.core.draw.filters.shaders.VignetteFilter;
import com.haxademic.core.draw.filters.shaders.WobbleFilter;
import com.haxademic.core.draw.image.ImageCyclerBuffer;
import com.haxademic.core.draw.image.ImageUtil;
import com.haxademic.core.draw.mapping.PGraphicsKeystone;
import com.haxademic.core.draw.textures.pgraphics.TextureSphereAudioTextures;
import com.haxademic.core.draw.textures.pgraphics.shared.BaseTexture;
import com.haxademic.core.file.FileUtil;
import com.haxademic.core.hardware.midi.devices.AbletonNotes;
import com.haxademic.core.hardware.midi.devices.AkaiMpdPads;
import com.haxademic.core.hardware.midi.devices.LaunchControl;
import com.haxademic.core.hardware.osc.devices.TouchOscPads;
import com.haxademic.core.hardware.shared.InputTrigger;
import com.haxademic.core.math.MathUtil;

import processing.core.PGraphics;
import processing.core.PImage;

/**
 * 
 * TODO:  
 * Add new texture shaders and special effects like two-color shader and screen repeating & rotation shaders  
 * Add new concepts about layout, rather than just relying on displacement & mask effects 
 * Add text cycling texture
 * Add DrawUtil image rotation, just like the new post-draw scale function
 * Add tinting to layers - maybe a shader to re-color everything with a gradient map
 * mirror or kaleido the boring audio reactive textures
 * do something with the unicorn .obj model
     * 3d model layer always on top - receives current textures to apply to self
     * can we recreate the MeshDeform class from the old viz app - yes
     * Use DrawMesh.drawPointsWithAudio() with PShape. Also, deform style from SphereTextureLines class would be good - MeshUtil.deformMeshWithAudio()
 * Displacement layer should act as mesh displace map
 * Fix some old shaders - they go too fast
 */

public class HaxVisualTwo
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected float BEAT_DIVISOR = 1; // 10 to test, 1 by default
	protected int BEAT_INTERVAL_COLOR = (int) Math.ceil(6f / BEAT_DIVISOR);
	protected int BEAT_INTERVAL_ROTATION = (int) Math.ceil(6f / BEAT_DIVISOR);
	protected int BEAT_INTERVAL_TRAVERSE = (int) Math.ceil(20f / BEAT_DIVISOR);
	protected int BEAT_INTERVAL_ALL_SAME = (int) Math.ceil(150f / BEAT_DIVISOR);
	protected int BEAT_INTERVAL_NEW_MODE = (int) Math.ceil(80f / BEAT_DIVISOR);
	protected int BEAT_INTERVAL_NEW_TIMING = (int) Math.ceil(10f / BEAT_DIVISOR);
	protected int BEAT_INTERVAL_BIG_CHANGE = (int) Math.ceil(120f / BEAT_DIVISOR);

	protected String _inputFileLines[];
	protected ArrayList<BaseTexture> _bgTexturePool;
	protected ArrayList<BaseTexture> _fgTexturePool;
	protected ArrayList<BaseTexture> _overlayTexturePool;
	protected TextureSphereAudioTextures topLayer;
	
	protected ArrayList<BaseTexture> _curTexturePool;
	
	protected int layerSwapIndex = 0;
	protected int[] poolCurTextureIndexes = new int[]{0,0,0};
	protected ArrayList<BaseTexture>[] texturePools;

	protected boolean _debugTextures = false;
	protected boolean DEBUG_TEXTURE_SAVE_IMAGE_PREVIEWS = false;
	
	protected int midiInChannel = 0;
	
	protected int displaceMapLayerKnob = 21;
	protected int overlayModeKnob = 41;
	protected int invertKnob = 22;
	protected int vignetteKnob = 42;
	protected int distAmpKnob = 23;
	protected int distTimeKnob = 43;
	protected int warpKnobAmp = 44;
	protected int warpKnobFreq = 24;
	protected int kaledioKnob = 25;
	protected int effectsKnob = 46;
	protected int pixelateKnob = 26;
	protected int contrastKnob = 28;
	protected int brightnessKnob = 48;
	protected int interstitialKnob = 47;

	protected InputTrigger _colorTrigger = new InputTrigger(new char[]{'c'},new String[]{TouchOscPads.PAD_01},new Integer[]{AkaiMpdPads.PAD_01, LaunchControl.PAD_03, AbletonNotes.NOTE_01});
	protected InputTrigger _rotationTrigger = new InputTrigger(new char[]{'v'},new String[]{TouchOscPads.PAD_02},new Integer[]{AkaiMpdPads.PAD_02, LaunchControl.PAD_04, AbletonNotes.NOTE_02});
	protected InputTrigger _timingTrigger = new InputTrigger(new char[]{'n'},new String[]{TouchOscPads.PAD_03},new Integer[]{AkaiMpdPads.PAD_03, LaunchControl.PAD_01, AbletonNotes.NOTE_03});
	protected InputTrigger _modeTrigger = new InputTrigger(new char[]{'m'},new String[]{TouchOscPads.PAD_04},new Integer[]{AkaiMpdPads.PAD_04, LaunchControl.PAD_05, AbletonNotes.NOTE_04});
	protected InputTrigger _timingSectionTrigger = new InputTrigger(new char[]{'f'},new String[]{TouchOscPads.PAD_05},new Integer[]{AkaiMpdPads.PAD_05, LaunchControl.PAD_02, AbletonNotes.NOTE_05});
//	protected InputTrigger _allSameTextureTrigger = new InputTrigger(new char[]{'a'},new String[]{TouchOscPads.PAD_06},new Integer[]{AkaiMpdPads.PAD_06, AbletonNotes.NOTE_06});
	protected InputTrigger _bigChangeTrigger = new InputTrigger(new char[]{' '},new String[]{TouchOscPads.PAD_07},new Integer[]{AkaiMpdPads.PAD_07, LaunchControl.PAD_08, AbletonNotes.NOTE_07});
	protected InputTrigger _lineModeTrigger = new InputTrigger(new char[]{'l'},new String[]{TouchOscPads.PAD_08},new Integer[]{AkaiMpdPads.PAD_08, LaunchControl.PAD_06, AbletonNotes.NOTE_08});


	protected InputTrigger _audioInputUpTrigger = new InputTrigger(new char[]{},new String[]{"/7/nav1"},new Integer[]{26});
	protected InputTrigger _audioInputDownTrigger = new InputTrigger(new char[]{},new String[]{"/7/nav2"},new Integer[]{25});
	protected InputTrigger _brightnessUpTrigger = new InputTrigger(new char[]{']'},new String[]{},new Integer[]{});
	protected InputTrigger _brightnessDownTrigger = new InputTrigger(new char[]{'['},new String[]{},new Integer[]{});
	protected InputTrigger _keystoneResetTrigger = new InputTrigger(new char[]{'k'},new String[]{},new Integer[]{});
	protected InputTrigger _debugTexturesTrigger = new InputTrigger(new char[]{'d'},new String[]{},new Integer[]{});
	protected int _lastInputMillis = 0;
	protected int numBeatsDetected = 0;
	protected int lastTimingUpdateTime = 0;
	protected int lastTimingUpdateDelay = 500;

//	protected InputTrigger _programDownTrigger = new InputTrigger(new char[]{'1'},new String[]{TouchOscPads.PAD_15},new Integer[]{AkaiMpdPads.PAD_15, 27});
//	protected InputTrigger _programUpTrigger = new InputTrigger(new char[]{'2'},new String[]{TouchOscPads.PAD_16},new Integer[]{AkaiMpdPads.PAD_16, 28});
//	protected int _programIndex = 0;

	protected RandomLightTiming _dmxLights;

	protected float _brightnessVal = 1f;
	protected ImageGradient imageGradient;
	protected boolean imageGradientLuma = false;
	protected boolean imageGradientFilter = false;
	
	protected int displacementLayer = 0;
	protected int overlayMode = 0;
		
	// global effects processing
	protected static int[] _textureEffectsIndices = {0,0,0,0,0,0,0};	// store a effects number for each texture position after the first
	protected int _numTextureEffects = 16 + 8; // +8 to give a good chance at removing the filter from the texture slot

	// keystonable screen
	protected PGraphics _pg;
	protected PGraphicsKeystone _pgPinnable;
	protected float scaleDownPG = 1f;
	
	//////////////////////////////////////////////////
	// INIT
	//////////////////////////////////////////////////

	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE, false );
		p.appConfig.setProperty( AppSettings.FULLSCREEN, false );
		p.appConfig.setProperty( AppSettings.ALWAYS_ON_TOP, false );
		p.appConfig.setProperty( AppSettings.FILLS_SCREEN, false );
		p.appConfig.setProperty( AppSettings.OSC_ACTIVE, false );
		p.appConfig.setProperty( AppSettings.DMX_LIGHTS_COUNT, 0 );
		p.appConfig.setProperty( AppSettings.AUDIO_DEBUG, true );
//		p.appConfig.setProperty( AppSettings.INIT_ESS_AUDIO, true );
//		p.appConfig.setProperty( AppSettings.INIT_MINIM_AUDIO, true );
		p.appConfig.setProperty( AppSettings.INIT_BEADS_AUDIO, true );
		p.appConfig.setProperty( AppSettings.MIDI_DEVICE_IN_INDEX, 0 );
		p.appConfig.setProperty( AppSettings.MIDI_DEBUG, false );
		p.appConfig.setProperty( AppSettings.RETINA, false );
		p.appConfig.setProperty( AppSettings.WIDTH, 1200 );
//		p.appConfig.setProperty( AppSettings.SMOOTHING, AppSettings.SMOOTH_NONE );
	}

	protected void setupFirstFrame() {
		initDMX();
		buildCanvas();
		buildTextures();
		buildPostProcessingChain();
//		buildInterstitial();
	}
	
	protected void buildCanvas() {
		//_pg = p.createGraphics( P.round(p.width / scaleDownPG), P.round(p.height / scaleDownPG), P.P3D );
		int w = P.round(p.width * scaleDownPG);
		int h = P.round(p.height * scaleDownPG);
		_pg = p.createGraphics(w, h, P.P3D);
		OpenGLUtil.setTextureRepeat(_pg);
		_pgPinnable = new PGraphicsKeystone( p, _pg, 12, FileUtil.getFile("text/keystoning/hax-visual-two.txt") );
	}

	protected void initDMX() {
		if(p.appConfig.getInt(AppSettings.DMX_LIGHTS_COUNT, 0) > 0) {
			_dmxLights = new RandomLightTiming(p.appConfig.getInt(AppSettings.DMX_LIGHTS_COUNT, 0));
		}
	}
	
	protected void buildTextures() {
		_bgTexturePool = new ArrayList<BaseTexture>();
		_fgTexturePool = new ArrayList<BaseTexture>();
		_overlayTexturePool = new ArrayList<BaseTexture>();
		topLayer = new TextureSphereAudioTextures( _pg.width, _pg.height );
		texturePools = new ArrayList[]{_bgTexturePool, _fgTexturePool, _overlayTexturePool};
		_curTexturePool = new ArrayList<BaseTexture>();
		HaxVisualTexturesAll.addTexturesToPoolMinimal(_pg, _bgTexturePool, _fgTexturePool, _overlayTexturePool);
//		HaxVisualTexturesAll.addTexturesToPool(_pg, _bgTexturePool, _fgTexturePool, _overlayTexturePool);
		prepareTexturePools();
	}


	protected void buildPostProcessingChain() {
		KaleidoFilter.instance(p).setAngle(0f);
		KaleidoFilter.instance(p).setSides(2f);

		HalftoneFilter.instance(p).setSizeT(256f, 256f);
		HalftoneFilter.instance(p).setAngle(P.HALF_PI);
		HalftoneFilter.instance(p).setCenter(0.5f, 0.5f);
		HalftoneFilter.instance(p).setScale(1f);

		PixelateFilter.instance(p).setDivider(20f, _pg.width, _pg.height);
		
		p.midiState.controllerChange(midiInChannel, vignetteKnob, (int) 70);
	}
	
	protected ImageCyclerBuffer imageCycler;
	protected void buildInterstitial() {
		String imagesPath = "images/_sketch/glissline-interstitials/";
		ArrayList<String> imageFiles = FileUtil.getFilesInDirOfType(FileUtil.getFile(imagesPath), "jpg");
		PImage[] images = new PImage[imageFiles.size()];
		for (int i = 0; i < imageFiles.size(); i++) {
			images[i] = p.loadImage(imagesPath + imageFiles.get(i));
			P.println(imageFiles.get(i));
		}
		imageCycler = new ImageCyclerBuffer(1398, 1080, images, 500, 0.5f);
	}
	
	//////////////////////////////////////////////
	// DRAW
	//////////////////////////////////////////////

	public void drawApp() {
		background(0);
		handleInputTriggers();
		checkBeat();
		getDisplacementLayer();
		drawLayers();
		filterActiveTextures();
		drawTopLayer();
		postProcessFilters();
		postBrightness();
		if(imageCycler != null) drawInterstitial();
		// draw pinned pgraphics
		if(_debugTextures == true) _pgPinnable.drawTestPattern();
		p.debugView.setTexture(_pg);
		_pgPinnable.update(p.g);
		sendDmxLights();
	}

	protected void drawLayers() {
		// update textures
		for( int i=0; i < _curTexturePool.size(); i++ ) {
			BaseTexture tex = _curTexturePool.get(i);
			if(tex != null && tex.texture() != null) {
				tex.update();
			}
		}
		
		// set kaleido on audio layer
		PGraphics audioLayer = _curTexturePool.get(_curTexturePool.size() - 1).texture();
		KaleidoFilter.instance(p).setSides(6f);
		KaleidoFilter.instance(p).applyTo(audioLayer);

		// custom update for the sphere
		topLayer.update();
		
		// composite textures
		if(overlayMode != 3 || displacementLayer == 3) {	// we'll use the mask shader if 3, and no need to draw here
			_pg.beginDraw();
			_pg.background(0);
			_pg.blendMode(PBlendModes.EXCLUSION);
	//		OpenGLUtil.setBlending(p.g, true);
	//		OpenGLUtil.setBlendMode(p.g, OpenGLUtil.Blend.DARK_INVERSE);
			for( int i=0; i < _curTexturePool.size() - 1; i++ ) {	// don't draw the current filter layer
				if(i != displacementLayer) {	// don't draw displacement layer
					BaseTexture tex = _curTexturePool.get(i);
					if(tex != null && tex.texture() != null) {
						ImageUtil.drawImageCropFill(tex.texture(), _pg, true);
					}
				}
			}
			_pg.blendMode(PBlendModes.BLEND);
//			float[] offsetAndSize = ImageUtil.getOffsetAndSizeToCrop(_pg.width, _pg.height, topLayer.texture().width, topLayer.texture().height, true);
//			_pg.image(topLayer.texture(), offsetAndSize[0], offsetAndSize[1], offsetAndSize[2], offsetAndSize[3]);
			_pg.endDraw();
		}
	}
	
	protected void drawTopLayer() {
		_pg.beginDraw();
		ImageUtil.drawImageCropFill(topLayer.texture(), _pg, true);
		_pg.endDraw();
	}

	/////////////////////////////////////////////////////////////////
	// POST PROCESSING EFFECTS
	/////////////////////////////////////////////////////////////////
	
	protected void getDisplacementLayer() {		
		displacementLayer = P.round(P.map(p.midiState.midiCCPercent(midiInChannel, displaceMapLayerKnob), 0, 1, 0, 3));
		overlayMode = P.round(P.map(p.midiState.midiCCPercent(midiInChannel, overlayModeKnob), 0, 1, 0, 3));
	}
	
	protected void postProcessFilters() {
		// DISPLACEMENT MAP ////////////////////////
		// which layer to use for displacement?
		p.debugView.setValue("overlayMode", overlayMode);
		if(displacementLayer < 3) {
			if(displacementLayer >= _curTexturePool.size()) displacementLayer = _curTexturePool.size() - 1; // protection!
			PGraphics displacementBuffer = _curTexturePool.get(displacementLayer).texture();
			if(overlayMode == 0) {
				// zoom into displacement image
	//			DrawUtil.zoomReTexture(_curTexturePool.get(displacementLayer).texture(), 0.66f + 0.33f * P.sin(p.frameCount * 0.01f));
				// add blur to displacement image
				float blurPercent = 2f; // p.mousePercentX() * 10f;
				BlurHFilter.instance(p).setBlurByPercent(blurPercent, _pg.width);
				BlurVFilter.instance(p).setBlurByPercent(blurPercent, _pg.height);
				BlurHFilter.instance(p).applyTo(displacementBuffer);
				BlurVFilter.instance(p).applyTo(displacementBuffer);
				BlurHFilter.instance(p).applyTo(displacementBuffer);
				BlurVFilter.instance(p).applyTo(displacementBuffer);
				// set current layer as displacer & apply effect
				DisplacementMapFilter.instance(p).setMap(displacementBuffer);
				DisplacementMapFilter.instance(p).setMode(3);
				DisplacementMapFilter.instance(p).applyTo(_pg);
			} else if(overlayMode == 1) {
				LeaveBlackFilter.instance(p).setMix(1f);
				LeaveBlackFilter.instance(p).applyTo(displacementBuffer);
				_pg.beginDraw();
				ImageUtil.drawImageCropFill(displacementBuffer, _pg, true);
				_pg.endDraw();
			} else if(overlayMode == 2) {
				_pg.beginDraw();
				_pg.blendMode(PBlendModes.EXCLUSION);
				ImageUtil.drawImageCropFill(displacementBuffer, _pg, true);
				_pg.blendMode(PBlendModes.BLEND);
				_pg.endDraw();
			} else if(overlayMode == 3) {
				// ADD SHADER TO MASK & REVERSE MASK THE OPPOSITE 2 TEXTURES
				PGraphics tex1;
				PGraphics tex2;
				if(displacementLayer == 0) { 		tex1 = _curTexturePool.get(1).texture(); tex2 = _curTexturePool.get(2).texture(); }
				else if(displacementLayer == 1) { 	tex1 = _curTexturePool.get(0).texture(); tex2 = _curTexturePool.get(2).texture(); }
				else { 								tex1 = _curTexturePool.get(0).texture(); tex2 = _curTexturePool.get(1).texture(); }
				MaskThreeTextureFilter.instance(p).setMask(displacementBuffer);
				MaskThreeTextureFilter.instance(p).setTexture1(tex1);
				MaskThreeTextureFilter.instance(p).setTexture2(tex2);
				MaskThreeTextureFilter.instance(p).applyTo(_pg);
			}
		}
		
		// CONTRAST ////////////////////////
		if( p.midiState.midiCCPercent(midiInChannel, contrastKnob) != 0 ) {
			if(p.midiState.midiCCPercent(midiInChannel, contrastKnob) > 0.1f) {
				ContrastFilter.instance(p).setContrast(p.midiState.midiCCPercent(midiInChannel, contrastKnob) * 7f);
			}
		}

		// MULTIPLE EFFECTS KNOB ////////////////////////
		boolean halftone = ( p.midiState.midiCCPercent(midiInChannel, effectsKnob) > 0.25f && p.midiState.midiCCPercent(midiInChannel, effectsKnob) < 0.5f );
		if( halftone ) HalftoneFilter.instance(p).applyTo(_pg);

		boolean edged = ( p.midiState.midiCCPercent(midiInChannel, effectsKnob) > 0.5f && p.midiState.midiCCPercent(midiInChannel, effectsKnob) < 0.75f );
		if( edged ) EdgesFilter.instance(p).applyTo(_pg);

		boolean pixelated = ( p.midiState.midiCCPercent(midiInChannel, effectsKnob) > 0.75f );
		if( pixelated ) {
			float pixAmout = P.round(p.midiState.midiCCPercent(midiInChannel, pixelateKnob) * 40f);
			PixelateFilter.instance(p).setDivider(p.width/pixAmout, _pg.width, _pg.height);
			if(p.midiState.midiCCPercent(midiInChannel, pixelateKnob) > 0) PixelateFilter.instance(p).applyTo(_pg);
		}

		// INVERT ////////////////////////
		boolean inverted = ( p.midiState.midiCCPercent(midiInChannel, invertKnob) > 0.5f );
		if( inverted ) InvertFilter.instance(p).applyTo(_pg);
		
		// COLOR DISTORTION ///////////////////////
		// color distortion auto
		int distAutoFrame = p.frameCount % 6000;
		float distFrames = 100f;
		if(distAutoFrame <= distFrames) {
			float distAmpAuto = P.sin(distAutoFrame/distFrames * P.PI);
			p.midiState.controllerChange(0, distAmpKnob, P.round(127 * distAmpAuto));
			p.midiState.controllerChange(0, distTimeKnob, P.round(127 * distAmpAuto));
		}
		
		// color distortion
		float colorDistortionAmp = p.midiState.midiCCPercent(midiInChannel, distAmpKnob) * 2.5f;
		float colorDistortionTimeMult = p.midiState.midiCCPercent(midiInChannel, distTimeKnob);
		if(colorDistortionAmp > 0) {
			float prevTime = ColorDistortionFilter.instance(p).getTime();
			ColorDistortionFilter.instance(p).setTime(prevTime + 1/100f * colorDistortionTimeMult);
			ColorDistortionFilter.instance(p).setAmplitude(colorDistortionAmp);
			ColorDistortionFilter.instance(p).applyTo(_pg);
		}

		// WARP /////////////////////////
//		int warpAutoFrame = p.frameCount % 200;
//		float warpFrames = 100f;
//		if(warpAutoFrame <= warpFrames) {
//			float warpAmpAuto = P.sin(warpAutoFrame/warpFrames * P.PI);
//			p.midi.controllerChange(0, warpKnobAmp, 86);
//			p.midi.controllerChange(0, warpKnobFreq, P.round(0.1f * P.round(127 * warpAmpAuto)));
//		}

		float warpAmp = p.midiState.midiCCPercent(midiInChannel, warpKnobAmp) * 0.1f;
		float warpFreq = p.midiState.midiCCPercent(midiInChannel, warpKnobFreq) * 10f;
		if(warpAmp > 0) {
			LiquidWarpFilter.instance(p).setAmplitude(warpAmp);
			LiquidWarpFilter.instance(p).setFrequency(warpFreq);
			LiquidWarpFilter.instance(p).setTime(p.frameCount / 40f);
			LiquidWarpFilter.instance(p).applyTo(_pg);
		}

		// KALEIDOSCOPE ////////////////////////
		float kaleidoSides = P.round( p.midiState.midiCCPercent(midiInChannel, kaledioKnob) * 12f );
		if( kaleidoSides > 0 ) {
			if( kaleidoSides == 3 ) {
				MirrorFilter.instance(p).applyTo(_pg);
			} else {
				KaleidoFilter.instance(p).setSides(kaleidoSides);
				KaleidoFilter.instance(p).applyTo(_pg);
			}
		}
		
		// COLORIZE FROM TEXTURE ////////////////////////
		// build palette
		if(imageGradient == null) {
			imageGradient = new ImageGradient(ImageGradient.PASTELS());
			imageGradient.addTexturesFromPath(ImageGradient.COOLORS_PATH);
		}
		if(imageGradientFilter) {
			ColorizeFromTexture.instance(p).setTexture(imageGradient.texture());
			ColorizeFromTexture.instance(p).setLumaMult(imageGradientLuma);
			ColorizeFromTexture.instance(p).applyTo(_pg);
		}

		// VIGNETTE FROM CENTER ////////////////////////
		float vignetteVal = p.midiState.midiCCPercent(midiInChannel, vignetteKnob);
		float vignetteDarkness = P.map(vignetteVal, 0, 1, 13f, -13f);
		VignetteAltFilter.instance(p).setSpread(0.5f);
		VignetteAltFilter.instance(p).setDarkness(1f); // vignetteDarkness
		VignetteAltFilter.instance(p).applyTo(_pg);

		// normal vignette
		VignetteFilter.instance(p).setDarkness(0.56f);
		VignetteFilter.instance(p).applyTo(_pg);
	}

	protected void postBrightness() {
		// BRIGHTNESS ////////////////////////
		if(p.midiState.midiCCPercent(midiInChannel, brightnessKnob) != 0) _brightnessVal = p.midiState.midiCCPercent(midiInChannel, brightnessKnob) * 5f;
		BrightnessFilter.instance(p).setBrightness(_brightnessVal);
		BrightnessFilter.instance(p).applyTo(_pg);	
	}
	
	/////////////////////////////////////////////////////////////////
	// DMX LIGHTING
	/////////////////////////////////////////////////////////////////
	
	protected float dmxMultiplier() {
		return p.midiState.midiCCPercent(midiInChannel, 41) * 1.5f;
	}

	protected void sendDmxLights() {
		int dmxKnob = 47;
		if(_dmxLights != null) {
			_dmxLights.update();
			float knobValue = p.midiState.midiCCPercent(midiInChannel, dmxKnob);
			if(knobValue == 0) {
				_dmxLights.setBrightness(1);
			} else if(knobValue > 0.1f) {
				_dmxLights.setBrightness((p.midiState.midiCCPercent(midiInChannel, dmxKnob)-0.1f) * 50f);
			} else {
				_dmxLights.setBrightness(0);
			}
			if(_debugTextures == true) _dmxLights.drawDebug(p.g);
		}
	}

	/////////////////////////////////////////////////////////////////
	// BEAT DETECTION 
	/////////////////////////////////////////////////////////////////

	protected void checkBeat() {
		if( p.audioData.isBeat() == true && isBeatDetectMode() == true ) {
			updateTiming();
		}
	}

	protected boolean isBeatDetectMode() {
		return ( p.millis() - 10000 > _lastInputMillis );
	}

	public void resetBeatDetectMode() {
		_lastInputMillis = p.millis();
		numBeatsDetected = 1;
	}

	/////////////////////////////////////////////////////////////////
	// INPUT 
	/////////////////////////////////////////////////////////////////
	
	public void handleInputTriggers() {

//		if( p.key == 'a' || p.key == 'A' ){
//			_isAutoPilot = !_isAutoPilot;
//			P.println("_isAutoPilot = "+_isAutoPilot);
//		}
//		if( p.key == 'S' ){
//			_isStressTesting = !_isStressTesting;
//			P.println("_isStressTesting = "+_isStressTesting);
//		}
		if ( _colorTrigger.triggered() == true ) {
			resetBeatDetectMode();
			updateColor();
			_lastInputMillis = p.millis();
		}
		if ( _modeTrigger.triggered() == true ) {
			newMode();
			_lastInputMillis = p.millis();
		}
		if ( _lineModeTrigger.triggered() == true ) {
			resetBeatDetectMode();
			updateLineMode();
			_lastInputMillis = p.millis();
		}
		if ( _rotationTrigger.triggered() == true ) {
			resetBeatDetectMode();
			updateRotation();
			_lastInputMillis = p.millis();
		}
		if ( _timingTrigger.triggered() == true ) {
			resetBeatDetectMode();
			updateTiming();
			_lastInputMillis = p.millis();
		}
		if ( _timingSectionTrigger.triggered() == true ) {
			updateTimingSection();
			_lastInputMillis = p.millis();
		}
		if ( _bigChangeTrigger.triggered() == true ) {
			resetBeatDetectMode();
			bigChangeTrigger();
			_lastInputMillis = p.millis();
		}
//		if ( _allSameTextureTrigger.active() == true ) {
//			resetBeatDetectMode();
//			randomLayers();
//			_lastInputMillis = p.millis();
//		}
		if ( _audioInputUpTrigger.triggered() == true ) p.audioData.setGain(p.audioData.gain() + 0.05f);
		if ( _audioInputDownTrigger.triggered() == true ) p.audioData.setGain(p.audioData.gain() - 0.05f);
		if ( _brightnessUpTrigger.triggered() == true ) _brightnessVal += 0.1f;
		if ( _brightnessDownTrigger.triggered() == true ) _brightnessVal -= 0.1f;
		if ( _keystoneResetTrigger.triggered() == true ) _pgPinnable.resetCorners();
		if ( _debugTexturesTrigger.triggered() == true ) _debugTextures = !_debugTextures;
	}

	/////////////////////////////////////////////////////////////////
	// TIMING & PARAMETER UPDATES 
	/////////////////////////////////////////////////////////////////
	
	protected void newMode() {
		for( int i=0; i < _curTexturePool.size(); i++ ) {
			_curTexturePool.get(i).newMode();
		}
		topLayer.newMode();
	}

	protected void updateColor() {
		for( int i=0; i < _curTexturePool.size(); i++ ) {
			_curTexturePool.get(i).setColor( randomColor(1) );
		}
		topLayer.setColor( randomColor(1) );
		if(MathUtil.randBooleanWeighted(p, 0.2f)) imageGradient.randomGradientTexture();
		imageGradientLuma = true; // MathUtil.randBoolean(p);
		imageGradientFilter = true; // MathUtil.randBoolean(p);
	}

	protected void updateLineMode() {
		for( int i=0; i < _curTexturePool.size(); i++ ) {
			_curTexturePool.get(i).newLineMode();
		}
		topLayer.newLineMode();
	}

	protected void updateRotation() {
		for( int i=0; i < _curTexturePool.size(); i++ ) {
			_curTexturePool.get(i).newRotation();
		}
		topLayer.newRotation();
	}

	protected void updateTiming() {
		// tell all textures to update timing
		if(p.millis() > lastTimingUpdateTime + lastTimingUpdateDelay) {
			for( int i=0; i < _curTexturePool.size(); i++ ) {
				_curTexturePool.get(i).updateTiming();
			}
			topLayer.updateTiming();
			lastTimingUpdateTime = p.millis();
			// run auto beat mode
			autoBeatMode();
		}
		if(_dmxLights != null) _dmxLights.updateDmxLightsOnBeat();
	}

	protected void autoBeatMode() {
		if( isBeatDetectMode() == true ) numBeatsDetected++;
		p.debugView.setValue("isBeatDetectMode()", isBeatDetectMode());
		p.debugView.setValue("numBeatsDetected", numBeatsDetected);
		
		if( numBeatsDetected % BEAT_INTERVAL_COLOR == 0 ) {
//			P.println("BEAT_INTERVAL_COLOR");
			updateColor();
		}
		if( numBeatsDetected % BEAT_INTERVAL_ROTATION == 0 ) {
//			P.println("BEAT_INTERVAL_ROTATION");
			updateRotation();
		}
		if( numBeatsDetected % BEAT_INTERVAL_TRAVERSE == 0 ) {
//			P.println("BEAT_INTERVAL_TRAVERSE");
		}
//		updateColor();

		if( numBeatsDetected % BEAT_INTERVAL_ALL_SAME == 0 ) {
//			P.println("BEAT_INTERVAL_ALL_SAME");
			updateLineMode();
		}

		if( numBeatsDetected % BEAT_INTERVAL_NEW_MODE == 0 ) {
//			P.println("BEAT_INTERVAL_ALL_SAME");
			newMode();
		}
		
		if( numBeatsDetected % BEAT_INTERVAL_NEW_TIMING == 0 ) {
//			P.println("BEAT_INTERVAL_NEW_TIMING");
			updateTimingSection();
		}

		// every 400 beats, do something bigger
		if( numBeatsDetected % BEAT_INTERVAL_BIG_CHANGE == 0 ) {
//			P.println("BEAT_INTERVAL_BIG_CHANGE");
			bigChangeTrigger();
		}
	}

	protected void updateTimingSection() {
		for( int i=0; i < _curTexturePool.size(); i++ ) {
			_curTexturePool.get(i).updateTimingSection();
		}
		topLayer.updateTimingSection();
		
		// swap displacement filter option
		displacementLayer = MathUtil.randRange(0, 3);
		overlayMode = MathUtil.randRange(0, 3);	
		p.midiState.controllerChange(midiInChannel, displaceMapLayerKnob, P.round((127f/3.1f) * displacementLayer));
		p.midiState.controllerChange(midiInChannel, overlayModeKnob, P.round((127f/3.1f) * overlayMode));
		// P.println(P.round((127f/3.1f) * displacementLayer), P.round((127f/3.1f) * overlayMode));
		
		// change kaleido
		float kaleidoSides = MathUtil.randRangeDecimal(0, 1);
		if(kaleidoSides < 0.2f) kaleidoSides = 0;
		else if(kaleidoSides < 0.7f) kaleidoSides = 0.25f * 127f;
		else if(kaleidoSides < 0.85f) kaleidoSides = 0.3f * 127f;
		else kaleidoSides = 0.5f * 127f;
		p.midiState.controllerChange(midiInChannel, kaledioKnob, (int) kaleidoSides);
	}

	protected void bigChangeTrigger() {
		// swap each layer in succession, and loop around
		layerSwapIndex++;
		if(layerSwapIndex >= texturePools.length) layerSwapIndex = 0;
		
		// cycle through textures in pools
		poolCurTextureIndexes[layerSwapIndex] += 1;
		if(poolCurTextureIndexes[layerSwapIndex] >= texturePools[layerSwapIndex].size()) {
			poolCurTextureIndexes[layerSwapIndex] = 0;
			Collections.shuffle(texturePools[layerSwapIndex]);	// shuffle after showing all textures in pool
		}
		reloadLayers();
		// add new effects to each layer
		selectNewActiveTextureFilters();
		
		// debug values
		p.debugView.setValue("layerSwapIndex", layerSwapIndex);
		p.debugView.setValue("poolCurTextureIndexes[0]", poolCurTextureIndexes[0]);
		p.debugView.setValue("poolCurTextureIndexes[1]", poolCurTextureIndexes[1]);
		p.debugView.setValue("poolCurTextureIndexes[2]", poolCurTextureIndexes[2]);
		
		// make sure time steppers don't go wild
		updateTiming();
	}

	/////////////////////////////////////////////////////////////////
	// COLORS?
	/////////////////////////////////////////////////////////////////

	protected int randomColor( float mult ) {
		float baseR = 180 + 55 * P.sin(p.frameCount/100);
		float baseG = 180 + 55 * P.sin(p.frameCount/120);
		float baseB = 180 + 55 * P.sin(p.frameCount/135);
		return p.color(
			(baseR + p.random(-20, 20)) * mult,
			(baseG + p.random(-20, 20)) * mult,
			(baseB + p.random(-20, 20)) * mult
		);
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXTURE POOL MANAGEMENT
	/////////////////////////////////////////////////////////////////
	
	protected void prepareTexturePools() {
		// make sure all textures are not playing videos, etc
		for(BaseTexture tex : _bgTexturePool) tex.setActive(false);
		for(BaseTexture tex : _fgTexturePool) tex.setActive(false);
		for(BaseTexture tex : _overlayTexturePool) tex.setActive(false);

		// randomize all pools
		Collections.shuffle(_bgTexturePool);
		Collections.shuffle(_fgTexturePool);
		Collections.shuffle(_overlayTexturePool);

		// add inital textures to current array
		reloadLayers();

		// output to images
		if(DEBUG_TEXTURE_SAVE_IMAGE_PREVIEWS == true) {
			outputTestImages(_bgTexturePool);
			outputTestImages(_fgTexturePool);
			outputTestImages(_overlayTexturePool);
		}
	}
	
	protected void clearCurrentLayers() {
		for(BaseTexture tex : _curTexturePool) tex.setActive(false);
		for(BaseTexture tex : _curTexturePool) tex.setKnockoutBlack(false);
//		for(BaseTexture tex : _curTexturePool) tex.setAsOverlay(false);
		// remove from debug panel
		for (int i = 0; i < _curTexturePool.size(); i++) {
			p.debugView.removeTexture(_curTexturePool.get(i).texture());
		}
		_curTexturePool.clear();
	}
	
	protected void reloadLayers() {
		clearCurrentLayers();
		
		// reload current 3 layers
		for (int i = 0; i < texturePools.length; i++) {
			_curTexturePool.add( texturePools[i].get(poolCurTextureIndexes[i]) );
			// debug info
			p.debugView.setTexture(texturePools[i].get(poolCurTextureIndexes[i]).texture());
			p.debugView.setValue("texture "+i, texturePools[i].get(poolCurTextureIndexes[i]).toString());
		}
		
		// set current textures as active
		for(BaseTexture tex : _curTexturePool) { 
			tex.setActive(true); 
		}
		
		// tell the top layer
		topLayer.setCurTexturePool(_curTexturePool);
		
		p.debugView.setValue("_curTexturePool.size()", _curTexturePool.size());
	}

	protected BaseTexture randomTexture(ArrayList<BaseTexture> pool) {
		BaseTexture newTexture = pool.get( MathUtil.randRange(0, pool.size()-1 ) );
//		if(newTexture instanceof TextureVideoPlayer) {
//			newTexture.setActive(true);
//		}
		return newTexture;
	}
	
	
	
	
	
	
	
	/////////////////////////////////////////////////////////////////
	// TEXTURE-SPECIFIC POST-PROCESSING
	/////////////////////////////////////////////////////////////////

	protected void selectNewActiveTextureFilters() {
		for(int i=0; i < _textureEffectsIndices.length; i++) {
			if(MathUtil.randRange(0, 10) > 8) {
				_textureEffectsIndices[i] = MathUtil.randRange(0, _numTextureEffects);
			}
		}
//		P.println("_textureEffectsIndices", _textureEffectsIndices.toString());
	}
	
	protected void filterActiveTextures() {
		for( int i=0; i < _curTexturePool.size(); i++ ) {
			if(_curTexturePool.get(i).isActive() == true) {
				PGraphics pg = _curTexturePool.get(i).texture();
				applyFilterToTexture(pg, i);
			}
		}
	}
	
	public static void applyFilterToTexture(PGraphics pg, int effectIndex) {
		float filterTime = p.frameCount / 40f;
		
		int textureEffectIndex = _textureEffectsIndices[effectIndex];
		if(textureEffectIndex == 1) {
			KaleidoFilter.instance(p).setSides(4);
			KaleidoFilter.instance(p).setAngle(filterTime / 10f);
			KaleidoFilter.instance(p).applyTo(pg);
//		} else if(textureEffectIndex == 2) {
//			DeformTunnelFanFilter.instance(p).setTime(filterTime);
//			DeformTunnelFanFilter.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 3) {
			EdgesFilter.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 4) {
			MirrorFilter.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 5) {
			WobbleFilter.instance(p).setTime(filterTime);
			WobbleFilter.instance(p).setSpeed(0.5f);
			WobbleFilter.instance(p).setStrength(0.0004f);
			WobbleFilter.instance(p).setSize( 200f);
			WobbleFilter.instance(p).applyTo(pg);
//			} else if(textureEffectIndex == 6) {
//				InvertFilter.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 7) {
			RadialRipplesFilter.instance(p).setTime(filterTime);
			RadialRipplesFilter.instance(p).setAmplitude(0.5f + 0.5f * P.sin(filterTime));
			RadialRipplesFilter.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 8) {
			BadTVLinesFilter.instance(p).applyTo(pg);
//			} else if(textureEffectIndex == 9) {
//				EdgesFilter.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 10) {
			CubicLensDistortionFilterOscillate.instance(p).setTime(filterTime);
			CubicLensDistortionFilterOscillate.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 11) {
			SphereDistortionFilter.instance(p).applyTo(pg);
//		} else if(textureEffectIndex == 12) {
//			HalftoneFilter.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 13) {
			PixelateFilter.instance(p).setDivider(15f, pg.width, pg.height);
			PixelateFilter.instance(p).applyTo(pg);
//		} else if(textureEffectIndex == 14) {
//			DeformBloomFilter.instance(p).setTime(filterTime);
//			DeformBloomFilter.instance(p).applyTo(pg);
//		} else if(textureEffectIndex == 15) {
//			DeformTunnelFanFilter.instance(p).setTime(filterTime);
//			DeformTunnelFanFilter.instance(p).applyTo(pg);
		} else if(textureEffectIndex == 16) {
			HueFilter.instance(p).setTime(filterTime);
			HueFilter.instance(p).applyTo(pg);
		}
//			WarperFilter.instance(p).setTime( _timeEaseInc / 5f);
//			WarperFilter.instance(p).applyTo(pg);
//			ColorDistortionFilter.instance(p).setTime( _timeEaseInc / 5f);
//			ColorDistortionFilter.instance(p).setAmplitude(1.5f + 1.5f * P.sin(radsComplete));
//			ColorDistortionFilter.instance(p).applyTo(pg);
//			OpenGLUtil.setTextureRepeat(_buffer);

	}
	
	/////////////////////////////////////////////////////////////////
	// SPECIAL INTERSTITIAL MODE 
	/////////////////////////////////////////////////////////////////

	protected void drawInterstitial() {
		float interstitialAlpha = (p.midiState.midiCCPercent(midiInChannel, interstitialKnob) != 0) ? p.midiState.midiCCPercent(midiInChannel, interstitialKnob) : 0;
		if(interstitialAlpha > 0) {
			imageCycler.update();
			_pg.beginDraw();
			DrawUtil.setPImageAlpha(_pg, interstitialAlpha);
			ImageUtil.drawImageCropFill(imageCycler.image(), _pg, false);
			DrawUtil.resetPImageAlpha(_pg);
			_pg.endDraw();
		}
	}
	
	
	/////////////////////////////////////////////////////////////////
	// Debug textures
	/////////////////////////////////////////////////////////////////


	protected void outputTestImages(ArrayList<BaseTexture> texturePool) {
		for(BaseTexture tex : texturePool) {
			tex.update();
			tex.update();
			tex.update();
			tex.texture().save(FileUtil.getHaxademicOutputPath() + "hax-visual-textures/" + tex.toString());
			P.println("output: ", tex.toString());
		}
	}


}
