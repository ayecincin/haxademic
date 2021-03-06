<img src="https://raw.githubusercontent.com/cacheflowe/haxademic/master/data/haxademic/images/haxademic-logo.png" alt="Haxademic" width="160"/>

# Haxademic

Haxademic is my personal Processing-based creative coding toolkit, built to run in Eclipse with Java and the latest version of [Processing](http://processing.org/). It's a starting point for interactive visuals, installations, rendering and writing Processing "sketches" in a more robust environment. It loads a bunch of useful Java/Processing libraries and contains a ton of other useful tools that I've written.

## State of affairs

While the code has been open-sourced, I don't plan on making it easy/viable for others to use - I don't have the time or desire to support it. This repository is more about sharing the interesting code within, and I fully endorse borrowing it however you see fit.

## Notable code

Below you'll find a long list of classes and utilities that I've built to make my life easier. I've tried to make as many basic [demos](https://github.com/cacheflowe/haxademic/tree/master/src/com/haxademic/demo) as possible for all of these features. The `demo` package mostly mirrors the `core` directory and should give you an idea of how to implement these object on your own.

### App

`com.haxademic.core.app`

* __[PAppletHax](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/app/PAppletHax.java)__ - This is the base class for every Processing app that I build. It initializes tools for app-level concerns, rendering, multiple input devices, audio FFT, and debugging tools. Tons of additional tools and utilities can be found in the library.

* __[P](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/app/P.java)__ - This class holds static references and methods - primarily to the app instance so I don't have to pass it around everywhere.

`com.haxademic.core.app.config`

* __[AppSettings](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/app/config/AppSettings.java)__ - Static constants to help set app properties and initialize special objects in `PAppletHax`. Used in tandem with `P5Properties`.

* __[P5Properties](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/app/config/P5Properties.java)__ - Loads properties from `data/properties/run.properties` (or an alternate specified .properties file), using the same string constants in `AppSettings`. All of these properties can be overridden in PAppletHax in the automatically-called `overridePropsFile()` function on app initialization.

### Audio

`com.haxademic.core.audio`

* __[NormalizeMonoWav](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/NormalizeMonoWav.java)__ - Normalizes a mono .wav file without any external libraries.

* __[WavPlayer](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/WavPlayer.java)__ - Play a .wav file and cache for future plays.

* __[WavRecorder](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/WavRecorder.java)__ - Record a .wav file.

`com.haxademic.core.audio.analysis.input`

* __[IAudioInput](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/analysis/input/IAudioInput.java)__ - A common interface between several Java libraries that run FFT analysis and beat detection on an incoming audio signal. Choose between [Beads](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/analysis/input/AudioInputBeads.java), [Minim](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/analysis/input/AudioInputMinim.java), [ESS](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/analysis/input/AudioInputESS.java) or [Processing Sound](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/analysis/input/AudioInputProcessingSound.java), via [AppSettings](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/demo/audio/analysis/Demo_IAudioInput.java).

* __[AudioStreamData](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/audio/analysis/input/AudioStreamData.java)__ - The common data storage object for audio analysis results.

### Data

`com.haxademic.core.data`

* __[ConvertUtil](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/data/ConvertUtil.java)__ - A collection of basic Java type-conversion methods..

`com.haxademic.core.data.constants`

* __[GLBlendModes](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/data/constants/GLBlendModes.java)__ - Fancy OpenGL blend modes with helper methods to set them on a given PGraphics context.

* __[PBlendModes](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/data/constants/PBlendModes.java)__ - Static constants list of Processing blend modes.

* __[PRenderers](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/data/constants/PRenderers.java)__ - Static constants list of Processing renderers.

`com.haxademic.core.data.store`

* __[AppStore](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/data/store/AppStore.java)__ - A singleton data store and emitter. As values are updated, subscribers are notified. Uses __[IAppStoreListener](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/data/store/IAppStoreListener.java)__ as the callback interface for `AppStore` updates.

* __[AppStoreDistributed](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/data/store/AppStoreDistributed.java)__ - A WebSockets-enabled extension of `AppStore` to keep multiple machines in sync with a shared data structure.

### Debug

`com.haxademic.core.debug`

* __[DebugView](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/debug/DebugView.java)__ - Instantiated with every `PAppletHax` app, allows us to toggle and add properties to the `DebugView` HashMap to show realtime values on-screen, rather than trying to watch Java console values fly by. Press `/` to toggle. Also includes a panel for key commands and extra developer-defined info.

### Draw

`com.haxademic.core.draw.camera`

* __[CameraUtil](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/camera/CameraUtil.java)__ - Primarily just a helper method to increase the camera distance on a PGraphics context.

`com.haxademic.core.draw.color`

* __[ColorUtil](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/color/ColorUtil.java)__ - Handy color conversion methods.

* __[EasingColor](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/color/EasingColor.java)__ - An object that represents a single color with interpolation and helpful getter/setter methods. Has some overlap with `ColorUtil` - the two classes should possibly be merged.

* __[ImageGradient](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/color/ImageGradient.java)__ - Loads an image and samples its colors horizontally. Works well for gradients and comes with a library of presets.

`com.haxademic.core.draw.context`

* __[DrawUtil](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/context/DrawUtil.java)__ - Lots of static helper methods to set properties on the specified PGraphics context. I use this constantly.

* __[OpenGL32Util](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/context/OpenGL32Util.java)__ - Some helper methods to move graphics between 8-bit Processing graphics for display and PixelFlow 32-bit graphics contexts for higher-resolution drawing and shaders.

* __[OpenGLUtil](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/context/OpenGLUtil.java)__ - Lower-level helper methods to set OpenGL flags that aren't obviously available in Processing.

* __[OrientationUtil](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/context/OrientationUtil.java)__ - Helper methods to orient the graphics context rotation towards a specific 3d location. These utilities help point 3d objects toward each other.

`com.haxademic.core.draw.filters.pgraphics`

* __[A collection](https://github.com/cacheflowe/haxademic/tree/master/src/com/haxademic/core/draw/filters/pgraphics)__ of PGraphics-based image/video filters. These effects redraw an image in traditional Processing style on a PGraphics buffer.

`com.haxademic.core.draw.filters.pshader`

* __[A collection](https://github.com/cacheflowe/haxademic/tree/master/src/com/haxademic/core/draw/filters/pshader)__ of (over 100) PShader filter effects. These Java classes are convenience wrappers for GLSL programs/files and provide a common interface and (as minimal as) one-line implementations for shader filters. Shader uniforms are exposed through public setter methods.

`com.haxademic.core.draw.image`

* __[AnimatedGifEncoder](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/AnimatedGifEncoder.java)__ - Renders a gif file directly out of a Processing app. I've mostly abandoned this in favor of rendering a video, then using conversion tools like ffmpeg to more precisely convert to gif.

* __[Base64Image](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/Base64Image.java)__ - Encodes and decodes between a PImage and a base64-encoded string.

* __[BufferActivityMonitor](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/BufferActivityMonitor.java)__ - Gives us an "activity" rating of a moving image, like a webcam, video, or other PGraphics instance that animates. Useful for motion detection.

* __[BufferColorObjectDetection](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/BufferColorObjectDetection.java)__ - Finds the center of mass of a specific color (within a threshold) on an image. Useful for object tracking in controlled situations.

* __[BufferMotionDetectionMap](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/BufferMotionDetectionMap.java)__ - Uses shaders for fast frame-diffing to detect motion between video/camera frames. Includes helpers to find random motion points in the motion map result.

* __[BufferThresholdMonitor](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/BufferThresholdMonitor.java)__ - Copies an image to a low-res buffer, runs a threshold filter, and counts white vs black pixels. Useful as a step in certain CV processes.

* __[ImageCyclerBuffer](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/ImageCyclerBuffer.java)__ - Uses GLSL transitions to create a slideshow from an array of PImages.

* __[ImageSequenceMovieClip](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/ImageSequenceMovieClip.java)__ - Loads and plays back an image sequence like a Movie object does for video files.

* __[ImageSequenceRecorder](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/ImageSequenceRecorder.java)__ - Builds an array of PGraphics objects to record recent video frames and play them back for time distortion effects or short video compiling.

* __[ImageUtil](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/ImageUtil.java)__ - Lots of tools for dealing with images and drawing them to screen.

* __[MovieToImageSequence](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/MovieToImageSequence.java)__ - Loads frames from a video into an array of PImages in realtime. Useful for fancier & faster playback/scrubbing through video frames without relying on Movie decoding once the frames are extracted.

* __[SimplexNoiseTexture](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/SimplexNoiseTexture.java)__ - Fast simplex noise.

* __[TickerScroller](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/TickerScroller.java)__ - Repeats a texture across a PGraphics and scrolls.

* __[TiledTexture](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/image/TiledTexture.java)__ - A drawing helper that takes advantage of OpenGL's texture repeat function and lets us draw a rectangle with a texture fill. Includes zoom & rotation controls for fancy texture & pattern tiling.

`com.haxademic.core.draw.mapping`

... coming soon

`com.haxademic.core.draw.particle`

... coming soon

`com.haxademic.core.draw.shapes`

... coming soon

`com.haxademic.core.draw.textures.pgraphics`

* __[A collection](https://github.com/cacheflowe/haxademic/tree/master/src/com/haxademic/core/draw/textures/pgraphics)__ of PGraphics-based realtime textures, originally built as "VJ" clips/layers.

`com.haxademic.core.draw.textures.pshader`

* __[A collection](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/draw/textures/pshader/TextureShader.java)__ of (over 100) realtime shader textures, both original creations (prefixed with 'cacheflowe') and shaders ported from other artists on the web. Check the GLSL source for credits.

### File

* __[DemoAssets](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/file/DemoAssets.java)__ - A collection of media files (svg, obj, png, ttf, mp4) to help quickly load an asset for demo purposes. Files are pulled from `data/haxademic/`.

* __[FileUtil](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/file/FileUtil.java)__ - File & directory methods to help with file creation, deletion & listing.

* __[PrefToText](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/file/PrefToText.java)__ - Saves & retries a float/int/String value from a text file.

* __[WatchDir](https://github.com/cacheflowe/haxademic/blob/master/src/com/haxademic/core/file/WatchDir.java)__ - Watches a directory & provides a delegate callback when files have changed.

### Hardware

... coming soon

### Math

... coming soon

### Net

... coming soon

### Render

... coming soon

### System

... coming soon

### Text

... coming soon

### UI

... coming soon



... more to come

## Dependencies

Haxademic uses the following Java & Processing libraries, which I've included in this repository so you don't have to find them yourself (more on that below):

* [Processing Core](http://processing.org/) (view the [Processing for Eclipse instructions](http://processing.org/learning/eclipse/))
* [Beads](http://www.beadsproject.net/)
* [blobDetection](http://www.v3ga.net/processing/BlobDetection/)
* [ControlP5](http://www.sojamo.de/libraries/controlP5/)
* [DMXP512](https://github.com/hdavid/dmxP512)
* [ESS](https://web.archive.org/web/20171209153252/http://www.tree-axis.com/Ess/)
* [Geomerative](http://www.ricardmarxer.com/geomerative/)
* [He_Mesh](http://hemesh.wblut.com/)
* [Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket)
* [Java Image Filters](http://www.jhlabs.com/ip/filters/index.html)
* [jetty](https://www.eclipse.org/jetty/)
* [Joons renderer](https://github.com/joonhyublee/joons-renderer/wiki)
* [KinectPV2](https://github.com/ThomasLengeling/KinectPV2)
* [Leap Motion for Processing](https://github.com/voidplus/leap-motion-processing/)
* [minim](http://code.compartmental.net/tools/minim/)
* [OpenKinect for Processing](https://github.com/shiffman/OpenKinect-for-Processing)
* [oscP5](http://www.sojamo.de/libraries/oscP5/)
* [PixelFlow](https://github.com/diwi/PixelFlow)
* [Poly2Tri](https://github.com/orbisgis/poly2tri.java)
* [Super CSV](http://supercsv.sourceforge.net/)
* [simple-openni](https://github.com/totovr/SimpleOpenni)
* [themidibus](https://github.com/sparks/themidibus)
* [toxiclibs](http://toxiclibs.org/)
* [UDP Processing Library](http://ubaa.net/shared/processing/udp/)
* [UMovieMaker](https://github.com/mariuswatz/modelbuilder)


## Installing / Compiling

* If you're on OS X, it's helpful to see hidden files. Run this command in Terminal:
	* `defaults write com.apple.finder AppleShowAllFiles YES`
* Download the standard Eclipse IDE for Java development, and the Java Development Kit itself:
	* [Eclipse](http://www.eclipse.org/)
	* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - After installing JDK 1.8, open Eclipse, go to **Preferences** then **Java -> Installed JREs**, and click **Search...** to have Eclipse find the newly-installed library.
* Clone or [download](https://github.com/cacheflowe/haxademic/archive/master.zip) the `haxademic` project
* Open Eclipse and: **File -> Import -> General / Existing Projects into Workspace**
	* Choose the `haxademic` directory that you cloned/downloaded, press **Finish**, and the project should be ready to use.
* In the **Package Explorer** in Eclipse, right-click the `lib` directory and select **Refresh**. This will let Eclipse know that you've added the appropriate libraries on your file system.
* Make sure you're compiling with Java 1.8:
	* Right-click the `haxademic` project in the **Package Explorer** or **Navigator** window and click **Properties**
	* Click the **Java Compiler** section and check the **Enable project specific settings** box on the right
	* Select **1.8** as your **Compiler compliance level**, if possible
	* If "Configure the **Installed JREs**" is shown at the bottom of this window, click that, make sure the **1.8** item is checked, then click OK.
* Right-click on any of the demo apps within `src/com/demo/` and choose **Run As -> Java Application** from the menu. This will create a run configuration for the app.
* If it's necessary to add more RAM to the app, go to **Run -> Run Configurations**, select your app and add the following **VM Arguments** when running the Java Application to increase memory allocated to your app. This is a minimum of 1gb and a maximum of 4gb of RAM:
	* `-Xms1G`
	* `-Xmx4G`

## Licensing

The Haxademic codebase and apps are [MIT licensed](https://raw.github.com/cacheflowe/haxademic/master/LICENSE), so do what you want with these files. Feel free to let me know that you're using it for something cool. I've added 3rd-party .jar files and compiled Java libraries that I'm probably not actually allowed to redistribute here, so if you're the owner of one of those libraries and would like the files removed, let me know. I've included them to aid those who would like a quick start and not have to search for the many dependencies of this project. Some of these libraries have disappeared from the web entirely, so searching for them may be futile anyway. I just want people to make cool things with this library, and hope you understand.
