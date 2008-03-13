import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sourceforge.jeuclid.app.foprep.Processor;

log.info("Converting testsuite...");

scanner = ant.fileScanner {
  fileset(dir:"${project.basedir}/src/main/resources") {
    include(name:"**/*")
  }
}

def target = "target/site/svg"

for (f in scanner) {
  f2 = new File(f.getPath().replace("src/main/resources","target/site/svg"))
  ant.mkdir(dir: f2.getParent())

  def processed = false;
  Processor p = Processor.getProcessor();

  if (f.getPath().endsWith(".xml")) {
    def inputSource = new StreamSource(new FileInputStream(f))
    def result = new StreamResult(new FileOutputStream(f2))
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

