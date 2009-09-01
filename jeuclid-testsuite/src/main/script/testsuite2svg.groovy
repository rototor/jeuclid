import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sourceforge.jeuclid.*
import net.sourceforge.jeuclid.context.*;
import net.sourceforge.jeuclid.testsuite.TestSuiteProcessor;

def doit(extension,sourcelast,targetlast,mod3) {

def tsp = TestSuiteProcessor.getInstance();
def source = "src/main/resources/"+sourcelast;
def target = "target/site/"+targetlast;
def ext = "."+extension;

scanner = ant.fileScanner {
  fileset(dir:"${project.basedir}/"+source) {
    include(name:"**/*")
  }
}

for (f in scanner) {
  f2 = new File(f.getPath().replace(source,target))
  ant.mkdir(dir: f2.getParent())

  def processed = false;

  if (f.getPath().endsWith(ext)) {
    def inputSource = new StreamSource(f)
    def result = new StreamResult(f2)
    processed = tsp.process(inputSource,result,mod3)
  }

  if (!processed) {
    ant.copy(file: f, tofile:f2)
  }
}

}

log.info("Converting testsuite2...");
doit("xml","mml2-testsuite","svg",false);
log.info("Converting testsuite3...");
doit("xhtml","mml3-testsuite","svg3",true);
