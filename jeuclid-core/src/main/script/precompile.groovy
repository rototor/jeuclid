import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary2;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary3;
import net.sourceforge.jeuclid.elements.support.text.CharacterMapping;

File basedir = new File("${project.basedir}");

//log.info("Precompiling XSLT stylesheet...");
//try {
//  Source stylesheet = new StreamSource(new File(basedir,"target/classes/content/mathmlc2p.xsl"))
//  TransformerFactory factory = TransformerFactory.newInstance();
//  factory.setAttribute("generate-translet", Boolean.TRUE);
//  factory.setAttribute("package-name", "translet");
//  File destDir = new File(basedir,"target/classes");
//  factory.setAttribute("destination-directory", destDir.getAbsolutePath());
//  Templates templates = factory.newTemplates(stylesheet);
//
//  OutputStream os = new FileOutputStream(new File(basedir,"target/classes/content/mathmlc2p.ser"));
//  ObjectOutput oo = new ObjectOutputStream(os);
//  oo.writeObject(templates);
//  oo.close();
//} catch (Exception e) {
//  log.info("Could not precompile stylesheet (optional step)");
//  log.info("Reason: "+e.getMessage());
//}

log.info("Preloading operator dictionary 2...");
File newDict2 = new File(basedir,"target/classes/net/sourceforge/jeuclid/moDictionary.ser");
File oldDict2 = new File(basedir,"target/classes/net/sourceforge/jeuclid/moDictionary.xml");
ant.delete(file:newDict2);
Object dict2 = OperatorDictionary2.getInstance();
os = new FileOutputStream(newDict2);
oo = new ObjectOutputStream(os);
oo.writeObject(dict2);
oo.close();
ant.delete(file:oldDict2);

log.info("Preloading operator dictionary 3...");
File newDict3 = new File(basedir,"target/classes/net/sourceforge/jeuclid/appendixc.ser");
File oldDict3 = new File(basedir,"target/classes/net/sourceforge/jeuclid/appendixc.xml");
ant.delete(file:newDict3);
Object dict3 = OperatorDictionary3.getInstance();
os = new FileOutputStream(newDict3);
oo = new ObjectOutputStream(os);
oo.writeObject(dict3);
oo.close();
ant.delete(file:oldDict3);

log.info("Preloading character mappings...");
File newMap = new File(basedir,"target/classes/net/sourceforge/jeuclid/charmap.ser");
File oldMap = new File(basedir,"target/classes/net/sourceforge/jeuclid/UnicodeData.txt");
ant.delete(file:newMap);
CharacterMapping map = CharacterMapping.getInstance();
os = new FileOutputStream(newMap);
oo = new ObjectOutputStream(os);
oo.writeObject(map);
oo.close();
ant.delete(file:oldMap);

log.info("Precompilation done!");
