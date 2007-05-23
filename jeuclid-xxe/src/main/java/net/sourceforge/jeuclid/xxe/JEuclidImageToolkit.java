package net.sourceforge.jeuclid.xxe;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import com.xmlmind.xmledit.doc.Element;
import com.xmlmind.xmledit.imagetoolkit.ImageConverter;
import com.xmlmind.xmledit.imagetoolkit.ImageRenderer;
import com.xmlmind.xmledit.imagetoolkit.ImageToolkit;

public class JEuclidImageToolkit implements ImageToolkit {

// public static PrintStream DEBUG;

// static {
// try {
// JEuclidImageToolkit.DEBUG = new PrintStream("/tmp/Mylog", "UTF-8");
// } catch (final FileNotFoundException e) {
// } catch (final UnsupportedEncodingException e) {
// }
// }

    /** {@inheritDoc} */
    public String getDescription() {
        // JEuclidImageToolkit.DEBUG.println("getDescription");
        return "Support MathML content through the use of JEuclid.\n"
                + "Please see http://jeuclid.sf.net/ for more information.";
    }

    /** {@inheritDoc} */
    public ImageConverter getImageConverter(final File arg0, final File arg1) {
        // JEuclidImageToolkit.DEBUG.println("getImageConverter");
        return null;
    }

    /** {@inheritDoc} */
    public ImageRenderer getImageRenderer(final URL url) {
        final String path = url.getPath();
        final String extension = path.substring(path.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ENGLISH);

        if ("mml".equals(extension) || "odf".equals(extension)) {
            return JEuclidImageRenderer.getRenderer();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public ImageRenderer getImageRenderer(final byte[] arg0) {
        // JEuclidImageToolkit.DEBUG.println("getImageRenderer/byte[]");
        return null;
    }

    /** {@inheritDoc} */
    public ImageRenderer getImageRenderer(final Element arg0) {
        // JEuclidImageToolkit.DEBUG.println("getImageRenderer/Element " +
        // arg0);
        return null;
    }

    /** {@inheritDoc} */
    public String getName() {
        // JEuclidImageToolkit.DEBUG.println("getName");
        return "JEuclid MathML image plugin";
    }

}
