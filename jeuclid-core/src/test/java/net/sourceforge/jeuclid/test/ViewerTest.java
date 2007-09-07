package net.sourceforge.jeuclid.test;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import net.sourceforge.jeuclid.swing.JMathComponent;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class ViewerTest {

    /**
     * Test string with xml header.
     */
    final public static String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><munderover><mo>&#x0222B;</mo><mn>1</mn><mi>x</mi></munderover>"
            + "<mfrac><mi>dt</mi><mi>t</mi></mfrac></mrow></math>";

    @Test
    public void testViewer() throws Exception {
        // final Document doc = MathMLParserSupport.parseString(TEST1);
        // displayDocument(doc);
    }

    public void displayDocument(final Document document)
            throws InterruptedException, InvocationTargetException {
        final JFrame frame = new JFrame("Test MathComponent");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        final JMathComponent component = new JMathComponent();
        component.setDocument(document);
        component.setDebug(false);
        frame.getContentPane().add(component, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.pack();
        frame.dispose();

        // SwingUtilities.invokeAndWait(new Runnable() {
        // public void run() {
        // JFrame frame = new JFrame("Test MathComponent");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.getContentPane().setLayout(new BorderLayout());
        // JMathComponent component = new JMathComponent();
        // component.setDocument(document);
        // component.setDebug(false);
        // frame.getContentPane().add(component, BorderLayout.CENTER);
        // frame.setVisible(true);
        // frame.pack();
        // }
        // });
    }
}
