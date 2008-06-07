import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sourceforge.jeuclid.converter.Processor;

log.info("Converting testsuite...");

scanner = ant.fileScanner {
  fileset(dir:"${project.basedir}/src/main/resources") {
    include(name:"**/*")
  }
}

def target = "target/site/svg"

Processor p = Processor.getInstance();
for (f in scanner) {
  f2 = new File(f.getPath().replace("src/main/resources","target/site/svg"))
  ant.mkdir(dir: f2.getParent())

  def processed = false;

  if (f.getPath().endsWith(".xml")) {
    def inputSource = new StreamSource(f)
    def result = new StreamResult(f2)
    try {
      p.process(inputSource, result);
      processed = true;
    } catch (final TransformerException te) {
      processed = false;
    } catch (final NullPointerException npe) {
      processed = false;
    }
  }

  if (!processed) {
    ant.copy(file: f, tofile:f2)
  }
}

