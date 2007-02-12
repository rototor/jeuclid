package net.sourceforge.jeuclid.util;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.app.MathViewer;
import net.sourceforge.jeuclid.swing.JMathComponent;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.swing.SwingUtilities2;

public class ViewerTests {

	/**
	 * Test string with xml header.
	 */
	public static String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
			+ "<mrow><munderover><mo>&#x0222B;</mo><mn>1</mn><mi>x</mi></munderover>"
			+ "<mfrac><mi>dt</mi><mi>t</mi></mfrac></mrow></math>";

	@Test
	public void testViewer() throws Exception {
		Document doc = getDocument(TEST1);
		displayDocument(doc);
	}

	public static Document getDocument(String input) throws SAXException,
			ParserConfigurationException, IOException {
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		Document document;
		final DocumentBuilder parser = documentBuilderFactory
				.newDocumentBuilder();
		parser.setEntityResolver(new ResourceEntityResolver());
		document = parser.parse(new InputSource(new StringReader(input)));
		return document;
	}

	public void displayDocument(final Document document) throws InterruptedException, InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Test MathComponent");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().setLayout(new BorderLayout());
				JMathComponent component = new JMathComponent();
				component.setDocument(document);
				component.setDebug(false);
				frame.getContentPane().add(component, BorderLayout.CENTER);
				frame.setVisible(true);
				frame.pack(); 
				
			}
		});
	}
}
