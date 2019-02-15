package net.sourceforge.jeuclid.fop.plugin;

import org.apache.fop.apps.FopFactory;

public class JEuclidFopFactoryConfigurator {
    public static void configure(final FopFactory fopFactory) {
        fopFactory.getElementMappingRegistry().addElementMapping(
                new net.sourceforge.jeuclid.fop.JEuclidElementMapping());
        fopFactory.getXMLHandlerRegistry().addXMLHandler(
                new net.sourceforge.jeuclid.fop.JEuclidXMLHandler());

        final org.apache.xmlgraphics.image.loader.spi.ImageImplRegistry images =
                fopFactory.getImageManager().getRegistry();

        images.registerLoaderFactory(
                new net.sourceforge.jeuclid.xmlgraphics.ImageLoaderFactoryMathML());
        images.registerPreloader(
                new net.sourceforge.jeuclid.xmlgraphics.PreloaderMathML());
        images.registerConverter(
                new net.sourceforge.jeuclid.xmlgraphics.ImageConverterMathML2G2D());
    }
}
