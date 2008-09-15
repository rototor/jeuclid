The build.xml file is to Ant what a makefile is to make.

If you can not or not want to use Maven, you can still use Ant build
system. Because Ant do not fetch all the necessary dependencies
automatically, you mast have them already in your system.

For compilation of any module use the following instructions (example
for jeuclid-core):

1) create directory jeuclid-core/lib
2) copy or link all the necessary dependencies into jeuclid-core/lib
   (see dependencies list bellow)
3) start the compilation from the directory jeuclid-core with
   command "ant"


Dependencies list:

* jeuclid-core
   - Ant (ant.jar)
   - Batik (batik-all.jar)
   - commons-logging (commons-logging.jar)
   - jcip-annotations (jcip-annotations.jar)
   - FreeHep Util [optional - see notes]
   - FreeHep Graphics2D [see notes]
   - xml-commons-external (xml-apis.jar)
   - xmlgraphics-commons (xmlgraphics-commons.jar)
* jeuclid-cli
   - commons-cli (commons-cli.jar)
   - commons-lang (commons-lang.jar)
   - jeuclid-core (jeuclid-core.jar)
   - xmlgraphics-commons (xmlgraphics-commons.jar)
* jeuclid-mathviewer
   - AppleJavaExtensions (AppleJavaExtensions.jar)
   - commons-logging (commons-logging.jar)
   - jeuclid-core (jeuclid-core.jar)
* jeuclid-fop
   - Batik (batik-all.jar)
   - commons-logging (commons-logging.jar)
   - FOP (fop.jar, fop-hyph.jar ~ optional)
   - jeuclid-core (jeuclid-core.jar)
   - xml-commons-external (xml-apis.jar)
   - xmlgraphics-commons (xmlgraphics-commons.jar)


Notes:

1) If you do not need FreeHep support, remove all files
   jeuclid-core/src/main/java/net/sourceforge/jeuclid/converter/FreeHep*
   and from the file ConverterRegistry.java remove this line:

     private ConverterRegistry() {
         ImageIODetector.detectConversionPlugins(this);
         BatikDetector.detectConversionPlugins(this);
-        FreeHepDetector.detectConversionPlugins(this);
     }

2) Gentoo users can use ebuilds retorted here:
   http://bugs.gentoo.org/show_bug.cgi?id=121822
