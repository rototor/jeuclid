package net.sourceforge.jeuclid.util;

import java.awt.BorderLayout;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.swing.JMathComponent;
import net.sourceforge.jeuclid.test.MathBaseTest;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static org.testng.Assert.*;

public class ConverterTests {

	/**
	 * Test string with xml header.
	 */
	public static String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
			+ "<mrow><munderover><mo>&#x0222B;</mo><mn>1</mn><mi>x</mi></munderover>"
			+ "<mfrac><mi>dt</mi><mi>t</mi></mfrac></mrow></math>";

	@Test
	public void testConverter() throws Exception {
		Document doc = ViewerTests.getDocument(TEST1);
		File outFile = new File(getOutDir(),"test1.png");
		Map<ParameterKey, String> params = MathBase.getDefaultParameters();
		params.put(ParameterKey.FontSize,"25");
    	params.put(ParameterKey.OutFileType, "image/png");
		Converter.convert(doc, outFile, params);
		assertTrue(outFile.exists());
		assertTrue(outFile.length() > 0);
		
		//displayDocument(doc);
	}
	
	public File getOutDir() {
		File outDir = new File("temp");
		if (!outDir.isDirectory())
			outDir.mkdirs(); 
		return outDir;
	}
	
	/**
	 * Tests the examples at resources/test/exampleX.mml.
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
	@Test
	public void testConvertEmbeddedExamples() throws Exception {
		Map<ParameterKey, String> params = MathBase.getDefaultParameters();
		params.put(ParameterKey.FontSize,"25");
    	params.put(ParameterKey.OutFileType, "image/png");	
   	
		for (int example = 1; example <= 8; example++) {
			String exName = "example" + example + ".mml";
			File outFile = new File(getOutDir(),"example" + example + ".png");			
			Document document = MathBaseTest.loadDocument(exName);
			Converter.convert(document, outFile, params);
			assertTrue(outFile.exists());
			assertTrue(outFile.length() > 0);
		}
	}	

}
