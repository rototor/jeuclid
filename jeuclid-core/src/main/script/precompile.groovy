import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary;

File basedir = new File("${project.basedir}");

log.info("Precompiling XSLT stylesheet...");
try {
  Source stylesheet = new StreamSource(new File(basedir,"target/classes/content/mathmlc2p.xsl"))
  TransformerFactory factory = TransformerFactory.newInstance();
  factory.setAttribute("generate-translet", Boolean.TRUE);
  factory.setAttribute("package-name", "translet");
  File destDir = new File(basedir,"target/classes");
  factory.setAttribute("destination-directory", destDir.getAbsolutePath());
  Templates templates = factory.newTemplates(stylesheet);

  OutputStream os = new FileOutputStream(new File(basedir,"target/classes/content/mathmlc2p.ser"));
  ObjectOutput oo = new ObjectOutputStream(os);
  oo.writeObject(templates);
  oo.close();
} catch (Exception e) {
  log.info("Could not precompile stylesheet (optional step)");
  log.info("Reason: "+e.getMessage());
}

log.info("Preloading operator dictionary...");
Object dict = OperatorDictionary.getInstance().getDict();
os = new FileOutputStream(new File(basedir,"target/classes/moDictionary.ser"));
oo = new ObjectOutputStream(os);
oo.writeObject(dict);
oo.close();
