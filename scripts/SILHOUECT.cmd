REM @echo off
cd ..
timeout 5

"C:\Program Files\Java\jre1.8.0_162\bin\javaw.exe" -Xmx4G -Xms2G -Djava.library.path=C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\sound\library\macosx;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\serial\library\macosx;C:\Users\dancelab\Documents\GitHub\haxademic\lib\LeapMotionForProcessing\library\macosx;C:\Users\dancelab\Documents\GitHub\haxademic\lib\KinectPV2\library -Dfile.encoding=Cp1252 -classpath "C:\Program Files\Java\jre1.8.0_162\lib\resources.jar;C:\Program Files\Java\jre1.8.0_162\lib\rt.jar;C:\Program Files\Java\jre1.8.0_162\lib\jsse.jar;C:\Program Files\Java\jre1.8.0_162\lib\jce.jar;C:\Program Files\Java\jre1.8.0_162\lib\charsets.jar;C:\Program Files\Java\jre1.8.0_162\lib\jfr.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\cldrdata.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\dnsns.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\jaccess.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\jfxrt.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\localedata.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\nashorn.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\sunec.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\sunmscapi.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jre1.8.0_162\lib\ext\zipfs.jar;C:\Users\dancelab\Documents\GitHub\haxademic\bin;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\core\library\core.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\core\library\gluegen-rt.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\core\library\jogl-all.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\video\library\gstreamer-java.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\video\library\jna.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\video\library\video.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\sound\library\sound.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\pdf\library\itext.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\pdf\library\pdf.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\serial\library\jssc.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\processing-3\libraries\serial\library\serial.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\Ess\library\Ess.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\toxiclibs-complete-0020\toxiclibs_p5\library\toxiclibs_p5.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\toxiclibs-complete-0020\toxiclibscore\library\toxiclibscore.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\toxiclibs-complete-0020\volumeutils\library\volumeutils.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\toxiclibs-complete-0020\colorutils\library\colorutils.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\oscP5\library\oscP5.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\OBJLoader\library\OBJLoader.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\geomerative\library\batikfont.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\geomerative\library\geomerative.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\SimpleOpenNI\library\SimpleOpenNI.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\blobDetection\library\blobDetection.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\Filters\dist\Filters.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\super-csv\super-csv-2.1.0.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\joons\library\janino.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\joons\library\sunflow73.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\newhull\library\newhull.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\LeapMotionForProcessing\library\LeapJava.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\LeapMotionForProcessing\library\LeapMotionForProcessing.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\UMovieMaker\library\monte-cc.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\UMovieMaker\library\UMovieMaker.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\poly2tri\poly2tri-core-0.1.1.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\poly2tri\slf4j-api-1.6.3.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\udp\library\udp.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\controlP5\library\controlP5.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\openkinect_processing\library\openkinect_processing.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\hemesh\library\trove-3.1a1.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\hemesh\library\exp4j.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\hemesh\library\hemesh-data-2_2_0.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\hemesh\library\hemesh-external-2_2_0.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\hemesh\library\javolution-6.1.0.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\hemesh\library\jts.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\hemesh\library\objparser.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\hemesh\library\hemesh.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\KinectPV2\library\KinectPV2.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\themidibus\library\themidibus.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\GIFAnimation\library\GifAnimation.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\java_websocket\java_websocket.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\jetty\jetty-all-9.4.7.v20170914-uber.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\minim\library\jl1.0.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\minim\library\jsminim.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\minim\library\minim.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\minim\library\mp3spi1.9.4.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\minim\library\tritonus_aos.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\minim\library\tritonus_share.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\beads\library\beads.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\beads\library\jarjar-1.0.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\beads\library\jl1.0.1.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\beads\library\org-jaudiolibs-audioservers-jack.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\beads\library\org-jaudiolibs-audioservers-javasound.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\beads\library\org-jaudiolibs-audioservers.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\beads\library\org-jaudiolibs-jnajack.jar;C:\Users\dancelab\Documents\GitHub\haxademic\lib\beads\library\tools.jar" com.haxademic.app.silhouect.Silhouect

cd scripts