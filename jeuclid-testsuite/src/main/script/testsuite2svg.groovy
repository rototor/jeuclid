import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sourceforge.jeuclid.*
import net.sourceforge.jeuclid.context.*;
import net.sourceforge.jeuclid.testsuite.TestSuiteProcessor;

TestSuiteProcessor p = TestSuiteProcessor.getInstance();

log.info("Converting testsuite2...");

scanner = ant.fileScanner {
  fileset(dir:"${project.basedir}/src/main/resources/mml2-testsuite") {
    include(name:"**/*")
  }
}

def target = "target/site/svg"

for (f in scanner) {
  f2 = new File(f.getPath().replace("src/main/resources/mml2-testsuite","target/site/svg"))
  ant.mkdir(dir: f2.getParent())

  def processed = false;

  if (f.getPath().endsWith(".xml")) {
    def inputSource = new StreamSource(f)
    def result = new StreamResult(f2)
    processed = p.process(inputSource,result,false)
  }

  if (!processed) {
    ant.copy(file: f, tofile:f2)
  }
}

