# TODO

* Art projects
  * Adapt poly lerp/feedback loop displacing a sheet
  * Sound in space. Raspberry Pis via wifi, attached to speakers. Split channels for more outputs
  * New video loops should have a soundtrack. use my tunes? look in sketch/render/avloops
  * Kinect history textures in 3d
  	* Kinect history point mesh history
  * Convert all webcam VFX apps (and old video filters) to BaseVideoFilter subclasses
  * Moire sphere/shader for MW prototying
  * Voice-activated color room: What Say Hue?
  * Interphase
  	* Integrate HaxVisualTwo
  	* Integrate Launchpad direct interface
  * Grass cutout - laser cut w/Seied
  * Make a dmx gif loop
  * Motion detection point-of-interest motion capture by small rectangles - figure out how to zoom out and create multiple zones
  * Turn client snake mirror into its own thing
  * Make a version of partycles with GPU particles from VFX code and ability to swap webcam instead of Kinect
	* Also, blob tracking VFX but sweet patterns inside the blobs
* Audio
  * Sphinx4 speech recognition
	* Copy / paste + Robot for tired hands
  * Test basic audio input. why is audio getting delayed after hours of running?
  * Turn off Beads audio input object output - this should not pass through
  * Split audio stepthrough rendering from Renderer.java, which should just save movies. MIDIStepthrough renderer is a good example of splitting
  * Make demos for rendering at a specific bpm
* MIDI:
  * Move midibus instance to MidiState (now MidiDevice)
  * InputTrigger should merge MIDI buttons and CC controls, just like OSC commands
  * Check MIDI rendering now that MIDI code has been revamped
* DMX
  * Bring timeclock app into Haxademic as a new, more robust video-to-dmx demo/app
* GLSL
  * Wrap up GLSL transitions collection and make a common interface
  * Fix up GPU particle launcher to store colors per-particle
  * Convert Orbit noise: https://www.shadertoy.com/view/4t3yDn
  * Convert some postprocessing effects: https://github.com/libretro/glsl-shaders
  * Figure out `particle-displace-curl.glsl`
  * Add `feedback-map.glsl` & `feedback-radial.glsl` shader wrapper classes
  * Build a post-processing library: https://github.com/processing/processing/wiki/Library-Basics
  * Demo_VertexShader_NoiseTest_WIP
  	* make a trexture that does audioreactive stripes emitting from the top down
  * Delete old displacement shaders since we have a new wrapper object
  * Optical flow glsl port - ported glsl file (with 2nd reference) is ready to fix up
  * Notes from book
	* Shader uniform updates should check if dirty before sending to shader
    * Look into structs and output from a fragment shader
    * Look at vertex attributes - Is that an array of values?
    	* Example here: https://github.com/gohai/processing-glvideo/blob/master/examples/VideoMappingWithShader/VideoMappingWithShader.pde
    * Data exits vertex processing by user-defined varying variables
    * gl_Position can be null and not be rendered?
    * gl_PointSize can be written to in vertex shader
    * gl_fragCoord.z has depth data for the fragment?!
    * Build a basic demo that uses vertex depth to fade to a color- probably already have something similar
    * Does textureSize(Sampler2D) give us the texture size???
    * Doing calculations in the vertex shader should always be faster than the fragment shader, since there are fewer vertices than fragments.
    * Use the ‘discard’ keyword to *not* update a fragment, anywhere in a fragment shader.
  * GPU Particles
	* http://barradeau.com/blog/?p=621
	* Look into Processing shader types - is there a point shader? yes - https://processing.org/tutorials/pshader/
	* https://codeanticode.wordpress.com/2014/05/08/shader_api_in_processing_2/
	* http://atduskgreg.github.io/Processing-Shader-Examples/
	* http://www.beautifulseams.com/2013/04/30/shaders/
	* https://github.com/codeanticode/pshader-tutorials
* SystemUtil:
  * Merge Windows & normal SystemUtil - make sure Java-killing code works on both OS X & Windows
* net
  * Should Screenshot in DashboardPoster be it's own app/process, like the CrashMonitor?
  * PrefsSliders should also serve up a web server that has just those sliders. .json config maybe?
  * WebServer and SocketServer should be more stylistically similar. See PORT in WebServer, and DEBUG static boolean - should be passed in?
  * Replace JavaWebsocket with Jetty WebSocket server??
* Demos:
  * Render a video with effects, using BrimVFX as example
  * Replicate indpendent 40k shape demo from Processing examples - update with GPU
  * Make a little planet generator with icosahedron deformation and colorized texture map of depth
    * https://github.com/ashima/webgl-noise/wiki
  * Distill more demos for `core` code
  * Make a texture map by drawing optical flow to ellipses and blurring
* PShape & PShapeUtil:
  * sine-distorted 3d models
  * Scrolling feedback texture mapped to a model with lighting
  * Move around a sphere (advice from EdanKwan: Generate the vector field on a sphere. Cross the 3d noise with the surface normal. Make the items move around on the surface of the sphere, set the initial direction of the items, move them with the noise field, normalize its position (assuming the sphere origin is center with 1 radius).
  * Make a vertex shader that does this to a sheet: https://www.google.com/search?ei=z9e3Wo6iOdG45gKIyZrgAQ&q=graph+z%3Dsin%28y*0.1%29*sin%28x%29&oq=graph+z%3Dsin%28y*0.1%29*sin%28x%29&gs_l=psy-ab.3...11324.12507.0.13684.4.4.0.0.0.0.72.277.4.4.0....0...1c.1.64.psy-ab..0.1.69...0i8i30k1.0.tqpD6rWz4Hk
  * PShapeUtil: Build a demo for changing the x/z registration/center point so models that sit on the ground can spin from the right origin (controlP5 ?)
  * Extrude2dPoints should be able to return a PShape
    * Shapes.java should have a filled or open extruded polygon generator method
	* PShapeSolid should loop properly through children like PShapeUtil does now
  * GIVE A MODEL FRONT & BACK TEXTURES! do wrapping the same way as now, but a different texture if negative z (or normal?)
  * 3d lighting w/glsl
    * Demo_VertexShader_NoiseTest_WIP
    * Demo_VertexShader_Fattener
* General / tools
  * Test importing a Java class into a Processing IDE project
  * How can we optimize for Raspberry Pi? It wants Java 1.7 for the old version of Eclipse :(
  * Look into JarSplice or other compiling tools for application deployment
  * Web interface to control PrefsSliders: Add JSON interface for PrefsSliders & WebServer/WebSockets?
  * Fix overhead view of KinectRegionGrid - with larger grids it's off-screen
  * BufferActivityMonitor should use FrameDifferenceBuffer object
  * Replace ColorUtil with EasingColor
  * Clean up /data directory with assets that can be used across demos, and move sketch assets into their own location
  * ImageSequence -> ffmpeg rendering from a class. Would make rendering easier on both platforms
  * Start moving all apps towards objects that can receive a PGraphics instance. decoupling from PApplet will help move visuals into HaxVisualTwo
  * Clean up old stuff - get rid of non-useful demos
  * Document important files/concepts/tools for README
