import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sourceforge.jeuclid.*
import net.sourceforge.jeuclid.context.*;
import net.sourceforge.jeuclid.testsuite.TestSuiteProcessor;
import java.util.concurrent.*;

def es;

class TheTask implements Runnable {

  def f;
  def tsp;
  def source;
  def target;
  def ext;
  def ant;
  def mod3;

  public TheTask(file,t,s,tg,e,a,m) {
    this.f = file;
    this.tsp = t;
    this.source = s;
    this.target = tg;
    this.ext = e;
    this.ant = a;
    this.mod3 = m;
  }
  
  public void run() {
    def f2 = new File(f.getPath().replace(source,target))
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
    es.submit(new TheTask(f,tsp,source,target,ext,ant,mod3));
  }

}

int numberOfProcessors = runtime.availableProcessors();
es = Executors.newFixedThreadPool(numberOfProcessors)+1;

log.info("Converting testsuite2...");
doit("xml","mml2-testsuite","svg",false);
log.info("Converting testsuite3...");
doit("xhtml","mml3-testsuite","svg3",true);

es.shutdown();
while (!es.isTerminated()) {
  try {
    es.awaitTermination(99, TimeUnit.DAYS);
  } catch (InterruptedException ex) {
    ex.printStackTrace();
  }
}
